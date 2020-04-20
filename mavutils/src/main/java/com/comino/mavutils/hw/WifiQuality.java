package com.comino.mavutils.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WifiQuality {

	private long tms = 0;
	private int  quality = 0;
	private String device;

	public WifiQuality(String device) {
		this.device = device;
	}

	public WifiQuality() {
		//TODO: Autodetect WLAN IF
		this.device = "wlan0";
	}

	public void getQuality() {
		if(System.currentTimeMillis()-tms < 2000)
			return;
		try {
			String line = null;
			Process process = Runtime.getRuntime().exec("iw dev "+device+" station dump");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null) {
				if(line.contains("signal:")) {
					quality = Math.min(Math.max(2 * (Integer.parseInt(line.substring(9, 14).trim()) + 80), 0), 100);
				   //System.out.println(Integer.parseInt(line.substring(9, 14).trim())+":"+quality);
				}
			}
			reader.close();
		} catch (IOException e) { quality = 0; }
		tms = System.currentTimeMillis();
	}

	public int get() {
		return quality;
	}

}
