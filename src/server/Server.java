package server;

import model.*;
import model.FieldObjects.Apple;
import model.Interfaces.IField;
import model.Interfaces.IGame;
import model.Interfaces.IGenerator;
import proto.Combiners;
import proto.Interfaces.IClient;
import proto.Interfaces.IClientAcceptor;
import proto.Settings;

import java.util.HashSet;

public class Server {
    private HashSet<IClient> clients;

    public Server(IClientAcceptor acceptor, int clientsCount) throws Exception {
        clients = acceptClients(acceptor, clientsCount);
        IField field = makeField(clients);
        IGame game = new Game(field, makeGenerators(field));

        run(game);
    }

    private HashSet<IClient> acceptClients(IClientAcceptor acceptor, int clientsCount) {
        HashSet<IClient> clients = new HashSet<>();
        while (clients.size() < clientsCount) {
            if (acceptor.hasClient())
                clients.add(acceptor.accept());
            HashSet<IClient> disconnected = new HashSet<>();
            for (IClient client : clients) {
                if (!client.isConnected())
                    disconnected.add(client);
            }
            clients.removeAll(disconnected);
        }
        return clients;
    }

    private IField makeField(HashSet<IClient> clients) {
        int clientsCount = clients.size();
        int size = Settings.MIN_FIELD_SIZE + clientsCount;
        IField field = FieldMakers.makeSquaredField(size);
        Vector offset = new Vector(size / clientsCount, size / clientsCount);
        Vector location = new Vector(offset.getX() / 2, offset.getY() / 2);
        int index = 0;
        for (IClient client : clients) {
            Vector direction = index > size / 2 ? Direction.UP : Direction.DOWN;
            field.addSnake(new SnakeController(field, location, direction, new RemotePlayer(client)));
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

    private void run(IGame game) {
        while (game.getField().getSnakes().size() > 0) {
            game.tick();
            for (IClient client : clients)
                client.send(Combiners.combineField(game.getField()));
            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {}
        }
    }
}
