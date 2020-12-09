package fi.utu.tech.ThreadRunner.dispatchers;

import fi.utu.tech.ThreadRunner.counters.StaticCounter;
/*
 * Luokka, jossa toteutetaan staattinen tehtävien jako ts. työn tehtävä 1
 * 
* @version     1.0                 
* @since       1.0          
*/

public class StaticDispatcher extends DispatcherBase {

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
			var taskGroup = super.buildTaskGroup(controlSet);
			var taskGroupData = taskGroup.getTaskGroupData();
			
			Thread[] threads = new Thread[taskGroupData.size()];
			
			for(int i=0;i<threads.length;i++)
			{
				threads[i] = new StaticCounter(taskGroupData.get(i), controlSet);
			}
			
			for(Thread t : threads) t.start();
			for(Thread t : threads) t.join();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
