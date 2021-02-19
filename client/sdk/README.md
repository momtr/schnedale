# Schnedale-SDK
official SDK for the schnedale-broke

## Installation
Via NPM:
```
npm install schnedale
```
Import it:
```javascript
const client = require('schnedale');
```

## Pipeline
- Clients can subscribe and push data to pipelines.
- Pipeline naming convention: `pipeline:tag` - the tag represents the stage of the pipeline. For pipelines having only one stage, `:raw` can be used. E.g. an article pipeline has different stages: `created, taged, analyzed, scored`, which is why you might use the pipeline names `articles:created, articles:tagged, articles:analyzed, articles:scored`. The pipeline prefix is `articles` in this case.

## Usage

### Create connection
Create a connection to the schnedale-broker by defining the broker's IP address and port number. Once a connection is established, the callbnack is triggered.

- __ip__: IPv4 address of schnedale-broker
- __port__: usually `2227`
- __callback__: triggered when connection is established

```javascript
const IP = '127.0.0.1';
const PORT = 2227; // default port
client.connect(IP, PORT, () => {
    console.log('connected to schnedale-broker');
})
```

### Subscribe to pipeline
Subscribe to a pipeline by defining its name and the callback function receiving data published to pipeline.

- __pipeline__: the pipeline's name (usually pipeline:tag, e.g. articles:raw)
- __callback__: function receiving data which is published to pipeline

```javascript
// once client is connected
const pipeline = 'articles:transformed';
client.subscribe(pipeline, (data) => {
    console.log(data);
})
```

### Push to pipeline
Push data to a pipeline. 

> Note: Published messages are acknowledged by the server. However, there is no SDK support for that feature.

- __pipeline__: the pipeline's name (usually pipeline:tag, e.g. articles:raw)
- __data__: object, string or number 

```javascript
// once client is connected
const pipeline = 'articles:raw';
const payload = {
    hello: 'world'
}
client.publish(pipeline, payload)
```

### Transform data
Transformers read data from one pipeline, do something with it and publish the transformed data to another pipeline.

> Note: The pipeline's prefix should be the same, however, the tags should be different

- __ingressPipeline__: name of the pipeline where the data is coming in
- __egressPipeline___ name of the pipeline where the transformed data should be published to
- __transformerFunction__: function receiving data from the ingressPipeline and returning the transformed data

```javascript
// once client is connected
// articles:raw -> transformation -> articles:tagged
const ingressPipeline = 'articles:raw';
const egressPipeline = 'articles:tagged';
client.transform(ingressPipeline, egressPipeline, (data) => {
    data.tags = ['tag1', 'tag2'];
    return data;
})
```