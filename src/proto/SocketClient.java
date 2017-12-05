package proto;

import proto.Interfaces.IClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayDeque;

public class SocketClient extends Thread implements IClient {
    private Socket client;
    private ArrayDeque<Object> sent;
    private ArrayDeque<Object> received;

    public SocketClient(Socket client) {
        super();
        this.client = client;
        received = new ArrayDeque<>();
        sent = new ArrayDeque<>();

        start();
    }

    public <TObject extends Serializable> void send(TObject object) {
        sent.addLast(object);
    }

    public <TObject extends Serializable> TObject receive() {
        return (TObject)received.removeFirst();
    }

    public boolean hasMessage() {
        return received.size() > 0;
    }

    @Override
    public int hashCode() {
        byte[] bytes = client.getInetAddress().getAddress();
        int xor = 0;
        for (byte b : bytes)
            xor ^= b;
        return xor ^ client.getPort();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SocketClient
                && ((SocketClient)obj).client.getInetAddress().equals(client.getInetAddress())
                && ((SocketClient)obj).client.getPort() == client.getPort();
    }

    @Override
    public void run() {
        BufferedInputStream input;
        BufferedOutputStream output;
        try {
            input = new BufferedInputStream(client.getInputStream());
            output = new BufferedOutputStream(client.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        while (!client.isClosed()) {
            processInput(input);
            processOutput(output);
            try {
                sleep(50);
            } catch (Exception ignored) {}
        }
    }

    private void processInput(BufferedInputStream input) {
        try {
            if (input.available() > 0) {
                byte[] bytes = new byte[input.available()];
                input.read(bytes);
                received.addLast(Packer.unpack(bytes));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processOutput(BufferedOutputStream output) {
        try {
            if (sent.size() > 0) {
                Object object = sent.removeFirst();
                output.write(Packer.pack((Serializable)object));
                output.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
