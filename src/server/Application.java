package server;

import proto.Interfaces.IClientListener;
import proto.Settings;
import proto.SocketClientListener;

import java.net.ServerSocket;

public final class Application {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(Settings.SERVER_PORT);
            IClientListener listener = new SocketClientListener(server);
            new Server(listener, Settings.CLIENTS_COUNT, Settings.ROUND_TIME);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
