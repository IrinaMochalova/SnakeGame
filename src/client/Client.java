package client;

import model.Interfaces.IField;
import model.Vector;
import proto.Interfaces.IClient;
import proto.Packer;

public class Client extends Thread {
    private IClient client;
    private IField field;
    private Vector direction;
    private int timeOut;

    public Client(IClient client) throws Exception {
        this.client = client;
        waitForStart();
        start();
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public IField getField() {
        return field;
    }

    @Override
    public void run() {
        while (true) {
            if (client.hasMessage()) {
                field = Packer.unpackField(client.receive());
                // paint field
                sleep(timeOut);
                client.send(Packer.packVector(direction));
            }
            sleep(10);
        }
    }

    private void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        }
        catch (Exception ignored) {}
    }

    private void waitForStart(){
        while (!client.hasMessage()) {
            sleep(100);
        }
        timeOut = Packer.unpackInt(client.receive());
    }

}
