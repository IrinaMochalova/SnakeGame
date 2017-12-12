package proto;

import proto.Interfaces.IClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.ByteBuffer;
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
        sent.push(object);
    }

    public <TObject extends Serializable> TObject receive() {
        return (TObject)received.poll();
    }

    public boolean hasMessage() {
        return !received.isEmpty();
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

        while (client.isConnected()) {
            processInput(input);
            processOutput(output);
            try {
                sleep(Settings.TIME_QUANTUM);
            } catch (Exception ignored) {}
        }
    }

    private void processInput(BufferedInputStream input) {
        try {
            if (input.available() >= 4) { // 4 = sizeof(int)
                byte[] bytes = new byte[4];
                input.read(bytes);
                int length = convertBytesToInt(bytes);
                bytes = new byte[length];
                int index = 0;
                while (index < bytes.length)
                    bytes[index++] = (byte)input.read();
                received.push(Packer.unpack(bytes));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processOutput(BufferedOutputStream output) {
        try {
            if (!sent.isEmpty()) {
                Object object = sent.poll();
                byte[] packed = Packer.pack((Serializable)object);
                output.write(convertIntToBytes(packed.length));
                output.write(packed);
                output.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] convertIntToBytes(int number){
        return ByteBuffer.allocate(4).putInt(number).array();
    }

    private int convertBytesToInt(byte[] bytes){
        return ByteBuffer.wrap(bytes).getInt();
    }
}
