package model.FieldObjects;

import model.Interfaces.IFieldObject;
import model.Interfaces.ISnakeController;
import model.Vector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    public void interact(ISnakeController controller) {}

    public boolean isActive() {
        return isActive;
    }

    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
        location = (Vector)input.readObject();
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.writeObject(location);
    }
}
