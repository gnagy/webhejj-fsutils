package hu.webhejj.fsutils.cli;

import java.io.File;

public class SimpleArgs {
	
	private String[] args;
	
	public SimpleArgs(String[] args) {
		this.args = args;
	}

	public int getArgIndex(String Arg) {
		for(int i = 0; i < args.length; i++) {
			if(Arg.equals(args[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean hasArg(int i) {
		return i >=0 && i < args.length;
	}
	
	public boolean hasArg(String Arg) {
		return getArgIndex( Arg) >= 0;
	}
	
	public String getArg(int i) {
		return hasArg(i) ? args[i] : null;
	}
	
	public String getParameterForArg(String Arg) {
		int i = getArgIndex(Arg);
		if(i < 0 || i + 1 >= args.length) {
			return null;
		}
		return args[i + 1];
	}
	
	public File getFile(String option, String defaultValue) {
		String arg = getParameterForArg(option);
		return arg != null ? new File(arg) : new File(defaultValue);
	}
}
