package ru.snake_game.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.Wall;
import ru.snake_game.model.Interfaces.ISnake;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.FieldMakers;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;
    private Field field;
    private Snake snake;

    /*@Before
    public void setUp() throws Exception {
        FieldMakers fieldMakers = new FieldMakers();
        field = fieldMakers.makeBoardedField(6,6);
        snake = new Snake(new Vector(1,1), Directions.RIGHT);
        field.addSnake(snake);

        game = new Game(field);
    }

    @Test
    public void tick() throws Exception {
        field.addObject(new Apple(new Vector(3,2), 2));

        game.tick();
        game.tick();
        snake.setDirection(Directions.DOWN);
        game.tick();
        game.tick();
        game.tick();
        snake.setDirection(Directions.LEFT);
        game.tick();
        game.tick();
        game.tick();

        assertFalse(snake.isAlive());
        assertTrue(field.getObjectAt(new Vector(0,4)) instanceof Wall);
        assertNull(field.getObjectAt(new Vector(3, 2)));
        //assertEquals(new Vector(1,4), snakeHead);
        assertEquals(3, snake.length());
    }

    @Test
    public void cycleTick() throws Exception{
        field.addObject(new Apple(new Vector(2,1), 11));

        ArrayList<Vector> directions = new ArrayList<>();
        directions.add(Directions.DOWN);
        directions.add(Directions.LEFT);
        directions.add(Directions.UP);
        directions.add(Directions.RIGHT);

        for (int j = 0; j < 5; j++) {
            for (Vector direction : directions) {
                for (int i = 0; i < 3; i++)
                    game.tick();
                snake.setDirection(direction);
            }
        }

        assertTrue(snake.isAlive());
        assertEquals(12, snake.length());
    }
*/
}