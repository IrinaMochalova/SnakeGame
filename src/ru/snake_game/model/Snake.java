package ru.snake_game.model;

import ru.snake_game.model.Interfaces.ISnake;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;
import java.util.LinkedList;

public class Snake implements ISnake {
    private LinkedList<Vector> body;
    private Vector direction;
    private int lengthQueue;
    private boolean isAlive;

    public Snake(Vector location, Vector direction) {
        lengthQueue = 0;
        isAlive = true;
        this.direction = direction;
        body = new LinkedList<>();
        body.addFirst(location);
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        if (Vector.getScalarProduct(direction, this.direction) == 0)
            this.direction = direction;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public void eat(int growValue) {
        lengthQueue += growValue;
    }

    public void move() {
        Vector head = body.peekFirst();
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
