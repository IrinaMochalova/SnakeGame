package client.Interfaces;

import model.Interfaces.IField;

public interface IFieldProvider {
    IField getField();

    boolean isStarted();
    boolean isEnded();
}
