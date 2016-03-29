package hu.webhejj.fsutils.blobstore.impl;

import hu.webhejj.commons.crypto.Hasher;
import hu.webhejj.commons.text.StringUtils;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.blobstore.BlobStore;
import hu.webhejj.fsutils.blobstore.Id;
import hu.webhejj.fsutils.blobstore.TreeEntry;
import hu.webhejj.fsutils.blobstore.TreeNode;
import hu.webhejj.fsutils.cli.Scraper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBlobStore implements BlobStore {

    private final File baseDirectory;
    private final Map<Id, TreeNode> nodes = new HashMap<>();

    public FileBlobStore(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public TreeNode getNode(Id id) {
        return nodes.get(id);
    }

    @Override
    public TreeNode getRootNode() {
        Id id = new StringId("/");
        TreeNode treeNode = new TreeNodeImpl(id, null);
        return treeNode;
    }

    protected TreeNode scrape(String path) {
        FileInfo fileInfo = Scraper.scrape(new File(baseDirectory, path));
        return toTreeNode(fileInfo);
    }

    protected TreeNode toTreeNode(FileInfo fileInfo) {

        if(fileInfo == null) {
            return null;
        }

        TreeNode treeNode = null;
        if(fileInfo.isDirectory()) {
            List<TreeEntry> treeEntries = new ArrayList<>(fileInfo.getChildCount());
            for(FileInfo childInfo: fileInfo.getChildren()) {
                treeEntries.add(toTreeEntry(childInfo));
            }

            Id id = getId(treeEntries);
            treeNode = new TreeNodeImpl(id, Collections.unmodifiableList(treeEntries));

        } else {
            Id id = new StringId(fileInfo.getSha1());
            treeNode =  new TreeNodeImpl(id, null);
        }
        storeNode(treeNode, fileInfo);
        return treeNode;
    }

    protected TreeEntry toTreeEntry(FileInfo fileInfo) {
        if(fileInfo.isDirectory()) {
            TreeNode treeNode = toTreeNode(fileInfo);
            return new TreeEntryImpl(treeNode.getId(), TreeEntry.Type.TREE, fileInfo.getName());
        } else {
            Id id = new StringId(fileInfo.getSha1());
            return new TreeEntryImpl(id, TreeEntry.Type.BLOB, fileInfo.getName());
        }
    }

    protected Id getId(List<TreeEntry> treeEntries) {
        AppendableHasher hasher = new AppendableHasher(new Hasher(Hasher.SHA_1));
        for(TreeEntry treeEntry: treeEntries) {
            hasher.append(treeEntry.getId().toString());
            hasher.append("    ");
            hasher.append(treeEntry.getName());
            hasher.append("\n");
        }
        byte[] digest = hasher.getHash();
        return new StringId(StringUtils.encodeHex(digest));
    }

    protected boolean storeNode(TreeNode treeNode, FileInfo fileInfo) {
        if(nodes.containsKey(treeNode.getId())) {
            return false;
        }
        nodes.put(treeNode.getId(), treeNode);
        return true;
    }
}
