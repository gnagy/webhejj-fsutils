package hu.webhejj.fsutils.blobstore.impl;

import hu.webhejj.fsutils.blobstore.Id;
import hu.webhejj.fsutils.blobstore.TreeEntry;
import hu.webhejj.fsutils.blobstore.TreeNode;

import java.util.List;

public class TreeNodeImpl implements TreeNode {

    private final Id id;
    private final List<TreeEntry> entries;

    public TreeNodeImpl(Id id, List<TreeEntry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public Id getId() {
        return id;
    }
    public List<TreeEntry> getEntries() {
        return entries;
    }
}
