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

import static org.junit.Assert.*;

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
    public void growAndGo() throws Exception {
        int growValue = 3;
        snake.grow(growValue);
        for(int i = 0; i < growValue; i++){
            snake.move();
        }
        assertEquals(4, snake.length());
        assertEquals(new Vector(4,1), snake.getHead());
        assertTrue(snake.isAlive());
    }

    @Test
    public void growDownwards() throws Exception {
        growAndGo();
        int growValue = -2;
        snake.grow(growValue);

        assertEquals(2, snake.length());
        assertTrue(snake.isAlive());
    }

    @Test
    public void  oopsGrowDownwards() throws Exception {
        snake.grow(-2);
        assertFalse(snake.isAlive());
    }
}