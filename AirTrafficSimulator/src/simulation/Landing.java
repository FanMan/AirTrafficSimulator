package simulation;

public class Landing extends Thread
{
	private WindGenerator wind;
	private FlightGenerator flight;
	private Runway runway;
	
	public Landing(FlightGenerator fg, WindGenerator wg, Runway r)
	{
		flight = fg;
		wind = wg;
		runway = r;
	}
	
	public void run()
	{
		try{
			/*
			 * while the FlightGenerator thread is active or the Landing Queue is not empty
			 * the loop will continue running
			 */
			while(flight.isAlive() || !flight.isLandingQueueEmpty())
			{
				// if the landing queue is not empty and this is not interrupted
				if(!flight.isLandingQueueEmpty() && !isInterrupted())
				{
					// get the flight number and wind direction and pass it to the runway class
					runway.landing(flight.getLandingFlight(), wind.getWind());
				}
				
				Thread.sleep(400);
			}
		}
		catch(InterruptedException e){}
	}
}
