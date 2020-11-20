package com.comino.mavutils.hw.sitl;

import com.comino.mavutils.hw.IMeasurement;

public class BatteryTemperature implements IMeasurement {
	
	
	
	private float temp;
	
	
	public BatteryTemperature() {
		
	}

	public float get() {
		return temp;
	}

	public void determine() {
		
		temp = 54.0f + (float)Math.random()*3f;
	
		
	}

}
