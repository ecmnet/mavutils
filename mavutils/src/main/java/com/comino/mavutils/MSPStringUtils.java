package com.comino.mavutils;

import java.text.DecimalFormat;

public class MSPStringUtils {

	private static MSPStringUtils instance=null;

	private final boolean       enable;
	private final DecimalFormat time_formatter;
	private final DecimalFormat position_formatter;
	private final DecimalFormat velocity_formatter;
	private final DecimalFormat radians_formatter;
	private final DecimalFormat acceleration_formatter;

	public static MSPStringUtils getInstance(boolean enable) {
		if(instance==null)
			instance = new MSPStringUtils(enable);
		return instance;
	}

	public static MSPStringUtils getInstance() {
		if(instance==null)
			instance = new MSPStringUtils(false);
		return instance;
	}

	private MSPStringUtils(boolean enable) {
		this.enable                  = enable;
		this.time_formatter          = new DecimalFormat("#0.00s");
		this.position_formatter      = new DecimalFormat("#0.000m");
		this.velocity_formatter      = new DecimalFormat("#0.0m/s");
		this.acceleration_formatter  = new DecimalFormat("#0.0m/s2");
		this.radians_formatter       = new DecimalFormat("#0.0°");
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
	
	public String t_format(float t) {
		return time_formatter.format(t);
	}
	
	public String p_format(float t) {
		return position_formatter.format(t);
	}
	
	public String v_format(float t) {
		return velocity_formatter.format(t);
	}
	
	public String a_format(float t) {
		return acceleration_formatter.format(t);
	}
	
	public String r_format(float t) {
		return radians_formatter.format(MSPMathUtils.fromRad(t));
	}

}
