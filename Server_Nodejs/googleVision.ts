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
    /*
    console.log(`Full text: ${fullTextAnnotation.text}`);
    fullTextAnnotation.pages.forEach(page => {
        page.blocks.forEach(block => {
            console.log(`Block confidence: ${block.confidence}`);
            block.paragraphs.forEach(paragraph => {
                console.log(`Paragraph confidence: ${paragraph.confidence}`);
                paragraph.words.forEach(word => {
                    const wordText = word.symbols.map(s => s.text).join('');
                    console.log(`Word text: ${wordText}`);
                    console.log(`Word confidence: ${word.confidence}`);
                    word.symbols.forEach(symbol => {
                        console.log(`Symbol text: ${symbol.text}`);
                        console.log(`Symbol confidence: ${symbol.confidence}`);
                    });
                });
            });
        });
    });*/

    //const doc = new Document
    /*
    fullTextAnnotation.pages.forEach(page => {
        page.blocks.forEach(block => {
            block.paragraphs.forEach(paragraph => {
                paragraph.words.forEach(word => {
                    word.symbols.forEach(symbol => {
                        console.log(symbol.text);
                    });
                });
            });
        });
    });
    */

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
        filestream.writeFileSync("../../DocumentOutput/Document.docx", buffer);
    });
}


const testImageArray = [
    resolve(__dirname, "../../ImageInput/", "Rafeha_Letter.png"),
    resolve(__dirname, "../../ImageInput/", "testrabbit.jpg")
];

generateDocument(testImageArray);