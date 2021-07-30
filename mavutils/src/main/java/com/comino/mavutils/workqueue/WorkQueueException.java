package com.comino.mavutils.workqueue;

public class WorkQueueException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String msg = null;

	public WorkQueueException(String msg) {
		super();
		this.msg = msg;
	}
	
	public WorkQueueException() {
		super();
	}
	
	public String getMessage() {
		return msg;
	}
	
	public String toString() {
		if(msg!=null)
		  return "WorkQueueException: "+msg;
		return "WorkQueueException";
	}
}
