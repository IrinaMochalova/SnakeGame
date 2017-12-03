package model.Interfaces;

import model.Vector;

import java.util.HashSet;

public interface IField {
    int getWidth();
    int getHeight();

    IFieldObject getObjectAt(Vector location);
    void addObject(IFieldObject object);
    void removeObjectAt(Vector location);

    void addSnake(ISnakeController controller);
    void removeSnake(ISnakeController controller);
    HashSet<ISnakeController> getSnakes();

    Vector findEmptyCell();
}