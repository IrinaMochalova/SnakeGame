package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.Interfaces.ISnakeController;
import ru.snake_game.model.util.Vector;

public abstract class FieldObject implements IFieldObject {
    protected Vector location;
    protected boolean isActive;

    protected FieldObject(Vector location) {
        if (location == null)
            throw new IllegalArgumentException("Location can not be null.");
        this.location = location;
        isActive = true;
    }

    public Vector getLocation() {
        return location;
    }

    public void interact(ISnakeController controller) {
    }

    public boolean isActive() {
        return isActive;
    }
}
