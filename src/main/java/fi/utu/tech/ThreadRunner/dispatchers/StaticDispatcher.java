package fi.utu.tech.ThreadRunner.dispatchers;

import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;
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
			float tasksPerThread = ilist.size()/(float)threadCount;
			int minTasksPerThread = (int)Math.floor(tasksPerThread);
			int bonusTasksForFirstNThreads = (int)Math.ceil((tasksPerThread%1)*threadCount);  
			
			// Jaa tehtävät säikeille
			for(int threadId=0,firstTaskId = 0; threadId < threadCount; threadId++) {
				int lastTaskId = firstTaskId+ minTasksPerThread+ (threadId<bonusTasksForFirstNThreads?1:0); 
				var subList = ilist.subList(firstTaskId,lastTaskId);		
				threads.add(new StaticCounter(new ArrayList<Integer>(subList), controlSet));
				firstTaskId = lastTaskId; 
			}
						
			// Käynnistä säikeet
			for(StaticCounter thread: threads) thread.start();
			// Odota säikeiden suoritusta
			for(StaticCounter thread: threads) thread.join();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
