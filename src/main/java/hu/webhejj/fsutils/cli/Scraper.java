package hu.webhejj.fsutils.cli;

import hu.webhejj.commons.PrintingProgressMonitor;
import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.fsutils.actions.FsScanAction;
import hu.webhejj.fsutils.actions.FsScrapeAction;
import hu.webhejj.fsutils.mime.SharedMimeInfoMimeTyper;
import hu.webhejj.fsutils.xml.XmlFileInfoWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Scraper {

	public static void main(String[] args) throws IOException {
		
		File scanDir = args.length > 0 ? new File(args[0]) : new File("./");
		File output = args.length > 1 ? new File(args[1]) : new File("output.scan.xml");
		
		if(!scanDir.exists()) {
			System.err.println("Error: does not exist: " + scanDir);
			System.exit(-1);
		}
//		if(output.exists()) {
//			System.err.println("Error: output already exists: " + output);
//			System.exit(2);
//		}
		
		// preload mimeTyper
		SharedMimeInfoMimeTyper.getInstance();
		
		System.out.println("Scanning " + scanDir + " and writing to " + output);
		
		long time = System.currentTimeMillis();
		
		ProgressMonitor monitor = new PrintingProgressMonitor(System.out);
		FsScanAction scanAction = new FsScanAction(scanDir);
		scanAction.perform(monitor);
		
		FsScrapeAction scrapeAction = new FsScrapeAction(scanDir, scanAction.getRootFileInfo());
		scrapeAction.perform(monitor);
		
		XmlFileInfoWriter writer = new XmlFileInfoWriter(new FileOutputStream(output));
		writer.write(scanAction.getRootFileInfo());
		
		writer.close();
		
		System.out.println("Done in " + ((System.currentTimeMillis() - time) / 1024) + "s.");
		
//		for(FileInfo fileInfo: scanAction.getRootFileInfo()) {
//			System.out.println(fileInfo);
//		}
	}
}
