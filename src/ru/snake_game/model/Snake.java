package ru.snake_game.model;

import ru.snake_game.controller.Interfaces.ISnakeController;
import ru.snake_game.model.Interfaces.ISnake;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;
import java.util.LinkedList;

public class Snake implements ISnake {
    private ISnakeController controller;
    private LinkedList<Vector> body;
    private int lengthQueue;
    private boolean isAlive;

    public Snake(Vector location, ISnakeController controller) {
        lengthQueue = 0;
        isAlive = true;
        body = new LinkedList<>();
        body.addFirst(location);
        this.controller = controller;
    }

    public int length() {
        return body.size();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public void eat(int foodValue) {
        while (foodValue < 0) {
            body.removeLast();
            foodValue++;
        }

        lengthQueue += foodValue;
    }

    public void move() {
        Vector head = body.peekFirst();
        Vector direction = controller.calculateDirection(head);
        body.addFirst(head.add(direction));

        if (lengthQueue > 0)
            lengthQueue--;
        else
            body.removeLast();
    }

    public Vector getHead() {
        return body.peekFirst();
    }

    public HashSet<Vector> getTrace() {
        HashSet<Vector> trace = new HashSet<>();
        trace.addAll(body);
        return trace;
    }
}
