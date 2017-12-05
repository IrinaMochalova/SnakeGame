package server;

import proto.Interfaces.IClientListener;
import proto.Settings;
import proto.SocketClientListener;

import java.net.ServerSocket;

public final class ServerApplication {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(Settings.SERVER_PORT);
            IClientListener listener = new SocketClientListener(server);
            new Server(listener, Settings.CLIENTS_COUNT, Settings.ROUND_TIME, new SimpleGameConstructor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
}
