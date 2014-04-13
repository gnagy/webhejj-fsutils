package hu.webhejj.fsutils.scrape;


import hu.webhejj.fsutils.FileInfo;

import java.util.HashMap;
import java.util.Map;

public class FileInfoHashDeduplicator implements Deduplicator<FileInfo> {

	private final Map<String, FileInfo> items = new HashMap<String, FileInfo>();
	
	@Override
	public FileInfo deduplicate(FileInfo item) {
		
		String hash = item.getSha1(); 
		FileInfo original = items.get(hash);
		
		if(original == null) {
			items.put(hash, item);
			return item; 
		}
		return original;
	}
}
