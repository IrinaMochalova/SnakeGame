package ru.snake_game.model;

import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.Interfaces.IFieldObject;
import ru.snake_game.model.Interfaces.ISnake;
import ru.snake_game.model.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Field implements IField {
    private IFieldObject[][] field;
    private ArrayList<ISnake> snakes;

    public Field(int height, int width) {
        if (height < 1 || width < 1)
            throw new IllegalArgumentException("Incorrect field size.");
        field = new IFieldObject[width][];
        for (int i = 0; i < field.length; i++)
            field[i] = new IFieldObject[height];
        snakes = new ArrayList<>();
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

    public IFieldObject getObjectAt(int x, int y) {
        return field[x][y];
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

    public int getSnakesCount() {
        return snakes.size();
    }

    public int addSnake(ISnake snake){
        snakes.add(snake);
        return snakes.size() - 1;
    }

    public ISnake getSnake(int number){
        if (number >= snakes.size())
            throw new IndexOutOfBoundsException("Snake with given number doesn't exist.");
        return snakes.get(number);
    }

    public Vector findEmptyCell() {
        HashSet<Vector> snakeCells = getAllSnakeCells();
        ArrayList<Vector> freeCells = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Vector location = new Vector(x, y);
                if (getObjectAt(location) == null && !snakeCells.contains(location))
                    freeCells.add(location);
            }
        }
        int index = (new Random()).nextInt(freeCells.size());
        return freeCells.get(index);
    }

    public HashSet<Vector> getAllSnakeCells() {
        HashSet<Vector> snakeCells = new HashSet<>();
        for (ISnake snake : snakes) {
            snakeCells.addAll(snake.getTrace());
        }
        return snakeCells;
    }
}