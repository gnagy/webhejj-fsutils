package hu.webhejj.fsutils.actions;

import hu.webhejj.commons.PrintingProgressMonitor;
import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.commons.files.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopyAction implements FsAction {

	private final File source;
	private final File target;
	
	
	public FileCopyAction(File source, File target) {
		this.source = source;
		this.target = target;
	}

	public void perform(ProgressMonitor monitor) {
		
		if(monitor.isCanceled()) {
			return;
		}
		
		if(!source.exists()) {
			throw new FsActionException("Source file doesn't exist: " + source);
		} else if (!source.isFile()) {
			throw new FsActionException("Source file is not a file " + source);
		}
		
		// make directories for target
		if(!target.exists()) {
			File targetParent = target.getParentFile();
			if(!targetParent.exists()) {
				if(!targetParent.mkdirs()) {
					throw new FsActionException("Error while creating " + targetParent);
				}
			}
		}
		
		FileChannel in = null;
		FileChannel out = null;
		
		try {
			
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(target).getChannel();
			
			long size = in.size();
			
			// make sure enough space is available
			long free = FileUtils.getFreeSpace(target);
			if(size > free) {
				throw new FsActionException("Not enough free space for " + target + ", need " + size + " but only have " + free);
			}
			
			// copy in cancellable chunks
			long chunks = size / 4096;
			if(size % 4096 > 0) {
				chunks++;
			}
			
			monitor.begin("Copying " + source + " to " + target, (int) chunks);
			int progress = 0;
			for(long i = 0; i < chunks; i++) {
				
				if(monitor.isCanceled()) {
					cleanup();
					return;
				}
				
				in.transferTo(i * 4096, 4096, out);
				monitor.progress(++progress);
			}
			
		} catch (IOException e) {
			cleanup();
			throw new FsActionException("Error while copying file " + source + " to " + target, e);
			
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	protected boolean cleanup() {
		return target.delete();
	}
	
	public static void main(String[] args) {
		FileCopyAction action = new FileCopyAction(new File("/tmp/amt3.log"), new File("/tmp/foo/bar/baz/copy"));
		action.perform(new PrintingProgressMonitor(System.out));
	}
}
