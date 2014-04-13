package hu.webhejj.fsutils.xml;

import hu.webhejj.fsutils.FileInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UnixHashReader {

	public FileInfo read(File file) {
		
		BufferedReader fis = null;
		try {

			fis = new BufferedReader(new FileReader(file));
			
			String line = null;

			FileInfo root = new FileInfo();
			root.setPath(".");
			root.setName(".");
			
			while((line = fis.readLine()) != null) {
				
				FileInfo fileInfo = parseLine(line);
				addToRoot(root, fileInfo);
				
			}
			
			return root.getChildren().size() == 1 ? root.getChildren().get(0) : root;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
			
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	protected FileInfo parseLine(String line) {
		
		String[] fields = line.split("\\s+");
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setPath(fields[1]);
		fileInfo.setName(fields[1].substring(fields[1].lastIndexOf('/') + 1, fields[1].length()));
		fileInfo.setSha1(fields[0]);
		
		return fileInfo;
	}
	
	protected void addToRoot(FileInfo root, FileInfo fileInfo) {
		
		String[] path = fileInfo.getPath().split("/");
		StringBuilder pathBuf = new StringBuilder();

		FileInfo current = root;
		for(int i = 0; i < path.length - 1; i++) {
			
			String segment = path[i];
			if(pathBuf.length() == 0) {
				pathBuf.append(segment);
			} else {
				pathBuf.append("/");
				pathBuf.append(segment);
			}
			
			FileInfo child = current.getChild(segment);
			if(child == null) {
				child = new FileInfo();
				child.setPath(pathBuf.toString());
				child.setName(segment);
				child.setDirectory(true);
				current.addChild(child);
			}
			current = child;
		}

		current.addChild(fileInfo);
	}
}
