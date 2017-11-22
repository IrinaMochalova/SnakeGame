package ru.snake_game.model;

import org.junit.Before;
import org.junit.Test;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.Wall;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.FieldMakers;
import ru.snake_game.model.util.Vector;

import static org.junit.Assert.*;

public class FieldTest {
    Field field;

    @Before
    public void setUp() throws Exception {
        FieldMakers fieldMakers = new FieldMakers();
        field = fieldMakers.makeBoardedField(6,6);
        field.addSnake(new Snake(new Vector(3, 1), Directions.RIGHT));
        field.addSnake(new Snake(new Vector(2, 1), Directions.RIGHT));
    }

    @Test
    public void getObjectAt() throws Exception {
        assertNull(field.getObjectAt(4,1));
        assertTrue(field.getObjectAt(0,0) instanceof Wall);
    }

    @Test
    public void getObjectAt1() throws Exception {
        assertNull(field.getObjectAt(new Vector(3,3)));
        assertTrue(field.getObjectAt(new Vector(1,1)) instanceof Snake);
    }

    @Test
    public void addObject() throws Exception {
        Vector location = new Vector(5,5);
        field.addObject(new Apple(location, 1));
        assertTrue(field.getObjectAt(5,5) instanceof Apple);
    }

    @Test
    public void getWidth() throws Exception {
        assertEquals(6, field.getWidth());
    }

    @Test
    public void getHeight() throws Exception {
        assertEquals(6, field.getHeight());
    }

    @Test
    public void getSnakesCount() throws Exception {
        assertEquals(2, field.getSnakesCount());
    }

    @Test
    public void addSnake() throws Exception {
        Vector location = new Vector(1,1);
        int length = field.addSnake(new Snake(location, Directions.RIGHT));
        assertEquals(3, length);
    }

    @Test
    public void findEmptyCell() throws Exception {
        assertNull(field.findEmptyCell());
    }


}