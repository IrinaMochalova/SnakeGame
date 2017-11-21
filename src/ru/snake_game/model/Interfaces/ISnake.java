package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

public interface ISnake {
    Vector getDirection();
    void setDirection(Vector direction);

    void kill();
    void move();
    void eat(int growValue);

    int length();
    boolean isAlive();
    boolean willGrow();

    Vector getHead();
    Vector[] getTrace();
}
