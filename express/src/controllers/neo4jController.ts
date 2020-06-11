import { Request, Response } from 'express';
import neo4j, { Driver, Session } from 'neo4j-driver';
import cypher from './cypherController';

function transactionInformation (): { driver: Driver; session: Session } {
  const uri = process.env.NEO4J_URI;
  const auth = process.env.NEO4J_PASSWORD ? neo4j.auth.basic('neo4j', 'password') : null;

  const driver: Driver = neo4j.driver(uri, auth, { disableLosslessIntegers: true });
  const session: Session = driver.session();
  return { driver, session };
}

async function verifyConnection () {
  const { driver, session } = transactionInformation();
  try {
    await driver.verifyConnectivity();
    console.log('Verified Neo4j Connection');
  } catch (error) {
    console.log(`Connectivity Verification Failed: ${error}`);
  }
}

function startTransaction (cypher: string, res) {
  try {
    const transaction = transactionInformation();
    const resultPromise = transaction.session.writeTransaction(tx => tx.run(cypher));

    resultPromise.then(result => {
      transaction.session.close();
      transaction.driver.close();
      return res({
        result
      });
    }).catch(error => {
      console.log(error);
      return res({
        error
      });
    });
  } catch (error) {
    console.log(error);
    return (res({
      error
    }));
  }
}

function loadBundleNeo4j (_bundle, res: Response) {
  startTransaction(cypher.loadBundle(_bundle), (result) => {
    if (result) {
      return res.status(200).send(result);
    } else {
      return res.status(500).send({
        error: 'Error'
      });
    }
  });
}

function deleteAllNodes (req: Request, res: Response) {
  startTransaction(cypher.deleteAll(), (result) => {
    if (result.result) {
      return res.status(200).send('All nodes deleted');
    } else {
      return res.status(500).send(result.error);
    }
  });
}

function getBundle (_id: string, res: Response) {
  startTransaction(cypher.buildBundleAroundID(_id), (result) => {
    if (result.result) {
      const bundle = result.result.records[0]._fields[0];
      if (Object.keys(bundle).length === 0) {
        return res.status(400).send({
          message: `Entry with ID ${_id} not found`
        });
      }
      return res.status(200).send(bundle);
    } else {
      return res.status(500).send(result.error);
    }
  });
}

function getBundleWithFilter (_id: string, _filter: string, res: Response) {
  startTransaction(cypher.buildBundleAroundIDWithFilter(_id, _filter), (result) => {
    if (result.result) {
      const bundle = result.result.records[0]._fields[0];
      if (Object.keys(bundle).length === 0) {
        return res.status(400).send({
          message: `Entry with ID ${_id} not found`
        });
      }
      return res.status(200).send(bundle);
    } else {
      return res.status(500).send(result.error);
    }
  });
}

export = {
  loadBundle: (bundle, res: Response) => {
    return loadBundleNeo4j(bundle, res);
  },
  deleteAll: (req: Request, res: Response) => {
    return deleteAllNodes(req, res);
  },
  buildBundle: (_id: string, _filter: string, res: Response) => {
    if (_filter && Object.keys(_filter).length > 0) {
      return getBundleWithFilter(_id, _filter, res);
    } else {
      return getBundle(_id, res);
    }
  },
  verifyConnection: () => {
    return verifyConnection();
  }
};
