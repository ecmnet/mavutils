package com.comino.mavutils.hw;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;

import com.comino.mavutils.hw.jetson.CPUTemperature;
import com.comino.mavutils.hw.upboard.BatteryTemperature;
import com.comino.mavutils.linux.LinuxUtils;

import us.ihmc.log.LogTools;

public class HardwareAbstraction implements Runnable {

	public static final int UPBOARD = 0;
	public static final int JETSON  = 1;
	public static final int SITL    = 2;

	private static HardwareAbstraction instance = null;

	private OperatingSystemMXBean osBean = null;
	private MemoryMXBean mxBean = null;

	private IMeasurement cpu_temp;
	private IMeasurement battery_temp;
	private WifiQuality     wifi;

	private String arch;
	private int    archid;
	private int    cpu;
	private int    memory=0;

	public static HardwareAbstraction instance() {
		if(instance==null)
			instance = new HardwareAbstraction();
		return instance;
	}

	private HardwareAbstraction(){

		mxBean = java.lang.management.ManagementFactory.getMemoryMXBean();
		osBean =  java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		arch = osBean.getArch();
		
		if (arch.contains("x86_64") || (osBean.getName().contains("Mac") && arch.contains("aarch64"))){
			archid = SITL;
			wifi = new WifiQuality(null);
			cpu_temp = new com.comino.mavutils.hw.upboard.CPUTemperature();
			battery_temp = new com.comino.mavutils.hw.sitl.BatteryTemperature();
			LogTools.info("Intel/Arm SITL architecture found..");
			wifi = new WifiQuality(null);
		}

		else if(arch.contains("aarch64")) {
			archid = JETSON;
			cpu_temp = new com.comino.mavutils.hw.jetson.CPUTemperature();
			battery_temp = new com.comino.mavutils.hw.sitl.BatteryTemperature();
			LogTools.info("Jetson Nano architecture found..");
			wifi = new WifiQuality("wlan0");
		
		}
		else if (arch.contains("amd64")){
			archid = SITL;
			//archid = UPBOARD;
			cpu_temp = new com.comino.mavutils.hw.upboard.CPUTemperature();
			//battery_temp = new com.comino.mavutils.hw.upboard.BatteryTemperature();
			LogTools.info("Intel UpBoard architecture found..");
		//	wifi = new WifiQuality("wlx74da38805d92");
			//	wifi = new WifiQuality();
			
			wifi = new WifiQuality(null);
			battery_temp = new com.comino.mavutils.hw.sitl.BatteryTemperature();
		}



	}

	public int getArchId() {
		return archid;
	}

	public float getCPUTemperature() {
		return cpu_temp.get();
	}

	public float getBatteryTemperature() {
		return battery_temp.get();
	}

	public byte getWifiQuality() {
		return (byte)wifi.get();
	}

	public String getArchName() {
		return arch;
	}

	public int getMemoryUsage() {
		return memory;
	}

	public int getCPULoad() {
		return cpu;
	}

	public void run() {

		float cpu_l;

		memory = (int)(mxBean.getHeapMemoryUsage().getUsed() * 100 / mxBean.getHeapMemoryUsage().getMax());

		cpu_temp.determine();
		wifi.getQuality();

		if(archid == UPBOARD)
			battery_temp.determine();

		try {
			cpu_l = LinuxUtils.getProcessCpuLoad();
			if(cpu_l > 0) {
				cpu = (int)(cpu*0.7f + cpu_l*0.3f);
			}
		} catch (Exception e) {	}
	}



}
