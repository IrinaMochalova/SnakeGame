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
    private HashSet<IGenerator> generators;
    private IGame game;

    @Before
    public void setUp() throws Exception {
        field = FieldMakers.makeBoardedField(7,7);
        generators = new HashSet<>();
        generators.add(new Generator<>(Apple.class, field));
        game = new Game(field, generators);

        IController aiController = new AIController(field);
        ISnakeController aiSnake = new SnakeController(field, new Vector(1, 1), Directions.RIGHT, aiController);
        field.addSnake(aiSnake);
    }

    @Test
    public void tick() throws Exception {
        game.tick();
    }

}