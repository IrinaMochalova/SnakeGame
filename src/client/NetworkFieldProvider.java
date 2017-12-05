package client;

import client.Interfaces.IFieldProvider;
import model.Interfaces.IField;
import model.Interfaces.IPlayer;
import model.Vector;
import proto.Interfaces.IClient;

public class NetworkFieldProvider extends Thread implements IFieldProvider {
    private IClient client;
    private IPlayer player;
    private int roundTime;
    private IField field;

    public NetworkFieldProvider(IClient client, IPlayer player) throws Exception {
        this.client = client;
        this.player = player;

        waitMessage();
        roundTime = client.receive();

        start();
    }

    public IField getField() {
        return field;
    }

    @Override
    public void run() {
        waitMessage();
        while (true) {
            field = client.receive();
            Vector direction = player.getDirection(null, null);
            waitDelay(roundTime - 300);
            if (direction != null)
                client.send(direction);
            waitMessage();
        }
    }

    private void waitMessage() {
        while (!client.hasMessage())
            waitDelay(10);
    }

    private void waitDelay(int delay) {
        try {
            Thread.sleep(delay);
        }
        catch (Exception ignored) {}
    }
}
