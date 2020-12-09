package fi.utu.tech.ThreadRunner.counters;

import fi.utu.tech.ThreadRunner.dispatchers.ControlSet;
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;

public abstract class CounterBase extends Thread {
	
	protected Worker worker;
	protected ControlSet controlSet;
	protected abstract void runCounter();
	
	public CounterBase(ControlSet controlSet)
	{
		this.controlSet = controlSet;
	}
	
	public void run() {
		try {
			worker = WorkerFactory.createWorker(controlSet.getWorkerType());
			runCounter();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
