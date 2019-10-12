import { createSocket } from 'dgram';

const clientSocket = createSocket('udp4');
const bufferData = Buffer.from("Hello World");
clientSocket.send(bufferData, 6969, 'localhost', err => {
    if (err) {
        console.error("CLIENT: Error: ", err.stack);
    }
});


clientSocket.on('message', (msg, rinfo) => {
    console.log(`CLIENT: Message Obtained from ${rinfo.address}:${rinfo.port}: "${msg}"`);


    clientSocket.close();
});
