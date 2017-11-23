package ru.snake_game.model;

import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.Wall;
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
        for (int number = 0; number < field.getSnakesCount(); number++) {
            ISnake snake = field.getSnake(number);
            if (!snake.isAlive()) {
                if (snake.length() > 0) {
                    for (Vector part : snake.getTrace()) {
                        if (field.getObjectAt(part) == null)
                            field.addObject(new Apple(part));
                        snake.eat(-snake.length());
                    }
                }
                continue;
            }
            snake.move();
            Vector head = snake.getHead();
            IFieldObject object = field.getObjectAt(head);
            if (object != null && object.interact(snake))
                field.removeObjectAt(head);
            if (field.getAllSnakeCells().contains(head) && !snake.getTrace().contains(head))
                snake.kill();
        }
    }

    public ArrayList<Vector> getAvailableDirections(int snakeNumber) {
        ISnake snake = field.getSnake(snakeNumber);
        Vector head = snake.getHead();
        ArrayList<Vector> available = new ArrayList<>();
        HashSet<Vector> snakes = field.getAllSnakeCells();
        for (Vector direction : Directions.ALL) {
            if (!snakes.contains(snake) && !(field.getObjectAt(head) instanceof Wall))
                available.add(direction);
        }
        return available;
    }

    protected void useGenerators() {
        for (IGenerator generator : generators)
            generator.process();
    }
}