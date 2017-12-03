package server;

import proto.SocketClientAcceptor;

import java.net.ServerSocket;

public final class Application {
    public static void main(String[] args) {
        try {
            new Server(new SocketClientAcceptor(new ServerSocket(15151)), 2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
