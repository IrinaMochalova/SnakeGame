package ru.snake_game.controller;

import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.Wall;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;

public class AIController implements IController {
    private IField field;
    private Vector direction;
    private Vector apple;

    public AIController(IField field){
        this.field = field;
    }

    public Vector getDirection(Vector head, Vector initialDirection) {
        this.direction = initialDirection;
        findApple(head);
        ArrayList<Vector> availableDirections = getAvailableDirections(head, direction);
        if (apple == null) {
            if (field.getObjectAt(head.add(direction)) instanceof Wall) {
                for (Vector newDirection : availableDirections) {
                    direction = newDirection;
                }
            }
            return direction;
        }
        double distance = Double.MAX_VALUE;
        for(Vector newDirection : availableDirections) {
            Vector newLocation = head.add(newDirection);
            double newDistance = Vector.getManhattanDistance(newLocation, apple);
            if (newDistance < distance) {
                distance = newDistance;
                direction = newDirection;
            }
        }
        return direction;
    }

    private void findApple(Vector headLocation) {
        if (apple != null && field.getObjectAt(apple) instanceof Apple)
            return;
        double distance = Double.MAX_VALUE;
        Vector apple = headLocation;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Vector newTarget = new Vector(x, y);
                IFieldObject object = field.getObjectAt(newTarget);
                if (object == null || !(object instanceof Apple))
                    continue;
                double newDistance = Vector.getDistance(newTarget, headLocation);
                if (newDistance < distance) {
                    apple = newTarget;
                    distance = newDistance;
                }
            }
        }
        this.apple = headLocation == apple ? null : apple;
    }


    private ArrayList<Vector> getAvailableDirections(Vector location, Vector direction) {
        ArrayList<Vector> directions = new ArrayList<>();
        for (Vector newDirection : Directions.ALL) {
            IFieldObject object  = field.getObjectAt(location.add(newDirection));
            if ((Vector.getScalarProduct(direction, newDirection) == 0 || direction == newDirection) &&
                (object == null || object instanceof Apple))
                directions.add(newDirection);
        }
        return directions;
    }
}