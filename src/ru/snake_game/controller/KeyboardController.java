package ru.snake_game.controller;

import javafx.scene.input.KeyCode;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.util.Vector;

import java.util.HashMap;

public class KeyboardController implements IController {
    private KeyCode lastPressedKey;
    private HashMap<KeyCode, Vector> keys;

    public KeyboardController(HashMap<KeyCode, Vector> keys) {
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
