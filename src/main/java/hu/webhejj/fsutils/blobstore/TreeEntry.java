package hu.webhejj.fsutils.blobstore;

public interface TreeEntry {

    enum Type {
        BLOB,
        TREE
    }

    Id getId();
    Type getType();
    String getName();
}
