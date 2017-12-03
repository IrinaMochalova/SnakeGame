package proto;

import model.Direction;
import model.Interfaces.IField;
import model.Vector;

public final class Packer {
    public static char[] packField(IField field) {
        return new char[1];
    }

    public static char[] packInt(int value) {
        return Integer.toString(value).toCharArray();
    }

    public static Vector unpackVector(char[] message) {
        return new Vector(0, 0);
    }
}
