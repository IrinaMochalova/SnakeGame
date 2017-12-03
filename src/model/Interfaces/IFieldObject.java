package model.Interfaces;

import model.Vector;

public interface IFieldObject {
    Vector getLocation();

    void interact(ISnakeController controller);

    boolean isActive();
}