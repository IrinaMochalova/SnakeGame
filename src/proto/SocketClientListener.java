package proto;

import proto.Interfaces.IClientListener;

import java.net.ServerSocket;
import java.util.ArrayDeque;

public class SocketClientListener extends Thread implements IClientListener<SocketClient> {
    private ServerSocket server;
    private ArrayDeque<SocketClient> clients;

    public SocketClientListener(ServerSocket server) {
        clients = new ArrayDeque<>();
        this.server = server;

        start();
    }

    public boolean hasClient() {
        return clients.size() > 0;
    }

    public SocketClient accept() {
        if (!hasClient())
            return null;
        return clients.removeFirst();
    }

    @Override
    public void run() {
        while (true) {
            try {
                clients.addLast(new SocketClient(server.accept(), Settings.MESSAGE_SIZE));
            }
            catch (Exception ignored) { }
        }
    }
}
