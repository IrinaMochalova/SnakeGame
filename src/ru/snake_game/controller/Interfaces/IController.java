package ru.snake_game.controller.Interfaces;

import ru.snake_game.model.Field;
import ru.snake_game.model.util.Vector;

public interface IController {
    Vector getDirection(Vector head, Vector direction);
}
