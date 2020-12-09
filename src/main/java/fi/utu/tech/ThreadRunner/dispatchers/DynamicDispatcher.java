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
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;
import fi.utu.tech.ThreadRunner.counters.DynamicCounter;
import fi.utu.tech.ThreadRunner.counters.SharedData;

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
			SharedData sharedData = new SharedData();
			
			// Muuttuja-alustukset tehtäväjakoa varten
			int threadCount = controlSet.getThreadCount();
			int tasksPerThread = ilist.size()/threadCount;
			int leftOverTasks = ilist.size() % threadCount;
			int take = tasksPerThread;
			
			// Jaa tehtävät huomattavasti suurempaan määrään osajoukkoja kuin on säikeitä
			for(int i=0; i < ilist.size(); i+=take) {
				if(leftOverTasks > 0) {
					leftOverTasks--;
					take = (int)Math.ceil(tasksPerThread/3) + 1;

				} else {
					take = (int)Math.ceil(tasksPerThread/3);
				}
				ArrayList<Integer> ilistThread = new ArrayList<Integer>(ilist.subList(i, Math.min( ilist.size(), i + take )));		
				sharedData.addSubList(ilistThread);
			}
			
			// Alusta ja käynnistä koordinaattorisäie
			Coordinator coordinator = new Coordinator(sharedData, controlSet,threadCount);
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
	
	Thread[] threads;
	
	public Coordinator(SharedData sharedData, ControlSet controlSet, int threadCount) {
		
		threads = new Thread[threadCount];
		for(int i=0; i<threadCount; i++) {
			threads[i] = new DynamicCounter(controlSet, sharedData);
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

