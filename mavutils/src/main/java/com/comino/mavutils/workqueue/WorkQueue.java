package com.comino.mavutils.workqueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.comino.mavutils.legacy.ExecutorService.DaemonThreadFactory;

public class WorkQueue { 

	private static WorkQueue instance;
	private final long ns_ms = 1000000L;

	private final HashMap<String,Worker> queues = new HashMap<String,Worker>();

	public static WorkQueue getInstance() {
		if(instance == null)
			instance = new WorkQueue();
		return instance;
	}

	private WorkQueue() {

		queues.put("LP",  new Worker("LP",Thread.MIN_PRIORITY));
		queues.put("NP",  new Worker("NP",Thread.NORM_PRIORITY));
		queues.put("HP",  new Worker("HP",Thread.MAX_PRIORITY));

	}

	public void addCyclicTask(String queue, int cycle_ms, Runnable runnable) {
		queues.get(queue).add(new WorkItem(runnable.getClass().getCanonicalName(),runnable , cycle_ms, false));
	}

	public void addSingleTask(String queue, int delay_ms, Runnable runnable) {
		queues.get(queue).add(new WorkItem(runnable.getClass().getCanonicalName(),runnable ,delay_ms, true));
	}

	public void start() {
		queues.forEach((i,w) -> {
			w.start();
		});
	}

	public void stop() {
		queues.forEach((i,w) -> {
			w.stop();
		});
	}

	public void printStatus() {
		queues.forEach((i,w) -> {
			if(!w.queue.isEmpty()) {
				System.out.println("Queue "+i+":");
				w.print(); 
			}
		});
	}

	class Worker implements Runnable {

		private final HashMap<Integer, WorkItem> queue = new HashMap<Integer, WorkItem>();
		private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
		private final ArrayList<Integer>   remove_list = new ArrayList<Integer>(10);

		private String     name         = null;
		private long       min_cycle_ns = 1000000000;
		private long       tms          = 0;
		private boolean    isRunning    = false;

		public Worker(String name, int priority) {
			this.name = name;
			pool.setThreadFactory(new DaemonThreadFactory(priority,"WQ_"+name));
			pool.allowCoreThreadTimeOut(false);
			pool.setRemoveOnCancelPolicy(true);
			pool.prestartAllCoreThreads();	

		}

		public void add(WorkItem item) {
			if(min_cycle_ns  > item.cycle_ns && item.cycle_ns > 0) {
				min_cycle_ns = item.cycle_ns;
			}
			queue.put(queue.size(),item);		
		}

		public void start() {

			if(isRunning)
				return;

			isRunning = true;
			System.out.println("WorkQueue "+name+" started ("+min_cycle_ns/ns_ms+"ms)");
			pool.submit(this);
		}

		public void stop() {
			isRunning = false;
			queue.clear();
			pool.shutdown();
		}

		public void print() {
			queue.forEach((i,w) -> {
				System.out.println(" -> "+i+": "+w);
			});
		}

		@Override
		public void run() {
			while(isRunning) {
				tms = System.nanoTime();
			//	pool.schedule(this, min_cycle_ns, TimeUnit.NANOSECONDS);

				remove_list.clear();
				queue.forEach((i,w) -> { w.run(); });
				queue.forEach((i,w) -> { if(w.once && w.count > 0) remove_list.add(i); });
				remove_list.forEach((i) -> queue.remove(i));

				if((System.nanoTime()  - tms) > min_cycle_ns)
					System.err.println(" Runtime of queue "+name+" exceeds cycle time: "+(System.nanoTime()  - tms)/ns_ms+"ms");
				LockSupport.parkNanos(min_cycle_ns/10);

			}
		}
	}

	class WorkItem {

		private String             name;
		private Runnable       runnable;
		private long           cycle_ns;
		private long          last_exec;
		private long           act_exec;
		private long          act_cycle;
		private long              count;
		private boolean            once;

		public WorkItem(String name, Runnable runnable, int cycle_ms, boolean once) {
			this.name           = name;
			this.runnable       = runnable;
			this.cycle_ns       = (long)cycle_ms * ns_ms;
			this.act_cycle      = 0;
			this.count          = 0;
			this.once           = once;

			if(once)
				this.last_exec      = System.nanoTime() ;
			else
				this.last_exec      = 0;
		}

		public String toString() {
			if(act_cycle>0)
				return String.format("%3.1f",1000f/act_cycle)+"Hz\t"+act_exec +"ms\t"+name;
			return "\t\t"+act_exec +"ms\t"+name;
		}


		public void run() {
			if((System.nanoTime() - last_exec) >= cycle_ns) {
				count++;
				if(last_exec > 0)
					act_cycle =  ( System.nanoTime() - last_exec) / ns_ms  ;
				last_exec = System.nanoTime();
				runnable.run();
				act_exec  = (act_exec  * (count - 1  ) + ( System.nanoTime() - last_exec) / ns_ms ) / ( count ) ;
			}
		}
	}




	public static void main(String[] args) {

		WorkQueue q = WorkQueue.getInstance();

		final long tms = System.currentTimeMillis();

		q.addCyclicTask("LP", 50,  () ->  { try { Thread.sleep(10); } catch (InterruptedException e) {} });
		q.addCyclicTask("LP", 50,  () ->  { try { Thread.sleep(2);  } catch (InterruptedException e) {} });
		q.addCyclicTask("LP", 100, () ->  { try { Thread.sleep(20); } catch (InterruptedException e) {} });
		q.addCyclicTask("NP", 200, () ->  { try { Thread.sleep(2);  } catch (InterruptedException e) {} });
		q.addCyclicTask("HP", 500, () ->  { try { Thread.sleep(5);  } catch (InterruptedException e) {} });
		q.addSingleTask("LP", 7000, () ->  { try { System.out.println("===> "+(System.currentTimeMillis()-tms)) ; } catch (Exception e) {} });
		q.addCyclicTask("HP", 10,  () ->  { try { Thread.sleep(2);  } catch (InterruptedException e) {} });

		q.start();

		int count = 0;
		while(count++ < 20) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { }

			q.printStatus();
		}

		q.stop();

	}

}
