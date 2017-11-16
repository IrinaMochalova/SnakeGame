package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.Interfaces.ISnakeHead;
import ru.snake_game.model.util.Vector;

public abstract class AbstractSolidFieldObject extends AbstractFieldObject {
    public AbstractSolidFieldObject(Vector location) {
        super(location);
    }

    @Override
    public void snakeInteract(ISnakeHead snake) {
        snake.kill();
    }
}
