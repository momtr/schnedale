/**
 * @version 1.0
 * @author Moritz Mitterdorfer (SPG-BLOG)
 * @description SDK for the schnedale broker
 */

const net = require('net');

/**
 * Schnedale SDK class providing the layer between the schnedale-broker and the higher-level code
 */
class Schnedale {

    /**
     * init function
     */
    constructor() {
        this.client = new net.Socket();
        this.connected = false;
        this.pipelineCallbacks = {};
    }

    /**
     * connects the schnedale client to the schendale-broker
     * @param ip - the schnedale-broker's IP address
     * @param port - the port on which the schnedale-broker is running
     * @param callback - a callback function which is called, if the client is connected to the schnedale-broker
     */
    connect(ip, port, callback) {
        try {
            this.client.connect(port, ip, () => {
                this.connected = true;
                this.__registerFunctions();
                callback();
            })
        } catch(e) {
            throw new Error('unable to connect to schnedale-broker');
        }
    }

    /**
     * client subscribes to a specified pipeline
     * @param pipeline - string indicating the pipeline's name
     * @param callback - a callback function receiving a message object if data is published to the pipeline
     */
    subscribe(pipeline, callback) {
        if(!this.connected) this.__throwNotConnectedError();
        this.client.write(`SUBS ${pipeline}\n`);
        this.pipelineCallbacks[pipeline] = callback;
    }

    /**
     * pushes data to a pipeline - if the pipeline does not exist, it is created
     * @param pipeline - string indicating the pipeline's name
     * @param data - object or string that should be sent to the pipeline
     */
    push(pipeline, data) {
        if(!this.connected) this.__throwNotConnectedError();
        const transformedData = this.__transformToBase64(this.__transformData(data));
        this.client.write(`PUSH ${pipeline} ${transformedData}\n`);
    }

    /**
     * receives data on ingress pipeline, transforms it via a transformer function and outputs it to an egress pipeline
     * @param ingressPipeline - string indicating the pipeline's name (input/ingress pipeline)
     * @param egressPipeline - string indicating the pipeline's name (output/egress/pipeline)
     * @param transformerFunction - function taking data in and returning transformed data
     */
    transform(ingressPipeline, egressPipeline, transformerFunction) {
        if(!this.connected) this.__throwNotConnectedError();
        this.subscribe(ingressPipeline, data => {
            let newData = transformerFunction(data.data);
            this.push(egressPipeline, newData);
        })
    }

    /**
     * registers events (handling received data) on the TCP client
     * @private
     */
    __registerFunctions() {
        this.client.on('data', data => {
            data = data+'';
            if(data.startsWith('ERR')) {
                throw new Error('error: ' + data);
            }
            if(data.startsWith('NOTI')) {
                const message = this.__parseMessage(data);
                if(message.pipeline) {
                    if(this.pipelineCallbacks[message.pipeline]) {
                        message.data = this.__transformToJsonOrText(this.__transformToString(message.data));
                        this.pipelineCallbacks[message.pipeline](message);
                    } else {
                        throw new Error('no callback function registered for pipeline ' + message.pipeline);
                    }
                } else {
                    throw new Error('received message that has no pipeline');
                }
            }
        })
    }

    /**
     * receives a message string and creates the JSON representation of the message
     * @param message
     * @returns {{}}
     * @private
     */
    __parseMessage(message) {
        message = message.replace('NOTI Message(', '').replace(')', '');
        let arr = message.split(',');
        let ret = {};
        for(let i of arr) {
            const parts = i.split('=');
            ret[parts[0]] = parts[1];
        }
        return ret;
    }

    /**
     * transforms data of any kind into a string
     * @param data
     * @returns {string|*}
     * @private
     */
    __transformData(data) {
        if(typeof data === 'object') {
            return JSON.stringify(data);
        }
        return data+'';
    }

    /**
     * throws an error indicating that the client is not connected to the schnedale-broker
     * @private
     */
    __throwNotConnectedError() {
        throw new Error('client not connected to schnedale-broker');
    }

    /**
     * converts a string into a base64 string
     * @param dataString
     * @returns {string}
     * @private
     */
    __transformToBase64(dataString) {
        return Buffer.from(dataString).toString('base64');
    }

    /**
     * converts a base64 string into a string
     * @param base64String
     * @returns {string}
     * @private
     */
    __transformToString(base64String) {
        return Buffer.from(base64String, 'base64').toString('ascii');
    }

    /**
     * takes a string and returns an object if the string is a JSON string, otherwise it returns the string
     * @param string
     * @private
     */
    __transformToJsonOrText(string) {
        try {
            return JSON.parse(string);
        } catch(e) {
            return string;
        }
    }

}

module.exports = new Schnedale();