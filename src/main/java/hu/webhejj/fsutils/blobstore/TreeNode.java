package hu.webhejj.fsutils.blobstore;

import java.util.List;

public interface TreeNode {
    Id getId();
    List<TreeEntry> getEntries();
}
