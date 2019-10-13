/*const express = require('express');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));


app.post('/', (req, res) => {
    console.log(req);
    console.log(req.body);
    res.send("Welcome!")
});

app.listen(6969, () => {
    console.log("Listening to 'localhost:6969'");
})*/

import * as fs from 'fs';
import { resolve } from 'path';
import { Document, Packer, Paragraph } from 'docx';

process.env.GOOGLE_APPLICATION_CREDENTIALS = resolve(__dirname, "../../", "serviceaccount.json")

const express = require('express');
const myParser = require('body-parser');
const app = express();
const port = 6969;
const vision = require('@google-cloud/vision');
const client = new vision.ImageAnnotatorClient();

async function callGoogleVision(fileName: string) {

    const [result] = await client.documentTextDetection(fileName);
    const fullTextAnnotation = result.fullTextAnnotation;
    return fullTextAnnotation.text;
}
async function generateDocument(images: string[]) {
    const doc = new Document();

    for (let image of images) {
        await callGoogleVision(image).then(res => {
            console.log(res + "\n\n");

            const paragraph = new Paragraph(res);
            doc.addSection({
                properties: {},
                children: [paragraph]
            });
        });
    }

    Packer.toBuffer(doc).then((buffer) => {
        fs.writeFileSync("../../DocumentOutput/Document.docx", buffer);
    });
}

function decodeRequest(request){
    let nameArray = [];
    let count = 1

    for (let datum of request){
        let buff = new Buffer(datum, 'base64');
        fs.writeFileSync(`../../ImageInput/Image${count}.jpg`,buff );
        nameArray.push(`Image${count}.jpg`);
        count++;
    }

    return nameArray;
}

function encodeDocument(){
    let buff = fs.readFileSync("../../DocumentOutput/Document.docx");
    let base64data = buff.toString('base64');
    return base64data;
}

app.use(myParser.json({extended : true}));
   app.post("/convert", function(request, response) {
       console.log(request.body); //This prints the JSON document received (if it is a JSON document)
        
       //Decode the Request
        let nameArray = decodeRequest(request);

       //Convert the Files
        generateDocument(nameArray);

       //Encode the responce
       response.json(encodeDocument());

});
 
 //Start the server and make it listen for connections on port 8080
  app.listen(port);