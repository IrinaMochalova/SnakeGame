package server;

import model.Interfaces.IPlayer;
import model.Vector;
import proto.Interfaces.IClient;

public class RemotePlayer implements IPlayer {
    private IClient client;

    public RemotePlayer(IClient client) {
        this.client = client;
    }

    public Vector getDirection(Vector head, Vector direction) {
        if (client.hasMessage()) {
            direction = null; // message
        }
        return direction;
    }
}
