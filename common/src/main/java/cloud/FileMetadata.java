package cloud;

public class FileMetadata {

    private final int nameLen;
    private final String pathname;
    private final long fileLen;

    public FileMetadata(int nameLen, String pathname, long fileLen) {
        this.nameLen = nameLen;
        this.pathname = pathname;
        this.fileLen = fileLen;
    }

    public String getPathname() {
        return pathname;
    }

    public long getFileLen() {
        return fileLen;
    }

    public int getNameLen() {
        return nameLen;
    }
}
