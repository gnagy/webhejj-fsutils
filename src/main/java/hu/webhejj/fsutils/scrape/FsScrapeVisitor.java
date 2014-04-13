package hu.webhejj.fsutils.scrape;

import hu.webhejj.commons.crypto.Hasher;
import hu.webhejj.commons.io.filter.ByteBufferHashTaker;
import hu.webhejj.commons.io.filter.ChannelFilterer;
import hu.webhejj.commons.text.StringUtils;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.mime.MimeTyper;

import java.io.File;
import java.io.FileNotFoundException;

public class FsScrapeVisitor extends FsScanVisitor {
	
	private final ByteBufferHashTaker md5Taker;
	private final ByteBufferHashTaker sha1Taker;
	private final ChannelFilterer hashFilterer;
	private final MimeTyper mimeTyper;
	
	public FsScrapeVisitor(File root, MimeTyper mimeTyper) {
		super(root);
		this.mimeTyper = mimeTyper;
		
		md5Taker = new ByteBufferHashTaker(new Hasher(Hasher.MD5));
		sha1Taker = new ByteBufferHashTaker(new Hasher(Hasher.SHA_1));
		hashFilterer = new ChannelFilterer(md5Taker, sha1Taker);
	}
	
	@Override
	public void scrape(File file, FileInfo item) {
		
		try {
			hashFilterer.filter(file, 1024);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error while scraping " + file, e);
		}
		String mimeType = getMimeType(file);
		
		item.setContentType(mimeType);
		// item.setMd5(StringUtils.encodeHex(md5Taker.getHash()));
		item.setSha1(StringUtils.encodeHex(sha1Taker.getHash()));
	}
	
	public ByteBufferHashTaker getMd5Taker() {
		return md5Taker;
	}
	
	public ByteBufferHashTaker getSha1Taker() {
		return sha1Taker;
	}
	
	public MimeTyper getMimeTyper() {
		return mimeTyper;
	}
	
	protected String getMimeType(File file) {
		return mimeTyper == null ? null :  mimeTyper.getMimeType(file);
	}

}
