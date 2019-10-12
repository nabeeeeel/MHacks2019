import { createSocket } from 'dgram';
import { AddressInfo } from 'net';

const server = createSocket('udp4');


server.on('error', err => {
    console.error("SERVER: Error, ", err.stack);
});

server.on('message', (msg, rinfo) => {
    console.log(`SERVER: got: ${msg} from ${rinfo.address}:${rinfo.port}`);

    // Do Stuff with Data

    // Respond back
    const msgBuffer = Buffer.from("Hi from Server!");
    server.send(msgBuffer, rinfo.port, rinfo.address, err => {
        if (err) console.error("SERVER: Error Occured ", err.stack);
    })
});

server.on('listening', () => {
    const address = server.address() as AddressInfo;
    console.log(`SERVER: listening ${address.address}:${address.port}`);
});

server.bind(6969);