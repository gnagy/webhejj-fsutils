package hu.webhejj.fsutils;

import hu.webhejj.commons.CompareUtils;
import hu.webhejj.commons.collections.TreeIterator;
import hu.webhejj.commons.collections.TreeWalker.ChildProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileInfo implements Iterable<FileInfo>, Comparable<FileInfo> {
	
	private static ChildProvider<FileInfo> childProvider = new ChildProvider<FileInfo>() {
		@Override
		public Iterable<FileInfo> getChildren(FileInfo node) {
			return node.getChildren();
		}
	};
	
	private static Comparator<FileInfo> nameComparator = new Comparator<FileInfo>() {
		@Override
		public int compare(FileInfo o1, FileInfo o2) {
			return CompareUtils.compare(o1.getName(), o2.getName());
		}
	};
	
	private String path;
	private String name;
	private boolean isDirectory;
	private Date lastModified;
	private long size;
	private int childCount;
	private String contentType;
	private String sha1;
	
	private FileInfo parent;
	private List<FileInfo> children;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public FileInfo getParent() {
		return parent;
	}
	public void setParent(FileInfo parent) {
		this.parent = parent;
	}
	public List<FileInfo> getChildren() {
		return children;
	}
	public void setChildren(List<FileInfo> children) {
		this.children = children;
	}
	
	public void addChild(FileInfo child) {
		if(children == null) {
			children = new ArrayList<FileInfo>();
		}
		int pos = Collections.binarySearch(children, child, nameComparator);
		if(pos >= 0) {
			children.set(pos, child);
		} else {
			children.add(-pos - 1, child);
		}
		child.setParent(this);
	}
	
	public FileInfo getChild(String name) {
		if(children == null) {
			return null;
		}
		FileInfo child = new FileInfo();
		child.setName(name);
		int pos = Collections.binarySearch(children, child, nameComparator);
		if(pos < 0) {
			return null;
		}
		return children.get(pos);
	}
	
	@Override
	public String toString() {
		return path;
	}
	@Override
	public Iterator<FileInfo> iterator() {
		return new TreeIterator<FileInfo>(this, childProvider);
	}
	
	@Override
	public int compareTo(FileInfo other) {
		if(other == null) {
			return 1;
		}
		return CompareUtils.compare(this.getPath(), other.getPath());
	}
}
