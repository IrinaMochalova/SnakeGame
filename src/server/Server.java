package server;

import model.Game;
import model.Vector;
import proto.Commands;
import proto.Interfaces.*;
import proto.Interfaces.IClientListener;
import proto.Settings;
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

        game = constructor.construct(clients.size());
        constructor.placePlayers(game.getField(), clients.values());
        play(roundTime);
    }

    private HashSet<IClient> acceptClients(IClientListener listener, int clientsCount) {
        HashSet<IClient> clients = new HashSet<>();
        while (clients.size() < clientsCount) {
            if (listener.hasClient())
                clients.add(listener.accept());
            try {
                Thread.sleep(Settings.TIME_QUANTUM);
            } catch (Exception ignored) {}
        }
        listener.close();
        return clients;
    }

    private void play(int roundTime) {
        broadcast(Commands.GAME_START);
        while (game.getField().getSnakes().size() > 0) {
            game.tick();
            waitMessages(roundTime);
            broadcast(game.getField());
        }
        broadcast(Commands.GAME_END);
    }

    private void waitMessages(int roundTime) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < roundTime) {
            for (IClient client : clients.keySet()) {
                if (client.hasMessage())
                    processMessage(client);
            }
        }
    }

    private void processMessage(IClient client) {
        Object message = client.receive();
        if (message instanceof Vector)
            clients.get(client).setDirection((Vector)message);
    }

    private <T extends Serializable> void broadcast(T object) {
        for (IClient client : clients.keySet())
            client.send(object);
    }
}
