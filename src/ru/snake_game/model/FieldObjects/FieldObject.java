package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.Interfaces.ISnake;
import ru.snake_game.model.util.Vector;

public abstract class FieldObject implements IFieldObject {
    private Vector location;

    protected FieldObject(Vector location) {
        this.setLocation(location);
    }

    public Vector getLocation() {
        return location;
    }

    protected void setLocation(Vector location) {
        if (location == null)
            throw new IllegalArgumentException("Location can not be null.");
        this.location = location;
    }

    public boolean interact(ISnake snake) {
        return false;
    }
}
