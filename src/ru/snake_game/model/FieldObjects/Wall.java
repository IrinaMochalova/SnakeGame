package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.util.Vector;
import ru.snake_game.model.Interfaces.ISnake;

public class Wall extends FieldObject {
    public Wall(Vector location) {
        super(location);
    }

    @Override
    public void interact(ISnake snake) {
        snake.kill();
    }
}
