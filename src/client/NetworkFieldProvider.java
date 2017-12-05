package client;

import client.Interfaces.IFieldProvider;
import model.Interfaces.IField;
import model.Interfaces.IPlayer;
import model.Vector;
import proto.Commands;
import proto.Interfaces.IClient;
import proto.Settings;

public class NetworkFieldProvider extends Thread implements IFieldProvider {
    private IClient client;
    private IPlayer player;
    private IField field;

    private boolean started;
    private boolean ended;

    public NetworkFieldProvider(IClient client, IPlayer player) throws Exception {
        this.client = client;
        this.player = player;

        started = false;
        ended = false;

        start();
    }

    public IField getField() {
        return field;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isEnded() {
        return ended;
    }

    @Override
    public void run() {
        Vector previous = null;
        while (!ended) {
            if (client.hasMessage())
                processMessage(client.receive());
            if (started) {
                Vector direction = player.getDirection(null, null);
                if (direction != null && !direction.equals(previous)) {
                    client.send(direction);
                    previous = direction;
                }
            }
            waitDelay(Settings.TIME_QUANTUM);
        }
    }

    private void processMessage(Object message) {
        if (message instanceof Commands) {
            if (message == Commands.GAME_START)
                started = true;
            else if (message == Commands.GAME_END)
                ended = true;
        } else if (message instanceof IField) {
            field = (IField)message;
        }
    }

    private void waitDelay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception ignored) {}
    }
}
