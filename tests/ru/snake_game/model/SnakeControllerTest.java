package ru.snake_game.model;

import org.junit.Before;
import org.junit.Test;
import ru.snake_game.controller.AIController;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.ISnakeController;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.FieldMakers;
import ru.snake_game.model.util.Vector;

public class SnakeControllerTest {
    private ISnakeController snake;

    @Before
    public void setUp() throws Exception {
        IField field = FieldMakers.makeBoardedField(7,7);
        Vector location = new Vector(1,1);
        Vector direction = Directions.RIGHT;
        IController controller = new AIController(field);
        snake = new SnakeController(field, location, direction, controller);
    }

    @Test
    public void eatAndGo() throws Exception {
        int growValue = 3;
        snake.grow(growValue);
        for(int i = 0; i <= growValue; i++){
            snake.move();
        }

    }

    @Test
    public void move() throws Exception {
        snake.move();
    }

}