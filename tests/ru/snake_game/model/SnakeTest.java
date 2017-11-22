package ru.snake_game.model;

import org.junit.Before;
import org.junit.Test;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import static org.junit.Assert.*;

public class SnakeTest {
    Snake snake;

    @Before
    public void setUp() throws Exception {
        snake = new Snake(new Vector(3,1), Directions.RIGHT);
    }

    @Test
    public void length() throws Exception {
        assertEquals(1, snake.length());
    }

    @Test
    public void getDirection() throws Exception {
        assertEquals(Directions.RIGHT, snake.getDirection());
    }

    @Test
    public void setDirection() throws Exception {
        snake.setDirection(Directions.DOWN);
        snake.setDirection(Directions.UP);
        assertEquals(Directions.DOWN, snake.getDirection());
    }

    @Test
    public void isAlive() throws Exception {
        assertTrue(snake.isAlive());
    }

    @Test
    public void kill() throws Exception {
        snake.kill();
        assertFalse(snake.isAlive());
    }

    @Test
    public void eat() throws Exception {
        snake.eat(1);
        snake.move();
        assertEquals(2, snake.length());
    }

    @Test
    public void move() throws Exception {
        snake.move();
        assertEquals("(4, 1)",snake.getHead().toString());
    }

    @Test
    public void getHead() throws Exception {
        snake.getHead();
    }

    @Test
    public void getTrace() throws Exception {
        assertEquals(1,snake.getTrace().length);
    }

}