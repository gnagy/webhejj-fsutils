package hu.webhejj.fsutils.actions;

import hu.webhejj.commons.CompareUtils;
import hu.webhejj.commons.ProgressMonitor;
import hu.webhejj.commons.diff.DiffComparator;
import hu.webhejj.commons.diff.DiffPrinter;
import hu.webhejj.commons.diff.Difference;
import hu.webhejj.commons.diff.Difference.Type;
import hu.webhejj.commons.diff.SortedIterableDifferentiator;
import hu.webhejj.fsutils.FileInfo;

import java.util.List;

public class FileInfoDiffComparatorAction implements FsAction {

	private final FileInfo left;
	private final FileInfo right;
	private List<Difference<FileInfo>> diff;
	
	public FileInfoDiffComparatorAction(FileInfo left, FileInfo right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public void perform(ProgressMonitor monitor) {
		
		SortedIterableDifferentiator<FileInfo> differ = new SortedIterableDifferentiator<FileInfo>();
		
		DiffComparator<FileInfo> comparator = new DiffComparator<FileInfo>() {
			@Override
			public int compare(FileInfo info1, FileInfo info2) {
				return info1.compareTo(info2);
			}

			@Override
			public boolean equals(FileInfo info1, FileInfo info2) {
				return CompareUtils.isEqual(info1.getSha1(), info2.getSha1());
			}
		};
		
		monitor.begin("Calculating differences between " + left + " and " + right, 1);
		DiffPrinter<FileInfo> diffPrinter = new DiffPrinter<FileInfo>(Type.ADDED, Type.CHANGED, Type.DELETED);
		
		differ.diff(left, right, comparator, diffPrinter, monitor);
		monitor.done();
	}
	
	public List<Difference<FileInfo>> getDifferences() {
		return diff;
	}
}
