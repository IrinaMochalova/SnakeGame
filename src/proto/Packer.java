package proto;

import model.Interfaces.IField;
import model.Vector;
import server.FieldMakers;

public final class Packer {
    public static char[] packField(IField field) {
        return new char[1];
    }

    public static IField unpackField(char[] message) {
        return FieldMakers.makeSquaredField(10);
    }

    public static char[] packInt(int value) {
        return Integer.toString(value).toCharArray();
    }

    public static int unpackInt(char[] message) {
        return 1000;
    }

    public static Vector unpackVector(char[] message) {
        return new Vector(0, 0);
    }

    public static char[] packVector(Vector vector) {
        return new char[1];
    }
}
