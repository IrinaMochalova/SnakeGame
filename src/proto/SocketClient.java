package proto;

import proto.Interfaces.IClient;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayDeque;

public class SocketClient extends Thread implements IClient<Socket> {
    private Socket client;
    private int bufferSize;
    private ArrayDeque<char[]> sent;
    private ArrayDeque<char[]> received;

    public SocketClient(Socket client, int bufferSize) {
        super();
        this.client = client;
        this.bufferSize = bufferSize;
        received = new ArrayDeque<>();
        sent = new ArrayDeque<>();

        start();
    }

    public void send(char[] message) {
        sent.addLast(message);
    }

    public char[] receive() {
        if (received.size() == 0)
            return null;
        return received.removeFirst();
    }

    public boolean hasMessage() {
        return received.size() > 0;
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public Socket getInterface() {
        return client;
    }

    @Override
    public void run() {
        try {
            InputStreamReader input = new InputStreamReader(client.getInputStream());
            OutputStreamWriter output = new OutputStreamWriter(client.getOutputStream());
            while (client.isConnected()) {
                if (input.ready()) {
                    char[] buffer = new char[bufferSize];
                    int length = input.read(buffer);
                    received.addLast(stripMessage(buffer, length));
                }
                if (sent.size() > 0) {
                    output.write(sent.removeFirst());
                    output.flush();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private char[] stripMessage(char[] message, int count) {
        char[] stripped = new char[count];
        System.arraycopy(message, 0, stripped, 0, count);
        return stripped;
    }
}
