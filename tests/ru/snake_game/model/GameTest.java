package ru.snake_game.model;

import org.junit.Before;
import org.junit.Test;
import ru.snake_game.controller.AIController;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IGame;
import ru.snake_game.model.Interfaces.IGenerator;
import ru.snake_game.model.Interfaces.ISnakeController;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.FieldMakers;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;

import static org.junit.Assert.*;

public class GameTest {
    private IField field;
    private IController aiController;
    private IGame game;
    private ISnakeController aiSnake;


    @Before
    public void setUp() throws Exception {
        field = FieldMakers.makeBoardedField(7,7);
        HashSet<IGenerator> generators = new HashSet<>();
        game = new Game(field, generators);

        aiController = new AIController(field);
        aiSnake = new SnakeController(field, new Vector(1, 1), Directions.RIGHT, aiController);
        field.addSnake(aiSnake);

        field.addObject(new Apple(new Vector(3, 5)));
    }

    @Test
    public void eatAndGo() throws Exception {
        field.addObject(new Apple(new Vector(5,4)));
        while (aiSnake.length() == 1)
            game.tick();
        assertTrue(aiSnake.isAlive());
        assertEquals(new Vector(3,5), aiSnake.getHead());
        assertEquals(Directions.RIGHT, aiSnake.getDirection());
    }

    @Test
    public void oopsSnakes() throws Exception{
        ISnakeController snake1= new SnakeController(field, new Vector(1,2), Directions.RIGHT, aiController);
        snake1.grow(1);
        ISnakeController snake2 = new SnakeController(field, new Vector(2,1), Directions.DOWN, aiController);
        snake2.grow(1);
        field.addSnake(snake1);
        field.addSnake(snake2);
        game.tick();
        assertFalse(aiSnake.isAlive());
        assertTrue(field.getObjectAt(new Vector(1,1)) instanceof Apple);
    }

}