package model;

import model.FieldObjects.Apple;
import model.FieldObjects.SnakePart;
import model.Interfaces.IPlayer;
import model.Interfaces.IField;
import model.Interfaces.ISnakeController;

import java.util.LinkedList;

public class SnakeController implements ISnakeController {
    private LinkedList<Vector> body;
    private IPlayer player;
    private Vector direction;
    private IField field;

    private int lengthQueue;
    private boolean alive;

    public SnakeController(IField field, Vector location, Vector direction, IPlayer player) {
        body = new LinkedList<>();
        body.addFirst(location);
        lengthQueue = 0;
        alive = true;

        this.field = field;
        this.player = player;
        this.direction = direction;
        field.addObject(new SnakePart(location));
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

    public void kill() {
        for (Vector location : body) {
            field.removeObjectAt(location);
            field.addObject(new Apple(location));
        }
        body.clear();
        alive = false;
    }

    public void grow(int value) {
        if (value <= -length()) {
            kill();
            return;
        }
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
        } else {
            Vector tail = body.removeLast();
            field.removeObjectAt(tail);
        }
    }

    public void updateDirection() {
        Vector newDirection = player.getDirection(getHead(), direction);
        if (newDirection != null && Vector.getScalarProduct(newDirection, direction) == 0)
            direction = newDirection;
    }
}
