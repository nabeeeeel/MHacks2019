import java.io.IOException;
import java.net.*;

class Hello {
    public static void main(String args[]) {
        System.out.println("Hello World");

        Server server = new Server();
        Thread server_t = new Thread(server);
        server_t.start();

        try {
            server_t.join(2000);
            new Client();
            server_t.join(2000);

            System.out.println("Shutting Server Down...");
            server.shutdown();

        } catch (InterruptedException e) {
            System.out.println("Interrupted!! " + e.getMessage());
        }

    }
}

class Client extends Thread {
    private DatagramSocket socket;
    private InetAddress address;

    public Client() {
        byte[] buff;
        String msg = "Hello World";

        try {
            // Setup Socket & Address
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");

            // Pack the Message and Send
            buff = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buff, buff.length, address, 6969);
            socket.send(packet);

            // Wait for Response
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Client: Received Message from Server [" + received + "]");
        } catch (Exception e) {
            System.out.println("Client: Exception, " + e.getMessage());
        }

        socket.close();
    }
}

class Server extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private boolean running;

    public Server() {
        try {
            socket = new DatagramSocket(6969);
            address = InetAddress.getByName("localhost");
        } catch (Exception e) {
            System.out.print("Server Failed to created Data, ");
            System.err.println(e.getMessage());
        }
    }

    public void run() {
        running = true;
        byte[] buffer = new byte[256];

        while (running) {
            try {
                // Listen for packet to be recieved from Client
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Obtain Sender Information
                address = packet.getAddress();
                int port = packet.getPort();

                // Construct Received Data
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Server: Recieved Packet: " + received);

                // Do stuff with Data Received TODO:

                // Respond to Client
                buffer = "Hi from Server".getBytes();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);

            } catch (IOException e) {
                System.out.print("Server: IO Exception, ");
                System.err.println(e.getMessage());
            }
        }
    }

    public void shutdown() {
        running = false;
        socket.close();
    }
}