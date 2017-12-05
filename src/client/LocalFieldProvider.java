package client;

import client.Interfaces.IFieldProvider;
import model.Game;
import model.Interfaces.IField;

public class LocalFieldProvider extends Thread implements IFieldProvider {
    private int roundTime;
    private Game game;

    public LocalFieldProvider(Game game, int roundTime) {
        this.game = game;
        this.roundTime = roundTime;

        start();
    }

    public IField getField() {
        return game.getField();
    }

    public boolean isStarted() {
        return true;
    }

    public boolean isEnded() {
        return game.getField().getSnakes().size() == 0;
    }

    @Override
    public void run() {
        while (!isEnded()) {
            try {
                sleep(roundTime);
            } catch (Exception ignored) {}

            game.tick();
        }
    }
}
