package ru.snake_game.model;

import ru.snake_game.model.Interfaces.*;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;

public class Game implements IGame {
    protected IField field;
    protected HashSet<IGenerator> generators;

    public Game(IField field, HashSet<IGenerator> generators) {
        this.field = field;
        this.generators = generators;
    }

    public IField getField() {
        return field;
    }

    public void tick() {
        useGenerators();
        moveSnakes();
    }

    protected void moveSnakes() {
        HashSet<ISnakeController> died = new HashSet<>();
        for (ISnakeController snake : field.getSnakes()) {
            checkCollision(snake);
            if (!snake.isAlive())
                died.add(snake);
            else
                snake.move();
        }
        for (ISnakeController snake : died)
            field.removeSnake(snake);
    }

    protected void checkCollision(ISnakeController snake) {
        snake.updateDirection();
        Vector location = snake.getHead().add(snake.getDirection());
        IFieldObject object = field.getObjectAt(location);
        if (object != null) {
            object.interact(snake);
            if (!object.isActive())
                field.removeObjectAt(location);
        }
    }

    protected void useGenerators() {
        for (IGenerator generator : generators)
            generator.process();
    }
}