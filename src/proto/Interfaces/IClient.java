package proto.Interfaces;

import java.io.Serializable;

public interface IClient {
    <TObject extends Serializable> TObject receive();
    <TObject extends Serializable> void send(TObject object);

    boolean hasMessage();
}
