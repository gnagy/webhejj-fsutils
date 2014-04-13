package hu.webhejj.fsutils.xml;

import hu.webhejj.fsutils.FileInfo;

import java.io.IOException;

import au.com.bytecode.opencsv.CSVParser;

public class CsvHashReader extends UnixHashReader {

	CSVParser csvParser = new CSVParser();
	
	@Override
	protected FileInfo parseLine(String line) {
		
		FileInfo fileInfo = new FileInfo();
		
		String[] fields;
		try {
			fields = csvParser.parseLine(line);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		fileInfo.setPath(fields[0]);
		fileInfo.setName(fileInfo.getPath().substring(fileInfo.getPath().lastIndexOf('/') + 1, fileInfo.getPath().length()));
		fileInfo.setSha1(fields[3]);
		
		return fileInfo;
	}
}
