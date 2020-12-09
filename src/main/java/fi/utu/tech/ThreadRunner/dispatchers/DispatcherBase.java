package fi.utu.tech.ThreadRunner.dispatchers;
import java.util.ArrayList;

import fi.utu.tech.ThreadRunner.tasks.Countable;
import fi.utu.tech.ThreadRunner.tasks.TaskFactory;

public abstract class DispatcherBase implements IDispatcher{
	
	public abstract void dispatch(ControlSet controlSet);
	
	protected static TaskGroup buildTaskGroup(ControlSet controlSet)
	{
		TaskGroup taskGroup = new TaskGroup();
		try {
			Countable co = TaskFactory.createTask(controlSet.getTaskType());
			ArrayList<Integer> ilist = co.generate(controlSet.getAmountTasks(), controlSet.getMaxTime());
			
			// Muuttuja-alustukset tehtäväjakoa varten
			int threadCount = controlSet.getThreadCount();
			float tasksPerThread = ilist.size()/(float)threadCount;
			int minTasksPerThread = (int)Math.floor(tasksPerThread);
			int bonusTasksForFirstNThreads = (int)Math.ceil((tasksPerThread%1)*threadCount);  
			
			// Jaa tehtävät säikeille
			for(int threadId = 0,firstTaskId = 0; threadId < threadCount; threadId++) {
				int lastTaskId = firstTaskId + minTasksPerThread+ (threadId<bonusTasksForFirstNThreads?1:0); 
				var subList = ilist.subList(firstTaskId,lastTaskId);
				taskGroup.addTaskGroup(new ArrayList<Integer>(subList));
				firstTaskId = lastTaskId; 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return taskGroup;
	}
}
