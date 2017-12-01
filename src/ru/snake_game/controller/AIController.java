package ru.snake_game.controller;

import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.FieldObjects.Apple;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.util.Directions;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AIController implements IController {
    private IField field;
    private Vector apple;

    public AIController(IField field){
        this.field = field;
    }

    public Vector getDirection(Vector head, Vector direction) {
        LinkedList<Vector>way = findWayToApple(head, direction);
        if (apple == null)
            return getAvailableDirections(head, direction).get(0);
        return way.removeFirst();
    }

    private LinkedList<Vector> findWayToApple(Vector head, Vector direction){
        Vector[][] connections = findConnections(head, direction);
        LinkedList<Vector> way = new LinkedList<>();

        Vector location = apple;
        Vector prevLocation = connections[apple.getX()][apple.getY()];
        while (prevLocation != null){
            way.addFirst(location.subtract(prevLocation));
            location = prevLocation;
            prevLocation = connections[location.getX()][location.getY()];
        }

        return way;
    }

    private Vector[][] findConnections(Vector headLocation, Vector myDirection) {
        LinkedList<Vector> queue= new LinkedList<>();
        LinkedList<Vector> directionsQueue = new LinkedList<>();
        HashSet<Vector> used = new HashSet<>();
        Vector[][] parent = new Vector[field.getWidth()][field.getHeight()];

        queue.add(headLocation);
        directionsQueue.add(myDirection);
        used.add(headLocation);
        parent[headLocation.getX()][headLocation.getY()] = null;

        while (!(field.getObjectAt(headLocation) instanceof Apple) && queue.size() != 0){
            headLocation = queue.poll();
            Vector prevDirection = directionsQueue.poll();
            if (headLocation.getX() > 0 && headLocation.getX() < field.getWidth()-1 &&
                    headLocation.getY() > 0 && headLocation.getY() < field.getHeight()-1 ) {
                for (Vector direction : getAvailableDirections(headLocation, prevDirection)) {
                    Vector newLocation = headLocation.add(direction);
                    if (!used.contains(newLocation)) {
                        queue.add(newLocation);
                        directionsQueue.add(direction);
                        used.add(newLocation);
                        parent[newLocation.getX()][newLocation.getY()] = headLocation;
                    }
                }
            }
        }

        apple = field.getObjectAt(headLocation) instanceof Apple ? headLocation : null;
        return parent;
    }

    private ArrayList<Vector> getAvailableDirections(Vector location, Vector direction) {
        ArrayList<Vector> directions = new ArrayList<>();
        for (Vector newDirection : Directions.ALL) {
            IFieldObject object  = field.getObjectAt(location.add(newDirection));
            if (!newDirection.multiply(-1).equals(direction) && (object == null || object instanceof Apple))
                directions.add(newDirection);
        }
        if (directions.size() == 0)
            directions.add(direction);
        return directions;
    }
}