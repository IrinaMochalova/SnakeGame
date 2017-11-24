package ru.snake_game.controller;

import javafx.scene.input.KeyCode;
import ru.snake_game.controller.Interfaces.ISnakeController;
import ru.snake_game.model.Interfaces.IField;
import ru.snake_game.model.util.Vector;

import java.util.HashMap;

public class KeyboardSnakeController implements ISnakeController{
    private IField field;
    private Vector direction;
    private HashMap<KeyCode, Vector> keys;

    public KeyboardSnakeController(IField field, HashMap<KeyCode, Vector> keys, Vector initialDirection) {
        this.field = field;
        this.keys = keys;

        direction = initialDirection;
    }

    public void pressKey(KeyCode key) {
        if (keys.containsKey(key))
            direction = keys.get(key);
    }

    public Vector calculateDirection(Vector head) {
        return direction;
    }
}
