package hu.webhejj.fsutils.xml;

import hu.webhejj.fsutils.FileInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class UnixHashWriter {
	
	private OutputStreamWriter writer;

	public UnixHashWriter(OutputStream os) throws IOException {
		this.writer = new OutputStreamWriter(os);
	}
	
	public void write(FileInfo fileInfo) throws IOException {
		
		if(fileInfo.isDirectory()) {
		
			for(FileInfo child: fileInfo.getChildren()) {
				write(child);
			}
		} else {
			writer.write(fileInfo.getSha1());
			writer.write("  ");
			writer.write(fileInfo.getPath());
			writer.write("\n");
		}
	}
	
	public void flush() throws IOException {
		writer.flush();
	}
	
	public void close() throws IOException {
		writer.close();
	}
}
