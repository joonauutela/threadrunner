package fi.utu.tech.ThreadRunner.dispatchers;
import java.util.ArrayList;

/**
 * 
 * TaskGroup-luokkaa käytetään ainoastaan luokissa DynamicCounter.java ja DynamicDisaptcher.java
 * 
 * Datalle on tässä tilanteessa mielekkäämpi luoda olio sen sijaan, että käyttäisi esim. sisäkkäisiä ArrayListejä
 *
 */
public class TaskGroup {	
	private ArrayList<ArrayList<Integer>> taskGroupData;
	
	public TaskGroup() {
		taskGroupData = new ArrayList<ArrayList<Integer>>();
	}
	
	public void addTaskGroup(ArrayList<Integer> subList) {
		taskGroupData.add(subList);
	}
	
	public int getTaskGroupCount() {
		return taskGroupData.size();
	}
	
	public ArrayList<ArrayList<Integer>> getTaskGroupData()
	{
		return taskGroupData;
	}
	
	public synchronized ArrayList<Integer> assignTasks(){
		return taskGroupData.remove(0);
	}
	
	public synchronized boolean areTasksLeft() {
		return taskGroupData.size() > 0;
	}
}