package hu.webhejj.fsutils.actions;

import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.commons.crypto.Hasher;
import hu.webhejj.commons.io.filter.ByteBufferHashTaker;
import hu.webhejj.commons.io.filter.ChannelFilterer;
import hu.webhejj.commons.text.StringUtils;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.mime.SharedMimeInfoMimeTyper;

import java.io.File;
import java.io.FileNotFoundException;

public class FsScrapeAction implements FsAction {

	private final File root;
	private final FileInfo rootInfo;
	private final ChannelFilterer channelFilterer;
	private ByteBufferHashTaker hashTaker;
	private SharedMimeInfoMimeTyper mimeTyper;
	int progress = 0;
	
	public FsScrapeAction(File root, FileInfo rootInfo) {
		this.root = root;
		this.rootInfo = rootInfo;
		hashTaker = new ByteBufferHashTaker(new Hasher(Hasher.SHA_1));
		this.channelFilterer = new ChannelFilterer(hashTaker);
		mimeTyper = SharedMimeInfoMimeTyper.getInstance();
	}

	@Override
	public void perform(ProgressMonitor monitor) {
		
		monitor.begin(String.format("Scraping %d files", rootInfo.getChildCount()), (int) rootInfo.getChildCount() + 1);
		
		doScrape(rootInfo, monitor);
		
		monitor.done();
	}
	
	protected void doScrape(FileInfo info, ProgressMonitor monitor) {
		
		if(monitor.isCanceled()) {
			return;
		}
		
		if(!info.isDirectory()) {
			try {
				File file = new File(root, info.getPath());
				channelFilterer.filter(file, 4094);
				info.setSha1(StringUtils.encodeHex(hashTaker.getHash()));
				info.setContentType(mimeTyper.getMimeType(file));
				
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		
		monitor.progress(++progress);
		
		if(info.getChildren() != null) {
			for(FileInfo child: info.getChildren()) {
				doScrape(child, monitor);
			}
		}
	}
}
