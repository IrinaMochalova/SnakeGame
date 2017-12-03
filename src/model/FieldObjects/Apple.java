package model.FieldObjects;

import model.Interfaces.ISnakeController;
import model.Vector;

import java.util.Random;

public class Apple extends FieldObject {
    private final int foodValue;

    public Apple(Vector location) {
        super(location);
        this.foodValue = 1 + (new Random()).nextInt(3);
    }

    @Override
    public void interact(ISnakeController controller) {
        controller.grow(foodValue);
        isActive = false;
    }
}
