package com.comino.mavutils.hw.upboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.comino.mavutils.hw.IMeasurement;

public class CPUTemperature implements IMeasurement  {

	private long tms = 0;
	private int  temperature = 0;

	public void determine() {
		if(System.currentTimeMillis()-tms < 10000)
			return;
		try {
			String line = null;
			Process process = Runtime.getRuntime().exec("cat /sys/devices/platform/coretemp.0/hwmon/hwmon1/temp2_input");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null)
					temperature = (temperature  + Integer.parseInt(line.trim()) / 1000) / 2;
			reader.close();
		} catch (IOException e) { }
		tms = System.currentTimeMillis();
	}

	public float get() {
		return (float)temperature;
	}

}
