package com.comino.mavutils.hw.upboard;

import java.io.IOException;

import com.comino.mavutils.hw.IMeasurement;

import io.dvlopt.linux.i2c.I2CBuffer;
import io.dvlopt.linux.i2c.I2CBus;

public class BatteryTemperature implements IMeasurement  {
	
	private static final int ADDRESS = 0x48 ;
	
	private float temp;
	private I2CBus bus = null;
	private I2CBuffer buffer = null;
	
	public BatteryTemperature() {
		try {
			buffer = new I2CBuffer(2);
			bus = new I2CBus( 5 ) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public float get() {
		return temp;
	}

	public void determine() {
		
		try {
			
		bus.selectSlave( ADDRESS ) ;
		bus.smbus.writeByteDirectly( 0 ) ;
		bus.read(buffer, 2);
		temp = ( buffer.get(0) << 8 | buffer.get(1) ) / 256.0f - 0.5f;
	
		} catch (IOException e) { temp = Float.NaN; }
		
	}

}
