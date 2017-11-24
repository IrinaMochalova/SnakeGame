package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

import java.util.HashSet;

public interface ISnake {
    int length();

    void kill();
    void move();
    void eat(int foodValue);

    boolean isAlive();

    Vector getHead();
    HashSet<Vector> getTrace();
}
