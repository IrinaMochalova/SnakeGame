package ru.snake_game.model;

import ru.snake_game.model.Interfaces.*;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;
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
        checkCollisions();
        updateObjects();
        moveSnakes();
    }

    protected void moveSnakes() {
        for (ISnakeController controller : field.getSnakes())
            controller.move();
    }

    protected void checkCollisions() {
        for (ISnakeController controller : field.getSnakes()) {
            Vector head = controller.getHead();
            Vector location = head.add(controller.getDirection());
            IFieldObject object = field.getObjectAt(location);
            if (object == null)
                continue;
            object.interact(controller);
        }
    }

    protected void updateObjects() {
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Vector location = new Vector(x, y);
                IFieldObject object = field.getObjectAt(location);
                if (object != null && !object.isActive())
                    field.removeObjectAt(location);
            }
        }
    }

    protected void useGenerators() {
        for (IGenerator generator : generators)
            generator.process();
    }
}