import neo4j from 'neo4j-driver'
import cypher from './cypherController'

function transactionInformation() {
    const uri = process.env.NEO4J_URI;
    const driver = neo4j.driver(uri);
    const session = driver.session();
    return {
        driver,
        session
    };
}

function startTransaction(cypher, res) {
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

function loadBundleNeo4j(_bundle, res) {
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

function deleteAllNodes(req, res) {
    startTransaction(cypher.deleteAll(), (result) => {
        if (result.result) {
            return res.status(200).send('All nodes deleted');
        } else {
            return res.status(500).send(result.error);
        }
    });
}

function getBundle(_id, res) {
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

function getBundleWithFilter(_id, _filter, res) {
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
    loadBundle: (bundle, res) => {
        return loadBundleNeo4j(bundle, res);
    },
    deleteAll: (req, res) => {
        return deleteAllNodes(req, res);
    },
    buildBundle: (_id, _filter, res) => {
        if (_filter && Object.keys(_filter).length > 0) {
            return getBundleWithFilter(_id, _filter, res);
        } else {
            return getBundle(_id, res);
        }
    }
};
