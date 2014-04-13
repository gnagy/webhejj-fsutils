package hu.webhejj.fsutils.mime;

import hu.webhejj.org.freedeskop.sharedmimeinfo.SharedMimeInfo;
import hu.webhejj.org.freedeskop.sharedmimeinfo.match.GlobMatcherEntry;

import java.io.File;
import java.util.List;

public class SharedMimeInfoMimeTyper implements MimeTyper {

	private static SharedMimeInfoMimeTyper instance;
	public static SharedMimeInfoMimeTyper getInstance() {
		if(instance == null) {
			synchronized (SharedMimeInfoMimeTyper.class) {
				if(instance == null) {
					instance =  new SharedMimeInfoMimeTyper();
				}
			}
		}
		return instance;
	}
	
	private SharedMimeInfo mimeInfo;
	
	protected SharedMimeInfoMimeTyper() {
		mimeInfo = SharedMimeInfo.create(SharedMimeInfoMimeTyper.class.getResourceAsStream("freedesktop.org.xml"));
	}

	@Override
	public String getMimeType(File file) {
		
		if(file.isDirectory()) {
			return "inode/directory";
		}
		List<GlobMatcherEntry> matches = mimeInfo.matchGlob(file);
		return matches.size() == 0 ? null : matches.get(0).getMimeType().getName();
	}
}
