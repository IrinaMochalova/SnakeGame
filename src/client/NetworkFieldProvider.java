package client;

import client.Interfaces.IFieldProvider;
import model.Interfaces.IField;
import model.Interfaces.IPlayer;
import model.Vector;
import proto.Interfaces.IClient;

public class NetworkFieldProvider extends Thread implements IFieldProvider {
    private IClient client;
    private IPlayer player;
    private IField field;

    public NetworkFieldProvider(IClient client, IPlayer player) throws Exception {
        this.client = client;
        this.player = player;

        start();
    }

    public IField getField() {
        return field;
    }

    @Override
    public void run() {
        Vector previous = null;
        while (true) {
            if (client.hasMessage())
                field = client.receive();
            Vector direction = player.getDirection(null, null);
            if (direction != null && !direction.equals(previous)) {
                client.send(direction);
                previous = direction;
            }
            waitDelay(10);
        }
    }

    private void waitDelay(int delay) {
        try {
            Thread.sleep(delay);
        }
        catch (Exception ignored) {}
    }
}
