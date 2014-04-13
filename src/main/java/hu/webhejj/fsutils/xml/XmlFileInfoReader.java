package hu.webhejj.fsutils.xml;

import hu.webhejj.commons.xml.SimpleXmlWalker;
import hu.webhejj.fsutils.FileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XmlFileInfoReader {
	
	public FileInfo read(File file) {
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			
			XmlFileInfoReaderVisitor visitor = new XmlFileInfoReaderVisitor();
			SimpleXmlWalker walker = new SimpleXmlWalker(fis, visitor);
			walker.walk();
			return visitor.getRoot();
			
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
}
