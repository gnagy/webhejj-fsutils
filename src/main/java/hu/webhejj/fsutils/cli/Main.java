package hu.webhejj.fsutils.cli;

import hu.webhejj.commons.PrintingProgressMonitor;
import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.fsutils.FileInfo;
import hu.webhejj.fsutils.actions.FileInfoDiffComparatorAction;
import hu.webhejj.fsutils.actions.FsScanAction;
import hu.webhejj.fsutils.actions.FsScrapeAction;
import hu.webhejj.fsutils.mime.SharedMimeInfoMimeTyper;
import hu.webhejj.fsutils.xml.CsvHashReader;
import hu.webhejj.fsutils.xml.UnixHashReader;
import hu.webhejj.fsutils.xml.UnixHashWriter;
import hu.webhejj.fsutils.xml.XmlFileInfoReader;
import hu.webhejj.fsutils.xml.XmlFileInfoWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		
		ProgressMonitor monitor = new PrintingProgressMonitor(System.out);
		
		SimpleArgs simpleArgs = new SimpleArgs(args);
		File output = simpleArgs.getFile("-o", "output.scan.xml");
		
		// scan
		if(simpleArgs.hasArg("-s")) {
			
			File dir = simpleArgs.getFile("-s", "./");
			if(!dir.exists()) {
				throw new FileNotFoundException("Scan directory doesn't exist: " + dir);
			}
			
			FsScanAction scanAction = new FsScanAction(dir);
			scanAction.perform(monitor);
			
			write(output, scanAction.getRootFileInfo());
			
		// scrape
		} else if(simpleArgs.hasArg("-S2")) {
			
			File dir = simpleArgs.getFile("-S2", "./");
			if(!dir.exists()) {
				throw new FileNotFoundException("Scrape directory doesn't exist: " + dir);
			}
			
			// preload mimeTyper
			SharedMimeInfoMimeTyper.getInstance();
			
			long time = System.currentTimeMillis();
			
			FsScanAction scanAction = new FsScanAction(dir);
			scanAction.perform(monitor);
			
			FsScrapeAction scrapeAction = new FsScrapeAction(dir, scanAction.getRootFileInfo());
			scrapeAction.perform(monitor);
			
			XmlFileInfoWriter writer = new XmlFileInfoWriter(new FileOutputStream(output));
			writer.write(scanAction.getRootFileInfo());
			
			writer.close();
			
			System.out.println("Scrape done in " + ((System.currentTimeMillis() - time) / 1024) + "s.");
		
		// convert
		} else if(simpleArgs.hasArg("-c")) {
		
			File file = simpleArgs.getFile("-s", null);
			write(output, read(file));
		
			
		// diff
		} else if(simpleArgs.hasArg("-d")) {
			
			int i = simpleArgs.getArgIndex("-d");
			if(args.length < i + 2) {
				throw new IllegalArgumentException("Diff parameters left and right must be specified");
			}
			
			File leftFile = new File(args[i + 1]);
			File rightFile = new File(args[i + 2]);
			
			FileInfo left = Main.read(leftFile);
			FileInfo right = Main.read(rightFile);
			
			FileInfoDiffComparatorAction diffAction = new FileInfoDiffComparatorAction(left, right);
			diffAction.perform(monitor);
			
//			for(Difference<FileInfo> diff: diffAction.getDifferences()) {
//				if(diff.getType() != Type.UNCHANGED) {
//					System.out.println(diff);
//				}
//			}
			
		} else {
			System.out.println("No arguments specified.");
			// TODO: help
		}
	}
	
	public static FileInfo read(File file) {
		
		if(file.getName().endsWith(".xml")) {
			return new XmlFileInfoReader().read(file);
		
		} else if(file.getName().endsWith(".md5") || file.getName().endsWith(".sha1")) {
			return new UnixHashReader().read(file);

		} else if(file.getName().endsWith(".csv")) {
			return new CsvHashReader().read(file);
		
		} else {
			throw new IllegalArgumentException("Unknown input file type: " + file);
		}
	}
	
	public static void write(File file, FileInfo fileInfo) throws IOException {
		
		if(file.getName().endsWith(".xml")) {
			XmlFileInfoWriter writer = new XmlFileInfoWriter(new FileOutputStream(file));
			writer.write(fileInfo);
			writer.close();
		
		} else if(file.getName().endsWith(".sha1.txt")) {
			UnixHashWriter writer = new UnixHashWriter(new FileOutputStream(file));
			writer.write(fileInfo);
			writer.close();
		}
	}

}
