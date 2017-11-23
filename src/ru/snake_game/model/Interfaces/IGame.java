package ru.snake_game.model.Interfaces;

import ru.snake_game.model.util.Vector;

import java.util.ArrayList;

public interface IGame{
    void tick();

    IField getField();

    ArrayList<Vector> getAvailableDirections(int snakeNumber);
}
