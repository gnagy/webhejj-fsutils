package hu.webhejj.fsutils.actions;

import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.commons.files.FsWalker;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.scrape.FsScanVisitor;

import java.io.File;

public class FsScanAction implements FsAction {

	private final File root;
	private FsScanVisitor visitor;
	
	public FsScanAction(File root) {
		this.root = root;
		visitor = new FsScanVisitor(root);
	}

	@Override
	public void perform(ProgressMonitor monitor) {
		
		FsWalker walker = new FsWalker(visitor, true);

		monitor.begin("Scanning " + root, 1);
		walker.walk(root, monitor);
		monitor.done();
	}
	
	public FileInfo getRootFileInfo() {
		return visitor.getRootItem();
	}
}
