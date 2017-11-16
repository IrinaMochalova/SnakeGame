package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

public interface IFieldObject
{
    Vector getLocation();

    void snakeInteract(ISnakeHead snake);

    void act();
}