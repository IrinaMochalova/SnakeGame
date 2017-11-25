package ru.snake_game.model;

import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.Interfaces.ISnakeController;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Field implements IField {
    private IFieldObject[][] field;
    private HashSet<ISnakeController> snakes;

    public Field(int height, int width) {
        if (height < 1 || width < 1)
            throw new IllegalArgumentException("Incorrect field size.");
        field = new IFieldObject[width][];
        for (int i = 0; i < field.length; i++)
            field[i] = new IFieldObject[height];
        snakes = new HashSet<>();
    }

    public int getWidth() {
        return field.length;
    }

    public int getHeight() {
        return field[0].length;
    }

    public IFieldObject getObjectAt(Vector location) {
        return field[location.getX()][location.getY()];
    }

    public void addObject(IFieldObject object) {
        Vector location = object.getLocation();
        if (getObjectAt(location) != null)
            throw new IllegalArgumentException("This location is already used.");
        field[location.getX()][location.getY()] = object;
    }

    public void removeObjectAt(Vector location) {
        field[location.getX()][location.getY()] = null;
    }

    public void addSnake(ISnakeController controller){
        snakes.add(controller);
    }

    public void removeSnake(ISnakeController controller) {
        snakes.remove(controller);
    }

    public HashSet<ISnakeController> getSnakes() {
        return snakes;
    }

    public Vector findEmptyCell() {
        ArrayList<Vector> freeCells = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Vector location = new Vector(x, y);
                if (getObjectAt(location) == null)
                    freeCells.add(location);
            }
        }
        int index = (new Random()).nextInt(freeCells.size());
        return freeCells.get(index);
    }
}