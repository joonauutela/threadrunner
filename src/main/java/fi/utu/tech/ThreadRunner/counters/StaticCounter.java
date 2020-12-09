package fi.utu.tech.ThreadRunner.counters;


import java.util.ArrayList;
import fi.utu.tech.ThreadRunner.dispatchers.ControlSet;


public class StaticCounter extends CounterBase {
	
	private ArrayList<Integer> ilist;
	
	public StaticCounter(ArrayList<Integer> ilist, ControlSet controlSet) {
		super(controlSet);
		this.ilist = ilist;
	}
	
	protected void runCounter() 
	{
		try {
			for (int time : ilist) {
				super.worker.count(time);
			}
		} 
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
}