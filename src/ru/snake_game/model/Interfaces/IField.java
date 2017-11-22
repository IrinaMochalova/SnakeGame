package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

import java.util.HashSet;

public interface IField{
    int getWidth();
    int getHeight();

    IFieldObject getObjectAt(Vector location);
    void addObject(IFieldObject object);
    void removeObjectAt(Vector location);

    int getSnakesCount();
    int addSnake(ISnake snake);
    ISnake getSnake(int number);

    Vector findEmptyCell();
    HashSet<Vector> getAllSnakeCells();
}