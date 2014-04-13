package hu.webhejj.fsutils.actions;

import hu.webhejj.commons.ProgressMonitor;

public interface FsAction {

	void perform(ProgressMonitor monitor);
}
