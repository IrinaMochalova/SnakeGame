package proto;

import proto.Interfaces.IClientAcceptor;

import java.net.ServerSocket;
import java.util.ArrayDeque;

public class SocketClientAcceptor extends Thread implements IClientAcceptor<SocketClient> {
    private ServerSocket server;
    private ArrayDeque<SocketClient> clients;

    public SocketClientAcceptor(ServerSocket server) {
        clients = new ArrayDeque<>();
        this.server = server;
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
        try {
            clients.addLast(new SocketClient(server.accept(), Settings.MESSAGE_SIZE));
        } catch (Exception ignored) {}
    }
}
