import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Report implements Steppable {

    public static List<Worker> currentWorkers = new ArrayList<Worker>();
    public int numReporters = 0;
    Worker crashedWorker = null;
    double costProbability = 0;

    public void step(SimState state) {
        currentWorkers.clear();
        numReporters = 0;
        Workers workers = (Workers) state;
        double reward = 100;
        double actualV = reward / numReporters;

        if (workers.someoneCrashed) {
            //decide how many of the four coworkers reported
            for (Worker worker : Workers.listWorkers) {
                if (worker.isPlaying) {
                    currentWorkers.add(worker);
                    if (worker.isReporter && !worker.isCrash)
                        numReporters++;
                    if (worker.isCrash)
                        crashedWorker = worker;
                }
            }

            int firstReporter = ThreadLocalRandom.current().nextInt(0, 5); //0 to 4
            if (crashedWorker.accountability >= 0.5 && crashedWorker.id % 5 == firstReporter) {
                //0 1 2 3 4         divide by 5 == //0 1 2 3 4
                //5 6 7 8 9         divide by 5 == //0 1 2 3 4
                //10 11 12 13 14    divide by 5 == //0 1 2 3 4
                if (crashedWorker.timeCrash == 1) {
                    crashedWorker.utility = -crashedWorker.cost; //COST?
                } else {
                    //Punishment for the incident
                    crashedWorker.utility = -reward - crashedWorker.cost;

                }
                for (Worker worker : currentWorkers) {
                    worker.utility = -worker.cost;
                }

            } else {
                for (Worker worker : currentWorkers
                ) {
                    worker.utility = actualV - worker.cost;
                }
                crashedWorker.utility = -reward;
            }
            workers.someoneCrashed = false;
        }
    }
}

