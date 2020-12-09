package fi.utu.tech.ThreadRunner.counters;

import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;


/**
 * 
 * SharedData-luokkaa käytetään ainoastaan luokissa DynamicCounter.java ja DynamicDisaptcher.java
 * 
 * Datalle on tässä tilanteessa mielekkäämpi luoda olio sen sijaan, että käyttäisi esim. sisäkkäisiä ArrayListejä
 *
 */
public class SharedData {
	
	ArrayList<ArrayList<Integer>> taskGroups;
	
	public SharedData() {
		taskGroups = new ArrayList<ArrayList<Integer>>();
	}
	
	public void addTasks(ArrayList<Integer> subList) {
		taskGroups.add(subList);
	}
	
	public synchronized ArrayList<Integer> assignTasks(){
		return taskGroups.remove(0);
	}
	
	public synchronized boolean areTasksLeft() {
		return taskGroups.size() > 0;
	}
}