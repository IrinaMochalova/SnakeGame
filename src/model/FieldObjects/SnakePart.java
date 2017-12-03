package model.FieldObjects;

import model.Interfaces.ISnakeController;
import model.Vector;

public class SnakePart extends FieldObject {
    public SnakePart(Vector location) {
        super(location);
    }

    @Override
    public void interact(ISnakeController controller) {
        controller.kill();
    }
}
