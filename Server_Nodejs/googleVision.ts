import { resolve } from 'path';

process.env.GOOGLE_APPLICATION_CREDENTIALS = "/Users/brehm/MHacks2019/serviceaccount.json";

const picname = "testrabbit.jpg";
const testfilepath = resolve(__dirname, "../../", picname);

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

    return fullTextAnnotation.text;
}

callGoogleVision(testfilepath).then(res => {
    console.log(res);
});