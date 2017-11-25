package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.Interfaces.ISnakeController;
import ru.snake_game.model.util.Vector;

public class Wall extends FieldObject {
    public Wall(Vector location) {
        super(location);
    }

    @Override
    public void interact(ISnakeController controller) {
        controller.kill();
    }
}
