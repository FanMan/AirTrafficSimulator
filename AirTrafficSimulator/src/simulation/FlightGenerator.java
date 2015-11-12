package simulation;

import java.util.*;
import java.util.concurrent.locks.*;

public class FlightGenerator extends Thread
{
	private Random rand;
	private LinkedList<Integer> takeoff_queue;
	private LinkedList<Integer> landing_queue;
	
	private Lock L;
	private Condition C;
	
	private int state;
	private int flight_number;
	private int num_of_flights;
	
	public FlightGenerator(int n)
	{
		rand = new Random();
		takeoff_queue = new LinkedList<Integer>();
		landing_queue = new LinkedList<Integer>();
		
		L = new ReentrantLock();
		C = L.newCondition();
		
		state = 0;
		flight_number = 0;
		num_of_flights = n;
	}
	
	public void addTakeOff(int n) throws InterruptedException
	{
		L.lock();
		
		try {
			takeoff_queue.add(n);
			C.signalAll();
		}
		finally {
			L.unlock();
		}
	}
	
	public void addLanding(int n) throws InterruptedException
	{
		L.lock();
		
		try {
			landing_queue.add(n);
			C.signalAll();
		}
		finally {
			L.unlock();
		}
	}
	
	// retrieves and removes the flight at the beginning of the list in the takeoff queue
	public int getTakeoffFlight() throws InterruptedException
	{
		L.lock();
		
		try {
			return takeoff_queue.remove();
		}
		finally {
			C.signalAll();
			L.unlock();
		}
		
		//return takeoff_queue.remove();
	}
	
	// retrieves and removes the flight at the beginning of the list in the landing queue
	public int getLandingFlight() throws InterruptedException
	{
		L.lock();
		
		try {
			return landing_queue.remove();
		}
		finally {
			C.signalAll();
			L.unlock();
		}
		
		//return landing_queue.remove();
	}
	
	// returns a boolean on whether the queue is empty or not
	public boolean isTakeOffQueueEmpty()
	{
		return takeoff_queue.isEmpty();
	}
	
	// returns a boolean on whether the queue is empty or not
	public boolean isLandingQueueEmpty()
	{
		return landing_queue.isEmpty();
	}
	
	public void run()
	{
		try {
			
			for(int i = 0; i < num_of_flights; i++)
			{
				// randomizes a flight number
				flight_number = rand.nextInt(777) + 1;
				
				// ensures a flight is added to each queue as evenly as possibly
				switch(state)
				{
				case 0:
					//System.out.println("Flight " + flight_number + " created. " + takeoff_queue.getLast() + "\n");
					addTakeOff(flight_number);
					state = 1;
					break;
				case 1:
					//System.out.println("Flight " + flight_number + " created. " + landing_queue.getLast() + "\n");
					addLanding(flight_number);
					state = 0;
					break;
				default:
					System.out.println("No flights created");
				}
				
				Thread.sleep(250);
				//Thread.sleep(100); <-- also to test for faster flight creation; works
			}
			
		}
		catch(InterruptedException e){}
	}
}
