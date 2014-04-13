package hu.webhejj.fsutils.cli;

import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.xml.XmlFileInfoReader;

import java.io.File;
import java.io.IOException;

public class Dumper {
	
	public static void main(String[] args) throws IOException {
		
		if(args.length > 0) {
			XmlFileInfoReader reader = new XmlFileInfoReader();
			for(FileInfo info: reader.read(new File(args[0]))) {
				if(!info.isDirectory()) {
					System.out.format("%s  %s\n", info.getPath(), info.getSha1());
				}
			}
		}
	}
}