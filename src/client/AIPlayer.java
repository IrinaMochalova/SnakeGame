package client;

import model.FieldObjects.Apple;
import model.Interfaces.IPlayer;
import model.Interfaces.IField;
import model.Interfaces.IFieldObject;
import model.Direction;
import model.Vector;

import java.util.ArrayList;

public class AIPlayer implements IPlayer {
    private IField field;
    private Vector apple;

    public AIPlayer(IField field){
        this.field = field;
    }

    public Vector getDirection(Vector head, Vector direction) {
        ArrayList<Vector> availableDirections = getAvailableDirections(head, direction);
        updateAppleLocation(head);
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

    private void updateAppleLocation(Vector headLocation) {
        if (apple != null && field.getObjectAt(apple) instanceof Apple)
            return;
        apple = null;
        double distance = Double.MAX_VALUE;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Vector target = new Vector(x, y);
                IFieldObject object = field.getObjectAt(target);
                if (object instanceof Apple) {
                    double newDistance = Vector.getManhattanDistance(target, headLocation);
                    if (newDistance < distance) {
                        apple = target;
                        distance = newDistance;
                    }
                }
            }
        }
    }


    private ArrayList<Vector> getAvailableDirections(Vector location, Vector direction) {
        ArrayList<Vector> directions = new ArrayList<>();
        for (Vector newDirection : Direction.ALL) {
            IFieldObject object  = field.getObjectAt(location.add(newDirection));
            if (!newDirection.multiply(-1).equals(direction) && (object == null || object instanceof Apple))
                directions.add(newDirection);
        }
        return directions;
    }
}
