package ru.snake_game.FieldObjects;

import com.sun.istack.internal.NotNull;
import ru.snake_game.Interfaces.IField;
import ru.snake_game.Interfaces.ISnakeHead;
import ru.snake_game.util.Location;

public abstract class SnakePart extends AbstractSolidFieldObject {
    protected SnakeBody prev;
    protected IField field;

    protected SnakePart(@NotNull Location location, @NotNull SnakeBody prev, @NotNull IField field) {
        super(location);

        if (field == null)
            throw new IllegalArgumentException("field can't be null.");

        this.prev = prev;
        this.field = field;
    }

    protected abstract void move();

    protected void moveChild()
    {
        if (prev != null)
            prev.move();
        else
            field.setObjectAt(this.getLocation(), null);
    }

    @Override
    public void snakeInteract(ISnakeHead snakeHead) {
        if (prev != null || getHead().willGrow())
            snakeHead.kill();
    }

    protected abstract ISnakeHead getHead();
}
