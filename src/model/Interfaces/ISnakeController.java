package model.Interfaces;

import model.Vector;

public interface ISnakeController {
    int length();
    boolean isAlive();

    Vector getHead();
    Vector getDirection();

    void kill();
    void grow(int value);
    void move();
    void updateDirection();
}
