package ru.snake_game.controller;

import javafx.scene.input.KeyCode;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.model.Interfaces.IGame;
import ru.snake_game.model.util.Vector;

import java.util.HashMap;

public class KeyboardController implements IController {
    private IGame game;
    private KeyCode lastKey;
    private Vector direction;
    private HashMap<KeyCode, Vector> keys;

    public KeyboardController(
            IGame game,
            HashMap<KeyCode, Vector> keys,
            Vector initial) {
        this.keys = keys;
        this.game = game;

        direction = initial;
    }

    public void pressKey(KeyCode key) {
        lastKey = key;
    }

    public Vector calculateDirection(Vector head, Vector direction) {
        if (lastKey != null && keys.containsKey(lastKey)) {
            Vector newDirection = keys.get(lastKey);
            if (game.getAvailableDirections(head, direction).contains(newDirection))
                this.direction = newDirection;
        }
        return this.direction;
    }
}
