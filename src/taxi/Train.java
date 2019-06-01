package taxi;

import static taxi.Simulation.MAX_TRAVELLERS;
import static taxi.Simulation.MIN_TRAVELLERS;

/**
 * Trains bring a number of passengers to a station in the Taxi simulation
 *
 * @author pieterkoopman
 */
public class Train implements Runnable {

    private int nrOfPassengers;
    private final Station station;
    private int nrOfTrips = 0;

    public Train(Station station) {
        this.station = station;
        this.nrOfPassengers = 0;
    }

    /**
     * Populate this train with number nrOfPassengers
     *
     * @param number the number of passengers of this train
     */
    public void loadPassengers(int number) {
        nrOfPassengers = number;
    }

    /**
     * empties this train and augment the number of Passengers at the station
     * with this nrOfPassenegers
     */
    public void unloadPassengers() {
        nrOfTrips += 1;
        station.enterStation(nrOfPassengers);
    }

    public void closeStation() {
        station.close();
    }

    public int getNrOfTrips() {
        return nrOfTrips;
    }

    @Override
    public void run() {
        while (nrOfTrips < 10) {
            loadPassengers(Util.getRandomNumber(MIN_TRAVELLERS, MAX_TRAVELLERS));
            unloadPassengers();
        }
        closeStation();
    }
}
