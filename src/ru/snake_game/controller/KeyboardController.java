package ru.snake_game.controller;

import javafx.scene.input.KeyCode;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.util.Vector;

import java.util.HashMap;

public class KeyboardController implements IController {
    private IField field;
    private KeyCode lastKey;
    private Vector direction;
    private HashMap<KeyCode, Vector> keys;

    public KeyboardController(IField field, HashMap<KeyCode, Vector> keys, Vector initial) {
        this.keys = keys;
        this.field = field;
        direction = initial;
    }

    public void pressKey(KeyCode key) {
        lastKey = key;
    }

    public Vector calculateDirection(Vector head, Vector direction) {
        if (lastKey != null && keys.containsKey(lastKey)) {
            Vector newDirection = keys.get(lastKey);
            if (Vector.getScalarProduct(direction, newDirection) == 0)
                this.direction = newDirection;
        }
        return this.direction;
    }
}
