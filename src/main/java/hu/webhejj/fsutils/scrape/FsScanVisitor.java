package hu.webhejj.fsutils.scrape;

import hu.webhejj.commons.collections.TreeVisitor;
import hu.webhejj.fsutils.FileInfo;

import java.io.File;
import java.util.Date;
import java.util.Stack;

public class FsScanVisitor implements TreeVisitor<File>  {

	private final File root;
	private FileInfo rootInfo;
	private Stack<FileInfo> fileInfos;
	
	public FsScanVisitor(File root) {
		this.root = root;
		fileInfos = new Stack<FileInfo>();
	}

	@Override
	public boolean entering(File file) {
		
		FileInfo item = fromFile(file);
		scrape(file, item);

		if(rootInfo == null) {
			rootInfo = item;
		} else {
			fileInfos.peek().addChild(item);
		}
		fileInfos.push(item);
		
		return true;
	}
	
	@Override
	public void leaving(File node) {
		FileInfo fileInfo = fileInfos.pop();
		if(fileInfo.isDirectory() && fileInfo.getChildren() != null) {
			long size = 0;
			int childCount = 0;
			for(FileInfo child: fileInfo.getChildren()) {
				size += child.getSize();
				if(child.isDirectory()) {
					childCount += child.getChildCount() + 1;
				} else {
					childCount++;
				}
			}
			fileInfo.setSize(size);
			fileInfo.setChildCount(childCount);
		}
	}
	
	protected void scrape(File file, FileInfo Item) {
		// do nothing
	}
	
	
	protected String getKey(File file) {
		// make path relative to the root
		return file.getAbsolutePath().replace(root.getAbsolutePath(), "");
	}
	
	/** Template method for when a new item is scanned */
	protected void onNewItem(FileInfo item) {
		// do nothing
	}
	
	public FileInfo getRootItem() {
		return rootInfo;
	}
	
	public FileInfo fromFile(File file) {
		
		FileInfo item = new FileInfo();
		
		item.setPath(getKey(file));
		item.setName(file.getName());
		item.setDirectory(file.isDirectory());
		item.setLastModified(new Date(file.lastModified()));
		item.setSize(file.length());
		
		return item;
	}	
}
