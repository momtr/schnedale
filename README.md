# 🎈 schnedale
the lightweight message broker

<img src="https://github.com/spengerblog/schnedale/blob/master/schnedale-images/schnedale.svg" width="80%">

## Connection
- port: `2227`

## Protocol
The SCHNEDALE application-layer protocol is stateless and works on top of a TCP connection. It is designed to work with commands rather than heavyweight requests and responses.

Basic terms:
- __pipeline__: a channel/queue where data is transfered from source to destination
- __subscribe__: clients can subscribe to pipelines to receive all notifications published to this pipeline


### Client calls
- `SUBS <pipeline>:<pipeline_tag>` - subscribes to a pipeline (so that the client receives all notifications on that pipeline)
- `PUSH <pipeline>:<pipeline_tag> <base64_encoded_data> [<storage_flag>]` - push a message to a pipeline (if the pipeline does not exist, it will be created)
- `HEALTH` - do a health check (the server will respond with `ACK`)

### Server calls
- `ACK` - sent to the client when the server acknowledges a message
- `ERR <error_code> <error_message>[:<indicator>]` - if an error occurs, the server will send an error message which has an unique `error_code`
- `NOTI Message(source=<source-ip>:<source-port>,pipeline=<pipeline>,data=<base64_encoded_data>)` - the message that is received when data is published to a pipeline

__Message__:
- source: the unique identifier of the client who sent the message
- pipeline: the name of the message's pipeline
- data: base64 encoded data where '=' is replaced by '*'

## Client-SDK
- __Node.js__: NPM (https://www.npmjs.com/package/schnedale)

## Functionality

### Message broker
Schnedale can act as a message broker, distributing messages between individual services. 

### Raw & structured data store
Schnedale stores data:
- if the data_flag is set to `1` in the `PUSH` call
- if it is uploaded via the `DATA`call

Data is organized, so that it is accessible to analytics applications utilizing the Schnedale Sandbox. Machine learning models can be created in the Schnedale Sandbox and they have access to all stored data.

Data organization:
- in pipelines
- pipeline tags
- data format (TEXT, JSON, XML, CSV, XLSX, PHOTO, FILE)
