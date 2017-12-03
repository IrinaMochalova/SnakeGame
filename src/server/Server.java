package server;

import model.*;
import model.FieldObjects.*;
import model.Interfaces.*;
import proto.Interfaces.*;
import proto.Packer;
import proto.Settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
    private HashMap<IClient, RemotePlayer> clients;
    private IGame game;

    public Server(IClientListener listener, int clientsCount, int roundTime) throws Exception {
        clients = new HashMap<>();

        for (IClient client : acceptClients(listener, clientsCount))
            clients.put(client, new RemotePlayer());
        IField field = makeField(clients.values());
        game = new Game(field, makeGenerators(field));

        run(roundTime);
    }

    private HashSet<IClient> acceptClients(IClientListener listener, int clientsCount) {
        HashSet<IClient> clients = new HashSet<>();
        while (clients.size() < clientsCount) {
            if (listener.hasClient())
                clients.add(listener.accept());
            HashSet<IClient> disconnected = new HashSet<>();
            for (IClient client : clients) {
                if (!client.isConnected())
                    disconnected.add(client);
            }
            clients.removeAll(disconnected);
        }
        return clients;
    }

    private IField makeField(Collection<RemotePlayer> players) {
        int playersCount = players.size();
        int size = Settings.MIN_FIELD_SIZE + playersCount;
        IField field = FieldMakers.makeSquaredField(size);
        Vector offset = new Vector(size / playersCount, size / playersCount);
        Vector location = new Vector(offset.getX() / 2, offset.getY() / 2);
        int index = 0;
        for (IPlayer player : players) {
            Vector direction = index > size / 2 ? Direction.UP : Direction.DOWN;
            field.addSnake(new SnakeController(field, location, direction, player));
            location = location.add(offset);
            index++;
        }
        return field;
    }

    private HashSet<IGenerator> makeGenerators(IField field) {
        HashSet<IGenerator> generators =  new HashSet<>();
        generators.add(new Generator<>(Apple.class, field));
        return generators;
    }

    private void run(int roundTime) {
        broadcast(Packer.packInt(roundTime));
        while (game.getField().getSnakes().size() > 0) {
            broadcast(Packer.packField(game.getField()));
            sleep(roundTime);
            updateDirections(roundTime);
        }
    }

    private void updateDirections(int roundTime) {
        long startTime = System.currentTimeMillis();
        HashSet<IClient> changed = new HashSet<>();
        while (System.currentTimeMillis() - startTime < roundTime) {
            for (IClient client : clients.keySet()) {
                if (!changed.contains(client) && client.hasMessage()) {
                    clients.get(client).setDirection(Packer.unpackVector(client.receive()));
                    changed.add(client);
                }
            }
            if (changed.size() == clients.size())
                break;
        }
    }

    private void broadcast(char[] message) {
        clients.forEach((client, player) -> client.send(message));
    }

    private void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        }
        catch (Exception ignored) {}
    }
}
