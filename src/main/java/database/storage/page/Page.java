package database.storage.page;

import java.nio.ByteBuffer;

public class Page {

    public static final short PAGE_SIZE = 16 * 1024;

    private final FileHeader fileHeader;
    private final PageHeader pageHeader;
    private final UserRecords userRecords;
    private short freeSpace;

    private Page(FileHeader fileHeader, PageHeader pageHeader, UserRecords userRecords, short freeSpace) {
        this.fileHeader = fileHeader;
        this.pageHeader = pageHeader;
        this.userRecords = userRecords;
        this.freeSpace = freeSpace;
    }

    public static Page createIndex(int pageNumber, int prevPageNumber, int nextPageNumber, short pageLevel) {
        FileHeader fileHeader = FileHeader.create(pageNumber, PageType.INDEX, prevPageNumber, nextPageNumber);
        PageHeader pageHeader = PageHeader.create(pageLevel, (short) 0, (short) 0, false);
        UserRecords userRecords = UserRecords.empty();
        short freeSpace = calculateFreeSpace();

        return new Page(fileHeader, pageHeader, userRecords, freeSpace);
    }

    public static Page deserialize(ByteBuffer buffer) {
        FileHeader fileHeader = FileHeader.deserialize(buffer);
        PageHeader pageHeader = PageHeader.deserialize(buffer);
        UserRecords userRecords = UserRecords.deserialize(buffer, pageHeader.getRecordCount());
        short freeSpace = buffer.getShort();

        return new Page(fileHeader, pageHeader, userRecords, freeSpace);
    }

    private static short calculateFreeSpace() {
        return Page.PAGE_SIZE - FileHeader.HEADER_SIZE - PageHeader.HEADER_SIZE - Short.BYTES;
    }

    public void serialize(ByteBuffer buffer) {
        fileHeader.serialize(buffer);
        pageHeader.serialize(buffer);
        userRecords.serialize(buffer);
        buffer.putShort(freeSpace);
    }

    public void makeClean() {
        pageHeader.makeClean();
    }

    public void makeDirty() {
        pageHeader.makeDirty();
    }

    public boolean isDirty() {
        return pageHeader.isDirty();
    }

    public FileHeader getFileHeader() {
        return fileHeader;
    }

    public PageHeader getPageHeader() {
        return pageHeader;
    }

    public UserRecords getUserRecords() {
        return userRecords;
    }

    public short getFreeSpace() {
        return freeSpace;
    }

    public int getPageNumber() {
        return fileHeader.getPageNumber();
    }
}
