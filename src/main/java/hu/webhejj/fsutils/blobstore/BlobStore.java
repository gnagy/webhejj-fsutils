package hu.webhejj.fsutils.blobstore;

public interface BlobStore {

    TreeNode getNode(Id id);
    TreeNode getRootNode();
}
