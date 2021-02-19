const input_pipeline = 'articles:tagged';
const output_pipeline = 'articles:analyzed';

const client = require('../sdk');
client.connect('127.0.0.1', 2227, () => {
    client.subscribe(input_pipeline, data => {
        console.log(input_pipeline, data.data);
        data.data.score = Math.random() * 100;
        console.log(data);
        client.push(output_pipeline, data.data);
    })
})