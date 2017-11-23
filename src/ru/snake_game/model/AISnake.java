package ru.snake_game.model;

import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;

public class AISnake extends Snake {
    private IField field;
    private Vector target;

    public AISnake(Vector location, Vector direction, IField field) {
        super(location, direction);
        this.field = field;
        this.target = null;
    }

    @Override
    public void move() {
        updateDirection();
        super.move();
    }

    private void updateDirection() {
        Vector head = getHead();
        findApple(head);
        HashSet<Vector> snakes = field.getAllSnakeCells();
        Vector direction = this.getDirection();
        double distance = Vector.getManhattanDistance(head.add(direction), target);
        for (Vector newDirection : new Vector[] {Directions.LEFT, Directions.RIGHT, Directions.UP, Directions.DOWN}) {
            Vector newHead = head.add(newDirection);
            if (snakes.contains(newHead))
                continue;
            double newDistance = Vector.getManhattanDistance(newHead, target);
            if (newDistance < distance && Vector.getScalarProduct(direction, newDirection) == 0) {
                distance = newDistance;
                direction = newDirection;
            }
        }
        this.setDirection(direction);
    }

    private void findApple(Vector location) {
        if (target != null && field.getObjectAt(target) instanceof Apple)
            return;
        double distance = Double.MAX_VALUE;
        Vector apple = location;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Vector newTarget = new Vector(x, y);
                IFieldObject object = field.getObjectAt(newTarget);
                if (object == null || !(object instanceof Apple))
                    continue;
                double newDistance = Vector.getManhattanDistance(newTarget, location);
                if (newDistance < distance) {
                    apple = newTarget;
                    distance = newDistance;
                }
            }
        }
        this.target = apple;
    }
}
