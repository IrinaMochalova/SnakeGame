package ru.snake_game.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.Wall;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;
    private Field field;

    @Before
    public void setUp() throws Exception {
        field = new Field(6,6);
        int width = field.getWidth();
        int height = field.getHeight();
        for (int y = 0; y < height; y += 1) {
            Vector location = new Vector(0, y);
            field.addObject(new Wall(location));
            location = new Vector(width - 1, y);
            field.addObject(new Wall(location));
        }
        for (int x = 1; x < width - 1; x++) {
            Vector location = new Vector(x, 0);
            field.addObject(new Wall(location));
            location = new Vector(x, height - 1);
            field.addObject(new Wall(location));
        }

        game = new Game(field);
    }

    @After
    public void tearDown() throws Exception {
        field = null;
        game = null;
    }

    @Test
    public void tick() throws Exception {
        Vector location = new Vector(1,1);
        field.addObject(new SnakeHead(location, null, Directions.RIGHT, field));
        location = new Vector(3, 2);
        field.addObject(new Apple(location, field, 2));
        SnakeHead snakeHeadHead = (SnakeHead) field.getSnakeHead();

        game.tick();
        game.tick();
        snakeHeadHead.setDirection(Directions.DOWN);
        game.tick();
        game.tick();
        game.tick();
        snakeHeadHead.setDirection(Directions.LEFT);
        game.tick();
        game.tick();
        game.tick();

        assertFalse(snakeHeadHead.isAlive());
        assertTrue(field.getObjectAt(new Vector(0,4)) instanceof Wall);
        assertNull(field.getObjectAt(new Vector(3, 2)));
        assertEquals(new Vector(1,4), snakeHeadHead.getLocation());
        assertEquals(3, snakeHeadHead.length());
    }

    @Test
    public void cycleTick() throws Exception{
        Vector location = new Vector(1,1);
        SnakeHead snakeHeadHead = new SnakeHead(location, null, Directions.RIGHT, field);
        field.addObject(snakeHeadHead);
        location  = new Vector(2,1);
        field.addObject(new Apple(location, field, 11));

        ArrayList<Vector> directions = new ArrayList<>();
        directions.add(Directions.DOWN);
        directions.add(Directions.LEFT);
        directions.add(Directions.UP);
        directions.add(Directions.RIGHT);

        for (int j = 0; j < 5; j++) {
            for (Vector direction : directions) {
                for (int i = 0; i < 3; i++)
                    game.tick();
                snakeHeadHead.setDirection(direction);
            }
        }

        assertTrue(snakeHeadHead.isAlive());
        assertEquals(12, snakeHeadHead.length());
    }

}