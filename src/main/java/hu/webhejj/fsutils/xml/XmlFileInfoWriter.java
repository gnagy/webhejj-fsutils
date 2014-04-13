package hu.webhejj.fsutils.xml;

import hu.webhejj.commons.xml.SimpleXmlWriter;
import hu.webhejj.fsutils.FileInfo;

import java.io.IOException;
import java.io.OutputStream;

public class XmlFileInfoWriter {

	public static final String ELEMENT_FOLDER = "folder";
	public static final String ELEMENT_FILE = "file";
	
	public static final String ATTR_NAME = "name";
	public static final String ATTR_MODIFIED = "modified";
	public static final String ATTR_MIMETYPE="mimetype";
	public static final String ATTR_SIZE = "size";
	public static final String ATTR_CHILD_COUNT = "children";
	public static final String ATTR_HASH = "hash";
	
	private SimpleXmlWriter writer;

	public XmlFileInfoWriter(OutputStream os) throws IOException {
		this.writer = new SimpleXmlWriter(os);
	}
	
	public void write(FileInfo file) throws IOException {
		
		if(file.isDirectory()) {
            writer.startElement(ELEMENT_FOLDER);
        } else {
            writer.startElement(ELEMENT_FILE);
        }
        writer.writeAttribute(ATTR_NAME, file.getName());
        writer.writeAttribute(ATTR_MODIFIED, Long.toString(file.getLastModified() == null ? 0 : file.getLastModified().getTime()));
        writer.writeAttribute(ATTR_SIZE, Long.toString(file.getSize()));
        if(file.isDirectory()) {
            writer.writeAttribute(ATTR_CHILD_COUNT, Integer.toString(file.getChildCount()));
        }

        if(file.getSha1() != null) {
            writer.writeAttribute(ATTR_HASH, file.getSha1());
        }
        if(file.getContentType() != null) {
            writer.writeAttribute(ATTR_MIMETYPE, file.getContentType());
        }

        if(file.getChildren() != null) {
            for(FileInfo child: file.getChildren()) {
                write(child);
            }
        }
			
        writer.endElement();
	}
	
	public void flush() throws IOException {
		writer.flush();
	}
	
	public void close() throws IOException {
		writer.close();
	}
}
