package model;

import model.Interfaces.IField;
import model.Interfaces.IFieldObject;
import model.Interfaces.IGenerator;

public class Generator<T extends IFieldObject> implements IGenerator{
    private IField field;
    private Class<T> target;

    public Generator(Class<T> target, IField field) {
        this.field = field;
        this.target = target;
    }

    public void process() {
        int missing = field.getSnakes().size() - countObjects();
        for (int i = 0; i < missing; i++) {
            Vector emptyCell = field.findEmptyCell();
            try {
                IFieldObject object = target.getConstructor(Vector.class).newInstance(emptyCell);
                field.addObject(object);
            } catch (Exception ignored) {}
        }
    }

    private int countObjects() {
        int count = 0;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                IFieldObject object = field.getObjectAt(new Vector(x, y));
                if (object != null && object.getClass().isAssignableFrom(target))
                    count++;
            }
        }
        return count;
    }
}
