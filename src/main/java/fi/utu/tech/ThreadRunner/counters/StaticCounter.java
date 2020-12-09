package fi.utu.tech.ThreadRunner.counters;


import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;
import fi.utu.tech.ThreadRunner.dispatchers.ControlSet;


public class StaticCounter extends Thread {
	
	private ArrayList<Integer> ilist;
	Worker worker;
	ControlSet controlSet;
	
	public StaticCounter(ArrayList<Integer> ilist, ControlSet controlSet) {
		System.out.println(ilist.size());
		this.ilist = ilist;
		this.controlSet = controlSet;
	}
	
	public void run() {
		try {
			Worker worker = WorkerFactory.createWorker(controlSet.getWorkerType());
			for (int time : ilist) {
				worker.count(time);
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}