package com.comino.mavutils.hw;

import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

import com.comino.mavutils.hw.jetson.CPUTemperature;
import com.comino.mavutils.linux.LinuxUtils;

public class HardwareControl {

	public static final int UPBOARD = 0;
	public static final int JETSON  = 1;
	public static final int SITL    = 2;

	private OperatingSystemMXBean osBean = null;
	private MemoryMXBean mxBean = null;

	private ICPUTemperature temp;
	private WifiQuality     wifi;

	private String arch;
	private int    archid;
	private int    cpu;

	public HardwareControl() {

		mxBean = java.lang.management.ManagementFactory.getMemoryMXBean();
		osBean =  java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		arch = osBean.getArch();

		if(arch.contains("aarch64")) {
			archid = JETSON;
			temp = new com.comino.mavutils.hw.jetson.CPUTemperature();
		}
		else {
			archid = UPBOARD;
			temp = new com.comino.mavutils.hw.upboard.CPUTemperature();
		}

		mxBean = java.lang.management.ManagementFactory.getMemoryMXBean();

		wifi = new WifiQuality();

	}

	public int getTemperature() {
      return temp.get();
	}

	public byte getWifiQuality() {
		return (byte)wifi.get();
	}

	public String getArchitecture() {
		return arch;
	}

	public int getMemoryUsage() {
		return (int)(mxBean.getHeapMemoryUsage().getUsed() * 100 /mxBean.getHeapMemoryUsage().getMax());
	}

	public int getCPULoad() {
     return cpu;
	}

	public void update() {

		temp.determine();
		wifi.getQuality();

		try {
			cpu = LinuxUtils.getProcessCpuLoad();
		} catch (Exception e) {	}
	}



}
