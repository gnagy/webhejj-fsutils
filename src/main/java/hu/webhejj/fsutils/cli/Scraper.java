package hu.webhejj.fsutils.cli;

import hu.webhejj.commons.PrintingProgressMonitor;
import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.actions.FsScanAction;
import hu.webhejj.fsutils.actions.FsScrapeAction;
import hu.webhejj.fsutils.mime.SharedMimeInfoMimeTyper;
import hu.webhejj.fsutils.xml.XmlFileInfoWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Scraper {

    public static FileInfo scrape(File file) {
        ProgressMonitor monitor = new PrintingProgressMonitor(System.out);
        FsScanAction scanAction = new FsScanAction(file);
        scanAction.perform(monitor);

        FsScrapeAction scrapeAction = new FsScrapeAction(file, scanAction.getRootFileInfo());
        scrapeAction.perform(monitor);
        return scanAction.getRootFileInfo();
    }

    public static void scrapeToFile(File scanDir, File outputFile) throws IOException {

        if(!scanDir.exists()) {
            System.err.println("Error: does not exist: " + scanDir);
            System.exit(-1);
        }

        FileInfo fileInfo = scrape(scanDir);

        XmlFileInfoWriter writer = new XmlFileInfoWriter(new FileOutputStream(outputFile));
        writer.write(fileInfo);

        writer.close();
    }

	public static void main(String[] args) throws IOException {
		
		File scanDir = args.length > 0 ? new File(args[0]) : new File("./");
		File outputFile = args.length > 1 ? new File(args[1]) : new File("output.scan.xml");
		
		// preload mimeTyper
		SharedMimeInfoMimeTyper.getInstance();
		
		System.out.println("Scanning " + scanDir + " and writing to " + outputFile);
		long time = System.currentTimeMillis();

        scrapeToFile(scanDir, outputFile);

		System.out.println("Done in " + ((System.currentTimeMillis() - time) / 1024) + "s.");
	}
}
