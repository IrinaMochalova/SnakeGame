package ru.snake_game.model.util;

public class Vector {
    private int x, y;

    public Vector(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector add(Vector move)
    {
        return new Vector(x + move.getX(), y + move.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector)
        {
            Vector loc = (Vector)obj;
            return loc.x == x && loc.y == y;
        }
        return false;
    }
}
