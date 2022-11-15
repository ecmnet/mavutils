package com.comino.mavutils;

public class MSPUtils {

	private static MSPUtils instance=null;

	private boolean enable = false;

	public static MSPUtils getInstance(boolean enable) {
		if(instance==null)
			instance = new MSPUtils(enable);
		return instance;
	}

	public static MSPUtils getInstance() {
		if(instance==null)
			instance = new MSPUtils(false);
		return instance;
	}

	private MSPUtils(boolean enable) {
		this.enable = enable;
	}

	public void out(Object x) {
		if(!enable)
			return;
		System.out.println(x);
	}

	public void err(Object x) {
		if(!enable)
			return;
		System.err.println(x);
	}

}
