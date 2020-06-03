![CyFHIR](imgs/cyfhir.png)

### Operationalizing Neo4j for use within Healthcare by leveraging HL7 FHIR Standards

#### Table of Contents

1.  [Introduction](#introduction)
2.  [Using CyFHIR](#usage)
3.  [Using This Codebase](#repo)
4.  [Performance Metrics](#performance)

* * *

<a name="introduction"></a>

# 1. Introduction

### Background

Before explaining CyFHIR, you should first understand what **[Neo4j](https://neo4j.com/developer/graph-database/#neo4j-overview)** and **[FHIR](https://www.hl7.org/fhir/overview.html#Background)** are, as this project is directly built off of them. To summarize, Neo4j is an 'open-source, NoSQL, native graph database', and FHIR is an open standard in healthcare maintained by HL7 who publishes interoperable methods of data transfer, storage, classification, and more. We highly recommend at least reading through HL7's FHIR documentation before continuing to work with CyFHIR.

### What is CyFHIR?

CyFHIR is a native Neo4j plugin that acts as the bridge between FHIR and Neo4j. With CyFHIR, users can: directly load FHIR Resources into their Neo4j database, execute Neo4j queries (using Neo4j's query language, **Cypher**) on those FHIR Resources, and return query responses as FHIR Resources that meet HL7's standards.

<a name="usage"></a>

# 2. Using CyFHIR

### Usage Prerequisites

-   Docker Desktop (docker and docker-compose)

### How to use CyFHIR

We recommend the use of Docker for running Neo4j locally to maintain static development environments that can be easily cleaned/removed. But if you wish to use Neo4j Desktop, that should work as well. Whichever development you choose, adding CyFHIR to Neo4j should be the same process:

1.  Download the latest release from <https://github.com/Optum/CyFHIR/releases>
2.  Add CyFHIR to your `$NEO4J_HOME/plugins` folder
3.  Download the latest APOC Package from <https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases> (APOC is a Neo4j community package with tons of useful procedures and functions and we use it in tandem with CyFHIR)
4.  Add APOC to your `$NEO4J_HOME/plugins` folder
5.  In `$NEO4J_HOME/conf/neo4j.conf` add apoc and cyfhir as unrestricted plugins `NEO4J_dbms_security_procedu res_unrestricted: apoc.*,cyfhir.*`
6.  Run Neo4j

### CyFHIR Commands

Currently CyFHIR has 1 procedure and 1 aggregating function:

##### Procedures:

-   `cyfhir.loadBundle()`
    -   To load FHIR into Neo4J, you can easily do this by running `CALL cyfhir.loadBundle()` with the input being a FHIR Bundle JSON that has been formatted as a string (adding escape chars to all double quotes in the JSON).
    -   Another thing to note is if you want to test this way with generated data, we recommend [Synthea](https://github.com/synthetichealth/synthea). BUT if you choose to use Synthea, you must remove the generated html in every resource of the bundle for all entries. The path to the field to remove is: `Bundle.entry[i].resource.text.display`. This is necessary as there are escape chars hidden within the display that Neo4j cannot handle.

##### Functions

-   `cyfhir.buildBundle()`
    -   Pass an array of expanded, structured FHIR Resources that were the result of your query. To properly pass data into this function, the last few lines of your query will probably end up looking like this:

```js
UNWIND entryList AS entry
CALL apoc.path.expand(entry, ">|relationship", "-entry", 0, 999) YIELD path
WITH collect(path) AS paths
RETURN cyfhir.buildBundle(paths)
```

 The entryList variable above that gets unwound is the list of entry nodes that match a query that you've written above. This expands those entry nodes to get the full resource, converts that to a JSON/tree-like structure, then passes it to CyFHIR to build the bundle and enforce correct cardinality of resource properties.

<a name="repo"></a>

# 3. Using this Codebase

Understanding the Cypher and code behind this solution is not trivial, so we recommend using this repo to get a better understanding of how this all works.

In this repo we have:

-   The CyFHIR Java Project
-   An Express.js HTTP Server that also serves Swagger Docs for interacting with Neo4j, used for demonstrating examples of CyFHIR in action
    -   Check out the CyFHIR Controller in [express/src/controllers/cypherController.js](./express/src/controllers/cypherController.js) for some Cypher examples using CyFHIR, we are going to cook up a recipe book in the future
-   Docker-Compose files for spinning up Neo4j, Node.js, and a small ELK Stack with APM for performance monitoring if you are interested in that

Just be sure to `git clone` this repo and `cd CyFHIR` into the project

### Starting Neo4j with CyFHIR in Docker and local Express.js server

1.  In one terminal window run command `make neo4j`
2.  In a different terminal window run command `cd express && npm ci`
3.  Then run `npm run serve` to start the Express.js server
4.  Open browser to <http://localhost:7474> and <http://localhost:3000/docs>
5.  Use Swagger Docs to Load Test Bundle or run `bash bin/seed.sh` from root of project to seed Neo4j with 6, large Synthea Bundles

### Starting Neo4j with CyFHIR and Express.js server in Docker

1.  In terminal run command `make backend`
2.  Open browser to <http://localhost:7474> and <http://localhost:3000/docs>
3.  Use Swagger Docs to Load Test Bundle or run `bash bin/seed.sh` from root of project to seed Neo4j with 6, large Synthea Bundles

### Starting Neo4j with CyFHIR, Express.js server, and ELK Stack in Docker

1.  In terminal run command `make stack`
2.  Open browser to <http://localhost:7474>, <http://localhost:3000/docs>, and <http://localhost:5601>
3.  Use Swagger Docs to Load Test Bundle or run `bash bin/seed.sh` from root of project to seed Neo4j with 6, large Synthea Bundles
4.  Use APM Panels in Kibana to view performance metrics from the express server, more information below

<a name="performance"></a>

# 4. Performance Metrics

The performance of the Express Server is monitored through Elastic APM and visualized in Kibana. Connecting the service to the Elastic APM server is done through an APM agent in the `router.js` as shown below:

    var apm = require('elastic-apm-node').start({
      // Override service name from package.json
      serviceName: 'cyfhir_plugin',
      serverUrl: 'http://apm-server:8200',
    })

### Viewing Performance Metrics with APM

The APM agent automates sending transaction data from the Node application to the APM server, which is then consumed by Kibana. To view the metrics in Kibana, navigate to <http://localhost:5601/app/apm#/services> to see the list of running services. Go to **cyfhir_plugin**, and under the "Transactions" tab, you can view different performance metrics as shown below:
![](./imgs/sample_metrics.png)

### Visual Graph of Patient and Conditions

![](./imgs/patient_condition.png)
