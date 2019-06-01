package taxi;

import static taxi.Simulation.STARTTIME;

/**
 *
 * @author pieterkoopman
 * @author Paolo Scattolin
 * @author Johan Urban
 *
 */
public class Taxi implements Runnable {

    private final int taxiId;
    private final int maxNrOfPassengers;
    private final int transportationTime;
    private final Station station;

    private int totalNrOfPassengers = 0;
    private int nrOfRides = 0;

    public Taxi(int nr, int maxNumberOfPassengers, int transportationTime, Station station) {
        this.taxiId = nr;
        this.maxNrOfPassengers = maxNumberOfPassengers;
        this.transportationTime = transportationTime;
        this.station = station;
        System.out.println("Taxi " + nr + " created");
    }

    /**
     * Try to take the maximum number of passengers from the station. If actual
     * number op passengers is less then that number is taken When there are no
     * passengers the taxi just waits a little
     */
    public void takePassengers() {
        int nrOfPassengers = station.leaveStation(maxNrOfPassengers);
        if (nrOfPassengers > 0) {
            totalNrOfPassengers += nrOfPassengers;
            nrOfRides++;
            System.out.println(getColor(taxiId)+"Taxi " + taxiId + " takes " + nrOfPassengers + " passengers" + "at " + (System.currentTimeMillis() - STARTTIME) + " ms.");
        } else {
            System.out.println("There are no passengers for taxi " + taxiId);
        }
    }

    /**
     * Calculates the total time of this taxi by multiplying the number of rides
     * by the transportation time
     *
     * @return total time
     */
    public int calcTotalTime() {
        return transportationTime * nrOfRides;
    }

    public int getTotalNrOfPassengers() {
        return totalNrOfPassengers;
    }

    private String getColor(int id){
        switch (id){
            case 1:
                return "\033[31;1m";
            case 2:
                return "\033[32;1m";
            case 3:
                return "\033[36;1m";
            case 4:
                return "\033[35;1m";            
        }
        return "\033[0m";
    }
    
    @Override
    public void run() {
        while (station.waitingPassengers() > 0 || !station.isClosed()) {
            takePassengers();
            try {
                int time = 10 * (Util.getRandomNumber(transportationTime, transportationTime + maxNrOfPassengers));
                Thread.sleep(time); //add some delays based on the number of passengers.
                System.out.println(getColor(taxiId) + "Taxi " + taxiId + " drops passengers "
                        + " after " + time + " ms.");   
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
