package model;

import model.Interfaces.IField;
import model.Interfaces.IFieldObject;
import model.Interfaces.ISnakeController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Random;

public class Field implements IField {
    private IFieldObject[][] field;
    private HashSet<ISnakeController> snakes;

    public Field(int width, int height) {
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
        Vector size = new Vector(getWidth(), getHeight());
        int index = (new Random()).nextInt(size.getX() * size.getY());
        int iterator = 0;
        while (iterator <= index) {
            Vector location = new Vector(iterator % size.getX(), iterator / size.getX()).bound(size);
            if (getObjectAt(location) != null)
                index++;
            if (iterator == index)
                return location;
            iterator++;
        }
        throw new IndexOutOfBoundsException("Field does not contain any free cell.");
    }

    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
        field = (IFieldObject[][])input.readObject();
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.writeObject(field);
    }
}
