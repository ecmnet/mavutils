package com.comino.mavutils.workqueue;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.comino.mavutils.legacy.ExecutorService.DaemonThreadFactory;

public class WorkQueue { 

	private static WorkQueue instance;

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

	public void addToQueue(String queue, String name, int cycle_ms, Runnable runnable) {
		queues.get(queue).add(new WorkItem(name, runnable, cycle_ms));
	}
	
	public void addToQueue(String queue, int cycle_ms, Runnable runnable) {
		queues.get(queue).add(new WorkItem(runnable.getClass().getCanonicalName(),runnable , cycle_ms));
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
			System.out.println("Queue "+i+":");
			w.print(); 
		});
	}

	class Worker implements Runnable {

		private final HashMap<Integer, WorkItem> queue = new HashMap<Integer, WorkItem>();
		private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);

		private String     name         = null;
		private long       min_cycle_ns = Long.MAX_VALUE;
		private long       tms          = 0;
		private Future<?>  future       = null;

		public Worker(String name, int priority) {
			this.name = name;
			pool.setThreadFactory(new DaemonThreadFactory(priority,"WQ_"+name));
			pool.allowCoreThreadTimeOut(false);
			pool.setRemoveOnCancelPolicy(true);
			pool.prestartAllCoreThreads();	

		}

		public void add(WorkItem item) {
			if(min_cycle_ns  > item.getCycleTime())
				min_cycle_ns = item.getCycleTime();
			queue.put(queue.size(),item);
		}

		public void start() {
			if(queue.isEmpty()) {
				System.out.println("Worker not started: Queue is empty");
				return;
			}
			System.out.println("Worker started with cycle: "+min_cycle_ns/1000000);
			future = pool.submit(this);
		}

		public void stop() {
			if(future!=null)
				future.cancel(false);
		}

		public void print() {
			queue.forEach((i,w) -> {
				System.out.println(" -> "+i+": "+w);
			});
		}

		@Override
		public void run() {
			tms = System.nanoTime();
			future = pool.schedule(this, min_cycle_ns, TimeUnit.NANOSECONDS);
			queue.forEach((i,w) -> {
				w.run();
			});

			if((System.nanoTime()  - tms) > min_cycle_ns)
				System.err.println(" Runtime of queue "+name+" exceeds cycle time: "+(System.nanoTime()  - tms)/1000000+"ms");
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

		public WorkItem(String name, Runnable runnable, int cycle_ms) {
			this.name           = name;
			this.runnable       = runnable;
			this.cycle_ns       = cycle_ms * 1000000;
			this.last_exec      = 0;
			this.act_cycle      = 0;
			this.count          = 0;
		}

		public long getCycleTime() {
			return cycle_ns;
		}

		public String toString() {
			if(act_cycle>0)
			  return name+":\t"+(1000/act_cycle)+"Hz\t"+act_exec +"ms";
			return name;
		}


		public void run() {
			if((System.nanoTime() - last_exec) >= cycle_ns) {
				count++;
				if(last_exec > 0)
				  act_cycle = (act_cycle * (count - 1  ) + ( System.nanoTime() - last_exec) / 1000000 ) / ( count ) ;
				last_exec = System.nanoTime();
				runnable.run();
				act_exec  = (act_exec  * (count - 1  ) + ( System.nanoTime() - last_exec) / 1000000 ) / ( count ) ;
			}
		}
	}

	public static void main(String[] args) {

		WorkQueue q = WorkQueue.getInstance();

		q.addToQueue("LP", 50, ()   ->  { try { Thread.sleep(10); } catch (InterruptedException e) {} });
		q.addToQueue("LP", 50, ()   ->  { try { Thread.sleep(2); }  catch (InterruptedException e) {} });
		q.addToQueue("LP", 100, ()  ->  { try { Thread.sleep(20); } catch (InterruptedException e) {} });
		q.addToQueue("NP", 200, ()  ->  { try { Thread.sleep(2);  } catch (InterruptedException e) {} });
		q.addToQueue("HP", 10, ()   ->  { try { Thread.sleep(5);  } catch (InterruptedException e) {} });
		q.addToQueue("HP", 30, ()   ->  { try { Thread.sleep(2);  } catch (InterruptedException e) {} });

		q.start();

		int count = 1000;
		while(count-- > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { }

			q.printStatus();
		}

		q.stop();

	}

}
