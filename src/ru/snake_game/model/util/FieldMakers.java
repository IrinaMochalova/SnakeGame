package ru.snake_game.model.util;

import ru.snake_game.model.Field;
import ru.snake_game.model.FieldObjects.Wall;

public final class FieldMakers {
    public static Field makeBoardedField(int width, int height) {
        if (width < 3 || height< 3)
            throw new IllegalArgumentException("Width and height should be greater than 3.");

        Field field = new Field(width, height);
        for (int x = 0; x < width; x++) {
            field.addObject(new Wall(new Vector(x, 0)));
            field.addObject(new Wall(new Vector(x, height - 1)));
        }

        for (int y = 1; y < height - 1; y++) {
            field.addObject(new Wall(new Vector(0, y)));
            field.addObject(new Wall(new Vector(width - 1, y)));
        }

        return field;
    }
}
