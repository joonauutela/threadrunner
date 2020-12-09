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
public class TaskGroup {
	
	private ArrayList<ArrayList<Integer>> taskGroups;
	
	public TaskGroup() {
		taskGroups = new ArrayList<ArrayList<Integer>>();
	}
	
	public void addTaskGroup(ArrayList<Integer> subList) {
		taskGroups.add(subList);
	}
	
	public int getTaskGroupCount() {
		return taskGroups.size();
	}
	
	public synchronized ArrayList<Integer> assignTasks(){
		return taskGroups.remove(0);
	}
	
	public synchronized boolean areTasksLeft() {
		return taskGroups.size() > 0;
	}
}