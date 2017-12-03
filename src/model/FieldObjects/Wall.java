package model.FieldObjects;

import model.Interfaces.ISnakeController;
import model.Vector;

public class Wall extends FieldObject {
    public Wall(Vector location) {
        super(location);
    }

    @Override
    public void interact(ISnakeController controller) {
        controller.kill();
    }
}
