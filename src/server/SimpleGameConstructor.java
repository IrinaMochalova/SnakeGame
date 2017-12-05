package server;

import model.*;
import model.FieldObjects.Apple;
import model.FieldObjects.Wall;
import model.Interfaces.IField;
import model.Interfaces.IGenerator;
import model.Interfaces.IPlayer;
import proto.Settings;
import server.Interfaces.IGameConstructor;

import java.util.Collection;
import java.util.HashSet;

public class SimpleGameConstructor implements IGameConstructor {
    private final int PLAYER_RATIO = 5;

    public Game construct(int playersCount) {
        int size = Settings.EMPTY_FIELD_SIZE + PLAYER_RATIO * playersCount;
        Field field = makeSquareField(size);
        return new Game(field, makeGenerators(field));
    }

    public <T extends IPlayer> void placePlayers(IField field, Collection<T> players) {
        int count = players.size();
        int size = field.getWidth();
        int diff = size / count;
        int half = diff / 2;
        Vector offset = new Vector(half, half);
        Vector location = offset.clone();
        int index = 0;
        for (IPlayer player : players) {
            Vector direction = index > size / 2 ? Direction.UP : Direction.DOWN;
            field.addSnake(new SnakeController(field, location, direction, player));
            location = location.add(offset);
        }
    }

    private Field makeSquareField(int size) {
        Field field = new Field(size, size);
        for (int i = 0; i < size; i++) {
            field.addObject(new Wall(new Vector(i, 0)));
            field.addObject(new Wall(new Vector(i, size - 1)));
            if (i > 0 && i < size - 1) {
                field.addObject(new Wall(new Vector(0, i)));
                field.addObject(new Wall(new Vector(size - 1, i)));
            }
        }

        return field;
    }

    private HashSet<IGenerator> makeGenerators(Field field) {
        HashSet<IGenerator> generators =  new HashSet<>();
        generators.add(new Generator<>(Apple.class, field));
        return generators;
    }
}
