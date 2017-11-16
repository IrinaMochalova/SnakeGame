package ru.snake_game.model;

import ru.snake_game.model.FieldObjects.SnakeHead;
import ru.snake_game.model.FieldObjects.Wall;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.util.Vector;
import ru.snake_game.model.util.Direction;

public final class FieldGenerators {
    private FieldGenerators() {
    }

    public static IField genBoardedField(int height, int width) {
        if (height < 3 || width < 3)
            throw new IllegalArgumentException();

        IField field = new Field(height, width);
        for (int x = 0; x < width; x++) {
            field.addObject(new Wall(new Vector(x, 0)));
            field.addObject(new Wall(new Vector(x, height - 1)));
        }

        for (int y = 1; y < height - 1; y++) {
            field.addObject(new Wall(new Vector(0, y)));
            field.addObject(new Wall(new Vector(width - 1, y)));
        }

        SnakeHead snake = new SnakeHead(new Vector(4, 4), null, Direction.RIGHT, field);
        field.addObject(snake);

        return field;
    }
}
