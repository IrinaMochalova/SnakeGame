package server;

import model.Game;
import proto.Interfaces.*;
import proto.Interfaces.IClientListener;
import server.Interfaces.IGameConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
    private Game game;
    private HashMap<IClient, RemotePlayer> clients;

    public Server(
            IClientListener listener,
            int clientsCount,
            int roundTime,
            IGameConstructor constructor) {
        clients = new HashMap<>();

        for (IClient client : acceptClients(listener, clientsCount))
            clients.put(client, new RemotePlayer());

        game = constructor.construct(clients.values());
        play(roundTime);
    }

    private HashSet<IClient> acceptClients(IClientListener listener, int clientsCount) {
        HashSet<IClient> clients = new HashSet<>();
        while (clients.size() < clientsCount) {
            if (listener.hasClient())
                clients.add(listener.accept());
            try {
                Thread.sleep(10);
            } catch (Exception ignored) {}
        }
        listener.close();
        return clients;
    }

    private void play(int roundTime) {
        while (game.getField().getSnakes().size() > 0) {
            game.tick();
            updateDirections(roundTime);
            broadcast(game.getField());
        }
    }

    private void updateDirections(int roundTime) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < roundTime) {
            for (IClient client : clients.keySet()) {
                if (client.hasMessage())
                    clients.get(client).setDirection(client.receive());
            }
        }
    }

    private <T extends Serializable> void broadcast(T object) {
        for (IClient client : clients.keySet())
            client.send(object);
    }
}
