package model.FieldObjects;

import model.Interfaces.IFieldObject;
import model.Interfaces.ISnakeController;
import model.Vector;

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
