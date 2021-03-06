package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Vector implements Serializable
{
    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vector
                && x == ((Vector)obj).x
                && y == ((Vector)obj).y;
    }

    @Override
    public int hashCode() {
        return x ^ y;
    }

    public Vector clone() {
        return new Vector(this.x, this.y);
    }

    public Vector add(Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    public Vector subtract(Vector vector){
        return new Vector(this.x - vector.x, this.y - vector.y);
    }

    public Vector multiply(int value) {
        return new Vector(this.x * value, this.y * value);
    }

    public Vector bound(Vector vector) {
        return new Vector((vector.x + this.x) % vector.x, (vector.y + this.y) % vector.y);
    }

    public static int getScalarProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static double getDistance(Vector v1, Vector v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
    }

    public static double getManhattanDistance(Vector v1, Vector v2) {
        return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
    }

    public static Vector parse(String text, String delimeter) {
        String[] parts = text.split(delimeter);
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new Vector(x, y);
    }

    private void readObject(ObjectInputStream input) throws IOException {
        x = input.readInt();
        y = input.readInt();
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.writeInt(x);
        output.writeInt(y);
    }
}
