package ru.snake_game.model;

import ru.snake_game.model.Interfaces.*;
import ru.snake_game.model.util.Vector;

public class Game implements IGame {
    private IField field;

    public Game(IField field) {
        this.field = field;
    }

    public void tick() {
        for (int number = 0; number < field.getSnakesCount(); number++) {
            ISnake snake = field.getSnake(number);
            IFieldObject object = field.getObjectAt(snake.getHead());
            if (object != null) {
                object.interact(snake);
            }
            snake.move();
        }
    }

    public IField getField() {
        return field;
    }
}