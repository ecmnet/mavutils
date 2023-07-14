package com.comino.mavutils.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import com.comino.mavutils.legacy.ExecutorService;

public class WifiQuality {

	private long tms = 0;
	private int  quality = 0;
	private String device;

	public WifiQuality(String device) {
		this.device = device;
	}

	public WifiQuality() {
		device = getDevice();	
	}

	public void getQuality() {
		if(System.currentTimeMillis()-tms < 2000)
			return;
		if( device == null) {
			quality = 100;
			return;
		}
		
		try {
			String line = null;
			Process process = Runtime.getRuntime().exec("iw dev "+device+" link");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null) {
				if(line.contains("signal:")) {
					quality = Math.min(Math.max(2 * (Integer.parseInt(line.substring(9, 13).trim()) + 100), 0), 100);
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
	
	private String getDevice() {
		try {
			
			String line = null;
			Process process = Runtime.getRuntime().exec("iw dev | awk '$1==\"Interface\"{print $2}'");
			process.waitFor(1000,TimeUnit.MILLISECONDS);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null) {
				System.out.println("WLAN device "+line+" found");
				reader.close();
				return line;
			}
			reader.close();

		} catch (IOException | InterruptedException e) {
			
		}
		System.out.println("No Wifi quality info available");
		return null;
	}

}
