package hu.webhejj.fsutils.actions;

/** Generic exception caused by file system actions */
public class FsActionException extends RuntimeException {

	private static final long serialVersionUID = -4398543365068319951L;

	public FsActionException() {
	}

	public FsActionException(String message) {
		super(message);
	}

	public FsActionException(Throwable cause) {
		super(cause);
	}

	public FsActionException(String message, Throwable cause) {
		super(message, cause);
	}
}
