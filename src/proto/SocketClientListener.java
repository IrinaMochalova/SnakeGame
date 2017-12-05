package proto;

import proto.Interfaces.IClientListener;

import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;

public class SocketClientListener extends Thread implements IClientListener<SocketClient> {
    private boolean accepting;
    private ServerSocket server;
    private ArrayDeque<SocketClient> clients;

    public SocketClientListener(ServerSocket server) {
        clients = new ArrayDeque<>();
        this.server = server;
        accepting = true;

        start();
    }

    public boolean hasClient() {
        return clients.size() > 0;
    }

    public SocketClient accept() {
        return clients.removeFirst();
    }

    @Override
    public void run() {
        try {
            server.setSoTimeout(Settings.TIME_QUANTUM);
        } catch (Exception ignored) {}

        while (accepting) {
            try {
                clients.addLast(new SocketClient(server.accept()));
            }
            catch (SocketTimeoutException ignored) {}
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        accepting = false;
    }
}
