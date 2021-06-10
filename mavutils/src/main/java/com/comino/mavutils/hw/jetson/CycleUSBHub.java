package com.comino.mavutils.hw.jetson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CycleUSBHub {
	
	public static void run() {
		String line;
		try {
			Process process = Runtime.getRuntime().exec("uhubctl -l 1-2 -p 1 -a cycle");
			System.out.println("USBHub cycle performed....");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null) {
				System.out.println(line);
			}
			reader.close();
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
