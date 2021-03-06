package server;

import model.Interfaces.IPlayer;
import model.Vector;

public class RemotePlayer implements IPlayer {
    private Vector direction;

    public RemotePlayer() {
        direction = null;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Vector getDirection(Vector head, Vector direction) {
        if (this.direction != null)
            direction = this.direction;
        return direction;
    }
}
