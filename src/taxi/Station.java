package taxi;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static taxi.Simulation.STARTTIME;

/**
 * @author pieterkoopman
 * @author Paolo Scattolin
 * @author Johan Urban
 *
 *
 * Class that holds the number of persons arriving by train at the station and
 * waiting for a taxi
 */
public class Station {

    private int nrOfPassengersAtStation = 0;
    private int totalNrOfPassengers = 0;
    private boolean isClosed = false;
    private final Lock lock = new ReentrantLock();
    private final Condition noPeople = lock.newCondition();
    private final Condition waitingPeople = lock.newCondition();
    private final String color = "\033[33;1m";

    public void enterStation(int nrOfPassengers) {
        lock.lock();
        try {
            while (waitingPassengers() > 0) {
                noPeople.await();
            }
            nrOfPassengersAtStation += nrOfPassengers;
            totalNrOfPassengers += nrOfPassengers;
            System.out.println(color + nrOfPassengers + " passengers arrived at station at: "
                    + (System.currentTimeMillis() - STARTTIME) + " ms.");
            waitingPeople.signalAll();

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Ask for nrOfPassengers Passengers to leave the station
     *
     * @param requestedNrOfPassengers
     * @return number of passengers actually leaving
     */
    public int leaveStation(int requestedNrOfPassengers) {
        lock.lock();
        int actuallyLeaving = 0;
        try {
            while (waitingPassengers() == 0) {
                waitingPeople.await();
            }
            actuallyLeaving = Math.min(requestedNrOfPassengers, nrOfPassengersAtStation);
            nrOfPassengersAtStation -= actuallyLeaving;
            if (waitingPassengers() == 0) {
                noPeople.signalAll();
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        } finally {
            lock.unlock();
        }
        return actuallyLeaving;
    }

    public int waitingPassengers() {
        return nrOfPassengersAtStation;
    }

    public void close() {
        isClosed = true;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public int getTotalNrOfPassengers() {
        return totalNrOfPassengers;
    }
}
