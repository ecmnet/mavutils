package com.comino.mavutils;

import io.dvlopt.linux.i2c.I2CBuffer;
import io.dvlopt.linux.i2c.I2CBus;

public class I2CTest {
	
	// sudo java -cp  mavlquac-0.0.1-jar-with-dependencies.jar com/comino/mavutils/I2CTest 

	private static final int ADDRESS = 0x48 ;

	public static void main(String[] args) {

		I2CBuffer buffer = new I2CBuffer(2);

		try {
			I2CBus bus = new I2CBus( 5 ) ;
			buffer.set(0, (byte)0);
			bus.selectSlave( ADDRESS ) ;
			bus.write(buffer,1); ;

			while(true) {

				bus.read(buffer, 2);
				float temp = ( buffer.get(0) << 8 | buffer.get(1) ) / 256.0f;

				System.out.println("Temperature: "+temp);

				Thread.sleep(100);

			}




		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
