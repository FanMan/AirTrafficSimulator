package simulation;

import java.util.*;
import java.util.concurrent.locks.*;

public class WindGenerator extends Thread
{
	private FlightGenerator flight;
	
	private Lock L;
	private Condition C;
	
	private int wind_direction;
	private Random rand;
	
	public WindGenerator(FlightGenerator f)
	{
		rand = new Random();
		flight = f;
		
		L = new ReentrantLock();
		C = L.newCondition();
		
		wind_direction = 0;
	}
	
	public void setWind(int n) throws InterruptedException
	{
		L.lock();
		
		try {
			wind_direction = n;
			C.signalAll();
		}
		finally {
			L.unlock();
		}
	}
	
	// get the value of the wind direction
	public int getWind()throws InterruptedException
	{
		L.lock();
		try {
			return wind_direction;
		}
		finally {
			C.signalAll();
			L.unlock();
		}
	}
	
	public void run()
	{
		try {
			
			/*
			 * while the FlightGenerator thread is active
			 * or the Landing Queue is not empty or the Takeoff queue is not empty
			 * the loop will continue running
			 */
			while(flight.isAlive() || !flight.isLandingQueueEmpty() || !flight.isLandingQueueEmpty())
			{
				//wind_direction = rand.nextInt(360) + 1;
				setWind(rand.nextInt(360) + 1);
				//System.out.println("Wind direction: " + wind_direction + "\n");
				Thread.sleep(100);
			}
			
		}
		catch(InterruptedException e){}
	}
}
