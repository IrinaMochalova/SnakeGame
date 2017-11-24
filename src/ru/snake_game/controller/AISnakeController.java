package ru.snake_game.controller;

import ru.snake_game.controller.Interfaces.ISnakeController;
import ru.snake_game.model.Field;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.FieldObjects.Wall;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.Snake;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import java.util.HashSet;

public class AISnakeController implements ISnakeController {
    private IField field;
    private Vector direction;

    public AISnakeController(IField field, Vector initialDirection){
        this.field = field;
        this.direction = initialDirection;
    }

    public Vector calculateDirection(Vector headLocation) {
        Vector apple = findApple(headLocation);
        if (apple == null) {
            if (field.getObjectAt(headLocation.add(direction)) instanceof Wall) {
                for (Vector newDirection : Directions.ALL) {
                    if(Vector.getScalarProduct(direction, newDirection) == 0) {
                        HashSet<Vector> snakes = field.getAllSnakeCells();
                        Vector newLocation = headLocation.add(newDirection);
                        if (field.getObjectAt(newLocation) instanceof Wall || snakes.contains(newLocation)) {
                            continue;
                        }
                        direction = newDirection;
                        return direction;
                    }
                }
            }
            else
                return direction;
        }
        double distance = apple != null ? Vector.getManhattanDistance(headLocation.add(direction), apple) : 0;
        for(Vector newDirection : Directions.ALL) {
            if(Vector.getScalarProduct(direction, newDirection) == 0) {
                HashSet<Vector> snakes = field.getAllSnakeCells();
                Vector newLocation = headLocation.add(newDirection);
                if (field.getObjectAt(newLocation) instanceof Wall || snakes.contains(newLocation))
                    continue;
                double newDistance = Vector.getManhattanDistance(newLocation, apple);
                if (newDistance < distance) {
                    distance = newDistance;
                    direction = newDirection;
                }
            }
        }
        return direction;
    }

    private Vector findApple(Vector headLocation) {
        //if (target != null && field.getObjectAt(target) instanceof Apple)
            //return;
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
        return headLocation == apple ? null : apple;
    }
}
