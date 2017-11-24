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

    AISnakeController(IField field){
        this.field = field;
    }

    public Vector calculateDirecion(Vector headLocation) {
        Vector direction = headLocation;
        double distance = Double.MAX_VALUE;
        Vector apple = findApple(headLocation);
        for(Vector newDirection : Directions.ALL) {
            if(Vector.getScalarProduct(direction, newDirection) == 0) {
                HashSet<Vector> snakes = field.getAllSnakeCells();
                Vector newLocation = headLocation.add(newDirection);
                if (field.getObjectAt(newLocation) instanceof Wall && snakes.contains(newLocation))
                    continue;
                if(apple != null) {
                    double newDistance = Vector.getDistance(headLocation, apple);
                    if (newDistance < distance) {
                        distance = newDistance;
                        direction = newDirection;
                    }
                }
                else {
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
