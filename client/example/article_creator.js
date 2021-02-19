const input_pipeline = 'articles:analyzed';
const output_pipeline = 'articles:raw';

const client = require('../sdk');
client.connect('127.0.0.1', 2227, () => {
    client.subscribe(input_pipeline, data => {
        console.log(input_pipeline, data.data);
    })
    client.push(output_pipeline, {
        name: 'test test test'
    });
})
