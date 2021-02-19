const input_pipeline = 'articles:raw';
const output_pipeline = 'articles:tagged';

const client = require('../sdk');
client.connect('127.0.0.1', 2227, () => {
    client.transform(input_pipeline, output_pipeline, data => {
        data.tags = ["test", "test"];
        return data;
    })
})