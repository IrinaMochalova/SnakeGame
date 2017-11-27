package ru.snake_game.model;

import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.SnakePart;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.ISnakeController;
import ru.snake_game.model.util.Vector;

import java.util.LinkedList;

public class SnakeController implements ISnakeController {
    private LinkedList<Vector> body;
    private IController controller;
    private Vector direction;
    private IField field;

    private int lengthQueue;
    private boolean alive;

    public SnakeController(IField field, Vector location, IController controller) {
        body = new LinkedList<>();
        body.addFirst(location);
        lengthQueue = 0;
        alive = true;

        this.field = field;
        this.controller = controller;
        field.addObject(new SnakePart(location));

        updateDirection();
    }

    public int length() {
        return body.size();
    }

    public boolean isAlive() {
        return alive;
    }

    public Vector getHead() {
        return body.peekFirst();
    }

    public Vector getDirection() {
        return direction;
    }

    public boolean hasLocation(Vector location) {
        return body.contains(location);
    }

    public void kill() {
        for (Vector location : body) {
            field.removeObjectAt(location);
            field.addObject(new Apple(location));
        }
        body.clear();
        alive = false;
    }

    public void grow(int value) {
        while (value < 0) {
            Vector location = body.removeLast();
            field.removeObjectAt(location);
            value++;
        }

        lengthQueue += value;
    }

    public void move() {
        Vector head = getHead();
        Vector newHead = head.add(direction);
        body.addFirst(newHead);
        field.addObject(new SnakePart(newHead));
        if (lengthQueue > 0) {
            lengthQueue--;
        }
        else {
            Vector tail = body.removeLast();
            field.removeObjectAt(tail);
        }
    }

    public void updateDirection() {
        direction = controller.updateDirection(getHead());
    }
}
