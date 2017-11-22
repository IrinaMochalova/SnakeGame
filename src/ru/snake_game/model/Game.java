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
        checkCollisions();
        useGenerators();
    }

    private void checkCollisions() {
        HashSet<Vector> snakeCells = field.getAllSnakeCells();
        for (int number = 0; number < field.getSnakesCount(); number++) {
            ISnake snake = field.getSnake(number);
            Vector newHead = snake.getHead().add(snake.getDirection());
            IFieldObject object = field.getObjectAt(newHead);
            if (object != null)
                object.interact(snake);
            if (snakeCells.contains(newHead))
                snake.kill();
            if (snake.isAlive()) {
                snakeCells.removeAll(snake.getTrace());
                snake.move();
                snakeCells.addAll(snake.getTrace());
                field.removeObjectAt(newHead);
            }
        }
    }

    private void useGenerators() {
        for (IGenerator generator : generators)
            generator.process();
    }
}