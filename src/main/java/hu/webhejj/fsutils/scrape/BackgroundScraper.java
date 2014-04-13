package hu.webhejj.fsutils.scrape;

import hu.webhejj.commons.files.FsWalker;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.mime.SharedMimeInfoMimeTyper;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class BackgroundScraper {
	
	public class RunnableScrape implements Runnable {
		
		FsScrapeVisitor visitor = new FsScrapeVisitor(root, SharedMimeInfoMimeTyper.getInstance());

		@Override
		public void run() {
			while(!finished) {
				try {
					FileInfo fileItem = queue.take();
					visitor.scrape(root, fileItem);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	private final File root;
	private final int threads;
	private final ArrayBlockingQueue<FileInfo> queue;

	private boolean finished = true;
	
	public BackgroundScraper(File root, int threads) {
		this.root = root;
		this.threads = threads;
		queue = new ArrayBlockingQueue<FileInfo>(1024);
	}
	
	/** Method for scanning for files. Call this from one thread */
	public void scan() {
		finished = false;
		FsScanVisitor visitor = new FsScanVisitor(root) {
			@Override
			protected void onNewItem(FileInfo item) {
				queue.add(item);
			}
		};
		FsWalker walker = new FsWalker(visitor, true);
		walker.walk(root);
		finished = true;
	}
	
	/** Method for scraping file details. Call this from the other thread */
	public void scrape() {
		
	
	}
	
	/**
	 * Scan in current thread and scrape in a background thread
	 * started from this method
	 */
	public void scanAndScrapeInBg() {
		
		ExecutorService ex = Executors.newFixedThreadPool(threads);
		Future<?> future = ex.submit(new RunnableScrape());

		scan();
		
		try {
			future.get();
		} catch (InterruptedException e) {
			finished = true;
		} catch (ExecutionException e) {
			finished = true;
		}
	}
}
