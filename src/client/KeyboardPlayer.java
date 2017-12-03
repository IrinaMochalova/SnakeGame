package client;

import javafx.scene.input.KeyCode;
import model.Interfaces.IPlayer;
import model.Vector;

import java.util.HashMap;

public class KeyboardPlayer implements IPlayer{
    private KeyCode lastPressedKey;
    private HashMap<KeyCode, Vector> keys;

    public KeyboardPlayer(HashMap<KeyCode, Vector> keys) {
        this.keys = keys;
    }

    public void pressKey(KeyCode key) {
        lastPressedKey = key;
    }

    public Vector getDirection(Vector head, Vector direction) {
        if (lastPressedKey != null && keys.containsKey(lastPressedKey)) {
            Vector newDirection = keys.get(lastPressedKey);
            if (Vector.getScalarProduct(direction, newDirection) == 0)
                direction = newDirection;
        }
        return direction;
    }
}