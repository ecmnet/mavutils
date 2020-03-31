package com.comino.mavutils.hw.jetson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.comino.mavutils.hw.ICPUTemperature;

public class CPUTemperature implements ICPUTemperature {

	private long tms = 0;
	private int  temperature = 0;

	public void determine() {
		if(System.currentTimeMillis()-tms < 10000)
			return;
		try {
			String line = null;
			Process process = Runtime.getRuntime().exec("cat /sys/devices/thermal-fan-est/temps");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			if((line=reader.readLine())!=null) {
					temperature = (temperature  + Integer.parseInt(line.substring(3,9).trim()) / 1000) / 2;
			}
			reader.close();
		} catch (IOException e) { }
		tms = System.currentTimeMillis();
	}

	public byte get() {
		return (byte)temperature;
	}

}
