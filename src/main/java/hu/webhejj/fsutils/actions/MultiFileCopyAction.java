package hu.webhejj.fsutils.actions;

import hu.webhejj.commons.ProgressMonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiFileCopyAction implements FsAction {

	private final List<FileCopyAction> delegates;
	
	public MultiFileCopyAction(List<FileCopyAction> delegates) {
		this.delegates = delegates;
	}
	
	public MultiFileCopyAction(Iterable<File> sources, File target) {
		if(sources instanceof Collection) {
			delegates = new ArrayList<FileCopyAction>(((Collection<?>) sources).size());
		} else {
			delegates = new ArrayList<FileCopyAction>();
		}
		
		for(File source: sources) {
			delegates.add(new FileCopyAction(source, new File(target, source.getName())));
		}
	}

	@Override
	public void perform(ProgressMonitor monitor) {
		
		if(monitor.isCanceled()) {
			return;
		}
		
		monitor.begin("Copying " + delegates.size() + " files", delegates.size());
		int i = 0;
		for(FileCopyAction action: delegates) {
			action.perform(monitor);
			if(monitor.isCanceled()) {
				return;
			}
			monitor.progress(++i);
		}
	}
}
