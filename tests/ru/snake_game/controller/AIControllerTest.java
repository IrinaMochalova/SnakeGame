package ru.snake_game.controller;

import org.junit.Before;
import org.junit.Test;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.Field;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.FieldMakers;
import ru.snake_game.model.util.Vector;
import static org.junit.Assert.*;

public class AIControllerTest {
    private Field field;
    private IController controller;
    private Vector head;

    @Before
    public void setUp() throws Exception{
        field = FieldMakers.makeBoardedField(7,7);
        controller = new AIController(field);
        head = new Vector(1,1);
    }

    @Test
    public void snakeInEmptyField() throws Exception {
        assertEquals(Directions.DOWN, controller.getDirection(head, Directions.RIGHT));
        assertEquals(Directions.RIGHT, controller.getDirection(head, Directions.UP));
        assertEquals(Directions.DOWN, controller.getDirection(head, Directions.LEFT));
        assertEquals(Directions.DOWN, controller.getDirection(head, Directions.DOWN));
    }

    @Test
    public void snakeFindApple() throws Exception {
        field.addObject(new Apple(new Vector(1,4)));
        assertEquals(Directions.DOWN, controller.getDirection(head, Directions.RIGHT));
        assertEquals(Directions.RIGHT, controller.getDirection(head, Directions.UP));
        assertEquals(Directions.DOWN, controller.getDirection(head,Directions.LEFT));
        assertEquals(Directions.DOWN, controller.getDirection(head, Directions.DOWN));
    }

/*    @Test
    public void snakeInClosedSpace() throws Exception{

    }*/

/*    @Test
    public void snakeCollideWithOtherSnake() throws Exception{

    }*/

}