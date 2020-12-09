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
 */
public class SharedData {
	ArrayList<ArrayList<Integer>> ilistSubGroups;
	
	public SharedData() {
		 ilistSubGroups = new ArrayList<ArrayList<Integer>>();
	}
	
	public void addSubList(ArrayList<Integer> subList) {
		ilistSubGroups.add(subList);
	}
	
	public synchronized ArrayList<Integer> getSubList(){
		ArrayList<Integer> list = ilistSubGroups.remove(0);
		return list;
	}
	
	public void getCount() {
		System.out.println(ilistSubGroups.size());
	}
	
	public synchronized boolean areTasksLeft() {
		return ilistSubGroups.size() > 0;
	}
}