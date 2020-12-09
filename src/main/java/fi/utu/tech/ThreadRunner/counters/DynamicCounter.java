package fi.utu.tech.ThreadRunner.counters;

/*
 * Luokka, jossa toteutetaan dynaaminen tehtävien jako ts. työn tehtävä 2
 * 
* @version     1.0                 
* @since       1.0          
*/
import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;
import fi.utu.tech.ThreadRunner.dispatchers.ControlSet;


public class DynamicCounter extends Thread {
	
	SharedData sd;
	Worker worker;
	ControlSet controlSet;
	
	public DynamicCounter(ControlSet controlSet, SharedData sd) {
		this.sd = sd;
		this.controlSet = controlSet;
	}
	
	public void run() {

		ArrayList<Integer> list;
		try {
			Worker worker = WorkerFactory.createWorker(controlSet.getWorkerType());
			while(sd.areTasksLeft()) {
				list = sd.getSubList();
				for (int time :  list) {
					worker.count(time);
				}
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}