import { resolve } from 'path';
import { Document, Packer, Paragraph } from 'docx';
import * as filestream from 'fs';

process.env.GOOGLE_APPLICATION_CREDENTIALS = resolve(__dirname, "../../", "serviceaccount.json") //"/Users/brehm/MHacks2019/serviceaccount.json";

const vision = require('@google-cloud/vision');

// Creates a client
const client = new vision.ImageAnnotatorClient();

//Finds text in an image
async function callGoogleVision(fileName: string) {

    const [result] = await client.documentTextDetection(fileName);
    const fullTextAnnotation = result.fullTextAnnotation;
    return fullTextAnnotation.text;
}
<<<<<<< HEAD

//Generates a .docx document from a list of images. 
async function generateDocument(images: string[]){

    const doc = new Document();

    //Loops through each image, adding it to the document
    for (let image of images){
=======
async function generateDocument(images: string[]) {
    const doc = new Document();

    for (let image of images) {
>>>>>>> 1b2619c3d6182c5ee136b134b8984a26089e8514
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
        filestream.writeFileSync("../../DocumentOutput/Document.docx", buffer);
    });
}

const imageNameArray = [
    "Pg1.jpg",
    "Pg2.jpg",
    "Pg3.jpg"
];

const testImageArray = [];

for (let image of imageNameArray){
    testImageArray.push(resolve(__dirname, "../../ImageInput/",image));
}

generateDocument(testImageArray);