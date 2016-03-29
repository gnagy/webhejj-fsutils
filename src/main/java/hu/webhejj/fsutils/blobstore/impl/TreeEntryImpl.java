package hu.webhejj.fsutils.blobstore.impl;

import hu.webhejj.fsutils.blobstore.Id;
import hu.webhejj.fsutils.blobstore.TreeEntry;

public class TreeEntryImpl implements TreeEntry {

    private final Id id;
    private final Type type;
    private final String name;

    public TreeEntryImpl(Id id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
