package simulation;

import java.util.concurrent.locks.*;

public class Runway
{
	private Lock L;
	private Condition C;
	
	private int wind_direction;
	private int runway_number;
	private int turn;
	private String runway_direction;
	
	public Runway()
	{
		L = new ReentrantLock();
		C = L.newCondition();
		
		wind_direction = 0;
		runway_number = 0;
		turn = 0;
		runway_direction = "";
	}
	
	// determines which runway will be used based upon the direction of the wind
	public void RunwaySystem()
	{
		// if the wind direction is between 136 and 225
		if(wind_direction >= 136 && wind_direction <= 225)
		{
			System.out.println("Wind direction is " + wind_direction + "° blowing North to South");
			runway_number = 18;
			runway_direction = " facing South to North.";
		}
		// if the wind direction is between 1 and 45 and between 316 and 360
		else if((wind_direction >= 1 && wind_direction <= 45) || 
				(wind_direction >= 316 && wind_direction <= 360))
		{
			System.out.println("Wind direction is " + wind_direction + "° blowing South to North");
			runway_number = 36;
			runway_direction = " facing North to South.";
		}
		else if(wind_direction >= 46 && wind_direction <= 135)
		{
			System.out.println("Wind direction is " + wind_direction + "° blowing West to East");
			runway_number = 9;
			runway_direction = " facing East to West.";
		}
		// if the wind direction is between 226 and 315
		else if(wind_direction >= 226 && wind_direction <= 315)
		{
			System.out.println("Wind direction is " + wind_direction + "° blowing East to West");
			runway_number = 27;
			runway_direction = " facing West to East.";
		}
		else
		{
			System.out.println("Not a valid wind direction");
		}
	}
	
	// the takeoff method
	public void takeoff(int flight_num, int wind) throws InterruptedException
	{
		L.lock();
		
		try {
			// if turn is set to landing, then wait
			if(turn == 1)
			{
				C.await();
			}
			
			wind_direction = wind;
			RunwaySystem();
			
			System.out.println("Flight " + flight_num + " is cleared for takeoff on runway " + runway_number + runway_direction);
			System.out.println("Flight " + flight_num + " has departed. The runway is now clear.");
			System.out.println("===========>\n");
			
			// set the turn for landing
			turn = 1;
			
			C.signalAll();
		} 
		finally
		{
			L.unlock();
		}
	}
	
	// the landing method
	public void landing(int flight_num, int wind) throws InterruptedException
	{
		L.lock();
		
		try {
			// if turn is set to takeoff, then wait
			if(turn == 0)
			{
				C.await();
			}
			
			wind_direction = wind;
			RunwaySystem();
			
			System.out.println("Flight " + flight_num + " is cleared for landing on runway " + runway_number + runway_direction);
			System.out.println("Flight " + flight_num + " has arrived. The runway is now clear.");
			System.out.println("===========>\n");
			
			// set the turn for takeoff
			turn = 0;
			C.signalAll();
		} 
		finally
		{
			L.unlock();
		}
	}
}
