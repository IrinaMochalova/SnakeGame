package proto;

import java.io.*;

public final class Packer {
    public static <T extends Serializable> byte[] pack(T object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(object);
        objectStream.close();
        return byteStream.toByteArray();
    }

    public static <T extends Serializable> T unpack(byte[] bytes) throws ClassNotFoundException, IOException{
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        Object object = objectStream.readObject();
        objectStream.close();
        return (T)object;
    }
}
