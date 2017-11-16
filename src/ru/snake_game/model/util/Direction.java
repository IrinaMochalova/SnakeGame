package ru.snake_game.model.util;

@SuppressWarnings("InstanceVariableNamingConvention")
public class Direction {
    public final static Vector NONE = new Vector(0, 0);
    public final static Vector UP = new Vector(0, -1);
    public final static Vector DOWN = new Vector(0, 1);
    public final static Vector LEFT = new Vector(-1, 0);
    public final static Vector RIGHT = new Vector(1, 0);

    public static Vector parse(String text)
    {
        try
        {
            return (Vector)Direction.class.getField(text.toUpperCase()).get(null);
        }
        catch (Exception ex)
        {
            return Direction.NONE;
        }
    }
}
