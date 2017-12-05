package client;

import client.Interfaces.IFieldProvider;
import model.Game;
import model.Interfaces.IField;
import model.Interfaces.IPlayer;
import server.Interfaces.IGameConstructor;

import java.util.Collection;

public class LocalFieldProvider extends Thread implements IFieldProvider {
    private int roundTime;
    private Game game;

    public LocalFieldProvider(
            IGameConstructor constructor,
            Collection<IPlayer> players,
            int roundTime) {
        game = constructor.construct(players);
        this.roundTime = roundTime;

        start();
    }

    @Override
    public void run() {
        while (game.getField().getSnakes().size() > 0) {
            try {
                Thread.sleep(roundTime);
            } catch (Exception ignored) {
            }

            game.tick();
        }
    }

    public IField getField() {
        return game.getField();
    }
}
