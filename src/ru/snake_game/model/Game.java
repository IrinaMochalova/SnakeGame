package ru.snake_game.model;

import ru.snake_game.model.Interfaces.*;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;
import java.util.Iterator;

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
        Iterator iterator = field.getSnakes().iterator();
        while (iterator.hasNext()) {
            ISnakeController snake = (ISnakeController)iterator.next();
            snake.updateDirection();
            Vector head = snake.getHead();
            Vector location = head.add(snake.getDirection());
            IFieldObject object = field.getObjectAt(location);
            if (object != null) {
                object.interact(snake);
                if (!snake.isAlive()) {
                    iterator.remove();
                    continue;
                }
                if (!object.isActive())
                    field.removeObjectAt(location);
            }
            snake.move();
        }
    }

    protected void useGenerators() {
        for (IGenerator generator : generators)
            generator.process();
    }
}