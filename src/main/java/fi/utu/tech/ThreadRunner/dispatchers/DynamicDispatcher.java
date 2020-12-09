package fi.utu.tech.ThreadRunner.dispatchers;

/*
 * Luokka, jossa toteutetaan dynaaminen tehtävien jako ts. työn tehtävä 2
 * 
* @version     1.0                 
* @since       1.0          
*/

import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
import fi.utu.tech.ThreadRunner.counters.DynamicCounter;
import fi.utu.tech.ThreadRunner.counters.TaskGroup;

public class DynamicDispatcher implements Dispatcher {

	/**
	 * Metodi, jossa on toteutetaan dynaamisen tehtäväjaon toiminnallisuus.
	 *
	 *
	 * @param ControlSet Käyttöliittymässä asetettu arvot välittyvät tässä oliossa
	 * @return void
	 * 
	 */
	public void dispatch(ControlSet controlSet) {
		try {
			Countable co = TaskFactory.createTask(controlSet.getTaskType());
			// Luo tehtävät
			ArrayList<Integer> ilist = co.generate(controlSet.getAmountTasks(), controlSet.getMaxTime());
			// Tehtäväosajoukkojen lista
			TaskGroup taskGroup = new TaskGroup();
			
			// Muuttuja-alustukset tehtäväjakoa varten
			int threadCount = controlSet.getThreadCount();
			float tasksPerThread = ilist.size()/(float)threadCount;
			int minTasksPerThread = (int)Math.floor(tasksPerThread);
			int bonusTasksForFirstNThreads = (int)Math.ceil((tasksPerThread%1)*threadCount);  
			
			// Jaa tehtävät säikeille
			for(int threadId=0,firstTaskId = 0; threadId < threadCount; threadId++) {
				int lastTaskId = firstTaskId + minTasksPerThread+ (threadId<bonusTasksForFirstNThreads?1:0); 
				var subList = ilist.subList(firstTaskId,lastTaskId);
				taskGroup.addTaskGroup(new ArrayList<Integer>(subList));
				firstTaskId = lastTaskId; 
			}
			
			// Alusta ja käynnistä koordinaattorisäie
			Coordinator coordinator = new Coordinator(taskGroup, controlSet);
			coordinator.start();
			coordinator.join();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

/*
 * Luokka koordinoi alisäikeiden työnjakoa
 */
class Coordinator extends Thread {
	
	private Thread[] threads;
	
	public Coordinator(TaskGroup taskGroup, ControlSet controlSet) {
		int threadCount = taskGroup.getTaskGroupCount();
		threads = new Thread[threadCount];
		for(int i=0; i<threadCount; i++) {
			threads[i] = new DynamicCounter(controlSet, taskGroup);
		}
	}
	
	public void run() {
		Thread myThread = Thread.currentThread();
		int myPriority = myThread.getPriority();
		
		try {
			// Alisäikeiden alustus
			for(int i=0; i< threads.length; i++) {
				threads[i].setPriority(myPriority-1);
				threads[i].start();
			}
			for(int i=0; i< threads.length; i++) {
				threads[i].join();
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
		waitForCountersToFinish(myPriority);
	}
	protected void waitForCountersToFinish(int myPriority) {
		// Lasketaan koordinaattorisäikeen prioriteettia, jotta alisäikeet pääsevät suoritukseen.
		Thread myThread = Thread.currentThread();
		myThread.setPriority(myPriority - 1);
	}
}

