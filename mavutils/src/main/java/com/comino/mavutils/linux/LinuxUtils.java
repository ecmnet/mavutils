package com.comino.mavutils.linux;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class LinuxUtils {

	private  final static MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	private  static ObjectName name ;

	public static int getProcessCpuLoad() throws Exception {

		if(name==null)
		     name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
		
	    AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });

	    if (list.isEmpty())  return 0;

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)    return 0;
	    // returns a percentage value with 1 decimal point precision
	    return (int)(value * 100);
	}

}
