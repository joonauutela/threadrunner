package fi.utu.tech.ThreadRunner.counters;

/*
 * Luokka, jossa toteutetaan dynaaminen tehtävien jako ts. työn tehtävä 2
 * 
* @version     1.0                 
* @since       1.0          
*/
import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;
import fi.utu.tech.ThreadRunner.dispatchers.ControlSet;


public class DynamicCounter extends Thread {
	
	private TaskGroup taskGroup;
	private Worker worker;
	private ControlSet controlSet;
	
	public DynamicCounter(ControlSet controlSet, TaskGroup taskGroup) {
		this.taskGroup = taskGroup;
		this.controlSet = controlSet;
	}
	
	public void run() {

		ArrayList<Integer> ilist;
		try {
			worker = WorkerFactory.createWorker(controlSet.getWorkerType());
			while(taskGroup.areTasksLeft()) {
				ilist = taskGroup.assignTasks();
				for (int time :  ilist) {
					worker.count(time);
				}
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}