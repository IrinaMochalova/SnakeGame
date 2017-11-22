package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

import java.util.HashSet;

public interface ISnake {
    Vector getDirection();
    void setDirection(Vector direction);

    void kill();
    void move();
    void eat(int growValue);

    boolean isAlive();

    Vector getHead();
    HashSet<Vector> getTrace();
}
