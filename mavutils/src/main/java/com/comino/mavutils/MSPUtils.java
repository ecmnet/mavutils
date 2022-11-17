package com.comino.mavutils;

import java.text.DecimalFormat;

public class MSPUtils {

	private static MSPUtils instance=null;

	private final boolean       enable;
	private final DecimalFormat time_formatter;

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
		this.enable         = enable;
		this.time_formatter = new DecimalFormat("#0.00s");
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
	
	public String time(float t) {
		return time_formatter.format(t);
	}

}
