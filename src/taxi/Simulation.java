package taxi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author pieterkoopman
 * @author Paolo Scattolin
 * @author Johan Urban
 */
public class Simulation {

    /**
     * Constants for the size of the simulation
     */
    public static final int TRAIN_TRIPS = 10;
    public static final int MIN_TRAVELLERS = 60;
    public static final int MAX_TRAVELLERS = 90;
    public static final int CAPACITY_SMALL = 4;
    public static final int CAPACITY_LARGE = 7;
    public static final int TIME_SMALL = 2;
    public static final int TIME_LARGE = 3;
    public static final int NR_OF_TAXIS = 4;
    public static final int NR_OF_SMALL_TAXIS = 2;

    /**
     * Constants for time measures.
     */
    public static final long STARTTIME = System.currentTimeMillis();

    /**
     * main elements of the simulation
     */
    private final Taxi[] taxis;
    private final Train train;
    private final Station station;

    /**
     * hasEnded: is the simulation finished? nextTaxi: number of the taxi to be
     * use in next step
     */
    private final boolean hasEnded = false;
    private final int nextTaxi = 0;

    /**
     * Constructor: create station and small and large taxis
     */
    public Simulation() {
        station = new Station();
        taxis = new Taxi[NR_OF_TAXIS];
        for (int i = 0; i < NR_OF_TAXIS; i++) {
            taxis[i] = i < NR_OF_SMALL_TAXIS
                    ? new Taxi(i + 1, CAPACITY_SMALL, TIME_SMALL, station)
                    : new Taxi(i + 1, CAPACITY_LARGE, TIME_LARGE, station);
        }
        train = new Train(station);
    }

    /**
     * it creates and starts the threads for train and taxis and shows stats
     * when all the threads are done.
     */
    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(train);
        for (Taxi taxi : taxis) {
            executor.execute(taxi);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            showStatistics();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean ended() {
        return hasEnded;
    }

    public void showStatistics() {
        System.out.println("All persons have been transported");
        System.out.println("Total transport time in this simulation:" + calcTotalTime(taxis));
        System.out.println("Total number of train travelers: " + station.getTotalNrOfPassengers());
        System.out.println("Total number of persons transported in this simulation: " + calcTotalNrOfPassengers(taxis));
    }

    /**
     * Calculates the total time of the simulation by looping over all taxis
     *
     * @param taxis
     * @return total time
     */
    private static int calcTotalTime(Taxi[] taxis) {
        int time = 0;
        for (Taxi taxi : taxis) {
            time += taxi.calcTotalTime();
        }
        return time;
    }

    /**
     * Calculates the total number of passengers that has been transported by
     * looping over all taxis
     *
     * @param taxis
     * @return total number of passengers
     */
    private static int calcTotalNrOfPassengers(Taxi[] taxis) {
        int total = 0;
        for (Taxi taxi : taxis) {
            total += taxi.getTotalNrOfPassengers();
        }
        return total;
    }

    public Simulation getSim() {
        return this;
    }
}
