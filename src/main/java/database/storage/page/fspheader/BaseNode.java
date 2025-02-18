package database.storage.page.fspheader;

import java.nio.ByteBuffer;

public class BaseNode {

    public static final int SIZE = 2 + Pointer.SIZE * 2;

    private short length;
    private Pointer first;
    private Pointer last;

    public BaseNode(short length, Pointer first, Pointer last) {
        this.length = length;
        this.first = first;
        this.last = last;
    }

    public static BaseNode deserialize(ByteBuffer buffer) {
        short length = buffer.getShort();
        Pointer first = Pointer.deserialize(buffer);
        Pointer last = Pointer.deserialize(buffer);

        return new BaseNode(length, first, last);
    }

    public void serialize(ByteBuffer buffer) {
        buffer.putShort(length);
        first.serialize(buffer);
        last.serialize(buffer);
    }

    public boolean isEmpty() {
        return first.isEmpty() && last.isEmpty();
    }

    public void increaseLength() {
        length++;
    }

    public void decreaseLength() {
        length--;
    }

    public short getLength() {
        return length;
    }

    public Pointer getFirst() {
        return first;
    }

    public Pointer getLast() {
        return last;
    }
}
