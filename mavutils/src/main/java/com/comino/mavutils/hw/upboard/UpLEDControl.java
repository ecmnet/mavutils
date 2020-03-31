package com.comino.mavutils.hw.upboard;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import com.comino.mavutils.legacy.ExecutorService;


public class UpLEDControl {

	private static void setLED(String led, boolean onoff) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream("/sys/class/leds/upboard:"+led.toLowerCase()+":/brightness"));
		if(onoff)
			  out.print("1");
		else
			  out.print("0");
		out.close();
		} catch(Exception e) { e.printStackTrace(); }
	}

	public static void flash(String led, int time_ms) {
		setLED(led,true);
		ExecutorService.get().schedule(() -> { setLED(led,false); }, time_ms, TimeUnit.MILLISECONDS);
	}

	public static void clear() {
		setLED("green", false); 	setLED("red", false); setLED("yellow", false);
	}
}
