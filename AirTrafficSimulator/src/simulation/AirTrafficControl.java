package simulation;

/**
 * @author Michael Jaramillo
 * 
 * An Air Traffic Control Simulation project made for an assignment for class
 *
 */

public class AirTrafficControl
{
	public static void main(String[] args) {
		// enter amount of flights to be created
		//FlightGenerator flight = new FlightGenerator(200); <-- to test for 200 flights; for fun
		FlightGenerator flight = new FlightGenerator(20);
		WindGenerator wind = new WindGenerator(flight);
		
		Runway runway = new Runway();
		TakeOff depart = new TakeOff(flight, wind, runway);
		Landing arrive = new Landing(flight, wind, runway);
		
		wind.start();
		flight.start();
		depart.start();
		arrive.start();
	}
}
