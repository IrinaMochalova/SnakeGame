package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.util.Vector;

abstract public class AbstractFieldObject implements IFieldObject {
    private Vector location;

    protected AbstractFieldObject(Vector location) {
        if (location == null)
            throw new IllegalArgumentException("location is null.");
        this.setLocation(location);
    }

    @Override
    public Vector getLocation() {
        return location;
    }

    protected void setLocation(Vector location) {
        if (location == null)
            throw new IllegalArgumentException("Vector can'n be null.");
        this.location = location;
    }

    @Override
    public void act() {
    }
}
