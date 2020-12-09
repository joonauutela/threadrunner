package fi.utu.tech.ThreadRunner.dispatchers;

import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
import fi.utu.tech.ThreadRunner.workers.Worker;
import fi.utu.tech.ThreadRunner.workers.WorkerFactory;
import fi.utu.tech.ThreadRunner.counters.StaticCounter;
/*
 * Luokka, jossa toteutetaan staattinen tehtävien jako ts. työn tehtävä 1
 * 
* @version     1.0                 
* @since       1.0          
*/

public class StaticDispatcher implements Dispatcher {

	/**
	 * Metodi, jossa on toteutettu staattinen tehtäväjako toiminnallisuus.
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
			// Alusta lista säikeille
			ArrayList<StaticCounter> threads = new ArrayList<StaticCounter>();
			
			// Muuttuja-alustukset tehtäväjakoa varten
			int threadCount = controlSet.getThreadCount();
			int tasksPerThread = ilist.size()/threadCount;
			int leftOverTasks = ilist.size() % threadCount;
			int take = tasksPerThread;
			
			// Jaa tehtävät säikeiden kesken
			for(int i=0; i < ilist.size(); i+=take) {
				if(leftOverTasks > 0) {
					leftOverTasks--;
					take = tasksPerThread + 1;

				} else {
					take = tasksPerThread;
				}
				ArrayList<Integer> ilistThread = new ArrayList<Integer>(ilist.subList(i, Math.min( ilist.size(), i + take )));		
				threads.add(new StaticCounter(ilistThread, controlSet));
			}
			
			// Käynnistä säikeet
			for(StaticCounter thread: threads) {
				thread.start();
			}
			// Odota säikeiden suoritusta
			for(StaticCounter thread: threads) {
				thread.join();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
