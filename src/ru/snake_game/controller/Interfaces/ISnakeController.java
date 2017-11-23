package ru.snake_game.controller.Interfaces;

import ru.snake_game.model.util.Vector;

public interface ISnakeController {
    Vector calculateDirection(Vector head);
}
