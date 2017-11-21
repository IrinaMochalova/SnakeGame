package ru.snake_game.model.FieldObjects;

import ru.snake_game.model.Interfaces.ISnake;
import ru.snake_game.model.util.Vector;

public class Apple extends FieldObject {
    private final int foodValue;

    public Apple(Vector location, int foodValue) {
        super(location);
        if (foodValue < 1)
            throw new IllegalArgumentException("foodValue should be positive.");
        this.foodValue = foodValue;
    }

    @Override
    public void interact(ISnake snake) {
        snake.eat(foodValue);
    }
}
