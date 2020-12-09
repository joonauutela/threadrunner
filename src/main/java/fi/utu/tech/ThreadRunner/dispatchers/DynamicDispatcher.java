package fi.utu.tech.ThreadRunner.dispatchers;

/*
 * Luokka, jossa toteutetaan dynaaminen tehtävien jako ts. työn tehtävä 2
 * 
* @version     1.0                 
* @since       1.0          
*/

import fi.utu.tech.ThreadRunner.counters.DynamicCounter;

public class DynamicDispatcher extends DispatcherBase {

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
			var taskGroup = super.buildTaskGroup(controlSet);
			
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

