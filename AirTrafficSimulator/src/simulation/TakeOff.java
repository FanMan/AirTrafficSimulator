package simulation;

public class TakeOff extends Thread
{
	private WindGenerator wind;
	private FlightGenerator flight;
	private Runway runway;
	
	public TakeOff(FlightGenerator fg, WindGenerator wg, Runway r)
	{
		flight = fg;
		wind = wg;
		runway = r;
	}
	
	public void run()
	{
		try{
			
			/*
			 * while the FlightGenerator thread is active or the Takeoff Queue is not empty
			 * the loop will continue running
			 */
			while(flight.isAlive() || !flight.isTakeOffQueueEmpty())
			{
				// if the takeoff queue is not empty and this is not interrupted
				if(!flight.isTakeOffQueueEmpty() && !isInterrupted())
				{
					// get the flight number and wind direction and pass it to the runway class
					runway.takeoff(flight.getTakeoffFlight(), wind.getWind());
				}
				
				Thread.sleep(500);
			}
			
		}
		catch(InterruptedException e){}
	}
}
