package fi.utu.tech.ThreadRunner.counters;

/*
 * Luokka, jossa toteutetaan dynaaminen tehtävien jako ts. työn tehtävä 2
 * 
* @version     1.0                 
* @since       1.0          
*/

import fi.utu.tech.ThreadRunner.dispatchers.ControlSet;
import fi.utu.tech.ThreadRunner.dispatchers.TaskGroup;


public class DynamicCounter extends CounterBase {	
	private TaskGroup taskGroup;
	
	public DynamicCounter(ControlSet controlSet, TaskGroup taskGroup) {
		super(controlSet);
		this.taskGroup = taskGroup;
	}
	
	protected void runCounter() 
	{
		try {
			while(taskGroup.areTasksLeft()) {
				var ilist = taskGroup.assignTasks();
				for (int time :  ilist) {
					worker.count(time);
				}
			}
		} 
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
}