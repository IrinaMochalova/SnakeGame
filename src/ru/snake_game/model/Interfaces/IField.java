package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

public interface IField{
    int getWidth();
    int getHeight();

    IFieldObject getObjectAt(Vector location);
    void addObject(IFieldObject object);
    void removeAt(Vector location);

    int getSnakesCount();
    int addSnake(ISnake snake);
    ISnake getSnake(int number);

    public Vector findEmptyCell();
}