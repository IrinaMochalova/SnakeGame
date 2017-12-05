package model.Interfaces;

import model.Vector;

import java.io.Serializable;

public interface IFieldObject extends Serializable {
    Vector getLocation();

    void interact(ISnakeController controller);

    boolean isActive();
}
