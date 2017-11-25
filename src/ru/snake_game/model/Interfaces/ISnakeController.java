package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

public interface ISnakeController {
    int length();
    boolean isAlive();

    Vector getHead();
    Vector getDirection();

    void kill();
    void grow(int value);
    void move();

    boolean hasLocation(Vector location);
}
