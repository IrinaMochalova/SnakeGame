package client;

import model.FieldObjects.Apple;
import model.Interfaces.IPlayer;
import model.Interfaces.IField;
import model.Interfaces.IFieldObject;
import model.Direction;
import model.Vector;

import java.util.ArrayList;

public class SimpleAIPlayer implements IPlayer {
    private IField field;

    public SimpleAIPlayer(IField field){
        this.field = field;
    }

    public Vector getDirection(Vector head, Vector direction) {
        ArrayList<Vector> availableDirections = getAvailableDirections(head, direction);
        Vector apple = findApple(head);
        if (apple == null)
            return availableDirections.get(0);
        double distance = Double.MAX_VALUE;
        for (Vector newDirection : availableDirections) {
            Vector newLocation = head.add(newDirection);
            double newDistance = Vector.getManhattanDistance(newLocation, apple);
            if (newDistance < distance) {
                distance = newDistance;
                direction = newDirection;
            }
        }
        return direction;
    }

    private Vector findApple(Vector head) {
        Vector apple = null;
        double distance = Double.MAX_VALUE;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Vector target = new Vector(x, y);
                IFieldObject object = field.getObjectAt(target);
                if (object instanceof Apple) {
                    double newDistance = Vector.getManhattanDistance(target, head);
                    if (newDistance < distance) {
                        apple = target;
                        distance = newDistance;
                    }
                }
            }
        }
        return apple;
    }

    private ArrayList<Vector> getAvailableDirections(Vector location, Vector direction) {
        ArrayList<Vector> directions = new ArrayList<>();
        Vector inverted = direction.multiply(-1);
        for (Vector newDirection : Direction.ALL) {
            IFieldObject object  = field.getObjectAt(location.add(newDirection));
            if (!newDirection.equals(inverted) && (object == null || object instanceof Apple))
                directions.add(newDirection);
        }
        return directions;
    }
}
