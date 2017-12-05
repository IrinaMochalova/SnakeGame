package proto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public enum Commands implements Serializable{
    GAME_START((byte)0x00),
    GAME_END((byte)0xFF);

    private byte value;

    Commands(byte value) {
        this.value = value;
    }

    private void readObject(ObjectInputStream input) throws IOException{
        value = input.readByte();
    }

    private void writeObject(ObjectOutputStream output) throws IOException{
        output.writeByte(value);
    }
}
