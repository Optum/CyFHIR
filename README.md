![CyFHIR](imgs/cyfhir.png)
#

Operationalizing Neo4j for use within healthcare by providing a platform that supports data interoperability.
The main focus of this project is to supply developers with a Native Java Plugin for Neo4j that can load FHIR resources directly into Neo4j
and that can convert Neo4j query responses into FHIR Resources. This is being built in parallel with a standardized FHIR Server that can utilize Neo4j instead
of the more popular databases built into other FHIR Servers. We are also providing a standard way for logging, streaming, performance testing, and deployment to be done
with Neo4j.

## Prerequisites
* Docker and Docker Compose
* Node.js (>12.0.0) and npm (>6.0.0)
* Maven and Java 11

## Basic Starting Instructions

1.  In terminal run command `make stack`
2.  Open browser to <http://localhost:7474> and <http://localhost:3000/docs>
3.  Use Swagger Docs to Load Test Bundle or run `bash bin/seed.sh` to seed Neo4j with many Synthea Bundles
4.  To query Patient and Condition resources (Can be any FHIR Resource if it is in the DB) in a visual graph run in Neo4j browser:
```js
WITH ["Patient", "Condition"] AS resources
MATCH (m:entry)-[*1]->(n:resource)
WHERE (n.resourceType in resources)
WITH collect(m) AS entryList
UNWIND entryList AS entry
CALL apoc.path.expand(entry, ">|relationship", "-entry", 0, 999) YIELD path
WITH collect(path) AS paths
RETURN paths
```

## Developing the Express.js Server

1.  In one terminal window run command `make neo4j`
2.  In a different terminal window run command `cd express && npm run start`
3.  Open browser to <http://localhost:7474> and <http://localhost:3000/docs>
4.  Use Swagger Docs to Load Test Bundle or run `bash bin/seed.sh` to seed Neo4j with many Synthea Bundles

## Viewing Performance Metrics

The performance of the Express Server is being monitored through Elastic APM and visualized in Kibana. Connecting the service to the Elastic APM server is done through am APM agent in the `router.js` as below:
```
var apm = require('elastic-apm-node').start({
  // Override service name from package.json
  serviceName: 'cyfhir_plugin',
  serverUrl: 'http://apm-server:8200',
})
```

The APM agent automates sending transaction data from the Node application to the APM server which is then consumed by Kibana. To view the metrics in Kibana navigate to <http://localhost:5601/app/apm#/services> to see the list of running services. Go to **cyfhir_plugin** and there under the "Transactions" tab you can view different performance metrics as shown below.
![](./imgs/sample_metrics.png)

### Visual Graph of Patient and Conditions
![](./imgs/patient_condition.png)
