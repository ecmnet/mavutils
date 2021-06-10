package com.comino.mavutils.hw.jetson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import com.comino.mavutils.legacy.ExecutorService;
import com.comino.mavutils.workqueue.WorkQueue;


public class JetsonLEDControl {

	public static final int GREEN  = 0;
	public static final int YELLOW = 2;
	public static final int RED    = 3;
	public static final int ORANGE = 4;

	private static PrintStream yellow;
	private static PrintStream green;
	private static PrintStream red;

	static {

		try {

			yellow = new PrintStream(new FileOutputStream("/sys/class/leds/upboard:yellow:/brightness"));
			green  = new PrintStream(new FileOutputStream("/sys/class/leds/upboard:green:/brightness"));
			red    = new PrintStream(new FileOutputStream("/sys/class/leds/upboard:red:/brightness"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void setLED(int led, boolean onoff) {
		try {
		  switch(led) {
			case GREEN:
				if(onoff) green.print("1"); else green.print("0");
				break;
			case RED:
				if(onoff) red.print("1"); else red.print("0");
				break;
			case YELLOW:
				if(onoff) yellow.print("1"); else yellow.print("0");
				break;
			case ORANGE:
				if(onoff) { yellow.print("1"); red.print("1"); } else { yellow.print("0"); red.print("0"); }
				break;
			}
		} catch(Exception e) { e.printStackTrace(); }
	}

	public static void flash(int led, int time_ms) {
		setLED(led,true);
		WorkQueue.getInstance().addSingleTask("LP", time_ms, () -> { setLED(led,false); });
	}

	public static void clear() {
		setLED(GREEN, false);  setLED(YELLOW, false); setLED(RED, false);
	}
}
