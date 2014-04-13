package hu.webhejj.fsutils.xml;

import hu.webhejj.commons.collections.TreeVisitor;
import hu.webhejj.commons.xml.SimpleXmlWalker.Element;
import hu.webhejj.fsutils.FileInfo;

import java.util.Date;
import java.util.Stack;

public class XmlFileInfoReaderVisitor implements TreeVisitor<Element> {

	private final StringBuilder buf = new StringBuilder();
	private final Stack<FileInfo> infos = new Stack<FileInfo>();
	private FileInfo root;
	
	@Override
	public boolean entering(Element element) {
		
		if(buf.length() > 0) {
			buf.append("/");
		}
		
		buf.append(element.getAttribute(XmlFileInfoWriter.ATTR_NAME));
	
		FileInfo fileInfo = new FileInfo();
		fileInfo.setPath(buf.toString());
		fileInfo.setName(element.getAttribute(XmlFileInfoWriter.ATTR_NAME));
		fileInfo.setContentType(element.getAttribute(XmlFileInfoWriter.ATTR_MIMETYPE));
		fileInfo.setDirectory("folder".equals(element.getName()));
		fileInfo.setSha1(element.getAttribute(XmlFileInfoWriter.ATTR_HASH));
		if(element.hasAttribute(XmlFileInfoWriter.ATTR_MODIFIED)) {
			fileInfo.setLastModified(new Date(Long.parseLong(element.getAttribute(XmlFileInfoWriter.ATTR_MODIFIED))));
		}
		if(element.hasAttribute(XmlFileInfoWriter.ATTR_SIZE)) {
			fileInfo.setSize(Long.parseLong(element.getAttribute(XmlFileInfoWriter.ATTR_SIZE)));
		}
		if(element.hasAttribute(XmlFileInfoWriter.ATTR_CHILD_COUNT)) {
			fileInfo.setChildCount(Integer.parseInt(element.getAttribute(XmlFileInfoWriter.ATTR_CHILD_COUNT)));
		}
		
		if(root == null) {
			root = fileInfo;
		} else {
			infos.peek().addChild(fileInfo);
		}
		infos.push(fileInfo);
		
		return true;
	}

	@Override
	public void leaving(Element element) {
		int i = buf.lastIndexOf("/");
		buf.setLength(i < 0 ? 0 : i);
		infos.pop();
	}

	public FileInfo getRoot() {
		return root;
	};
}
