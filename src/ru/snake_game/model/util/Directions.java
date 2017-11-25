package ru.snake_game.model.util;

public class Directions {
    public final static Vector UP = new Vector(0, -1);
    public final static Vector DOWN = new Vector(0, 1);
    public final static Vector LEFT = new Vector(-1, 0);
    public final static Vector RIGHT = new Vector(1, 0);
    public final static Vector[] ALL = new Vector[] {UP, DOWN, LEFT, RIGHT};
}
