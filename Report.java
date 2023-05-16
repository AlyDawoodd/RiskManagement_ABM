import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Report implements Steppable {

    public static List<Worker> currentWorkers = new ArrayList<Worker>();
    int numReportersRound;
    Worker crashedWorker = null;
    double costProbability = 0;

    public void step(SimState state) {
        currentWorkers.clear();
        Workers.numReporters = Workers.numNonReporters = 0;
        for(Worker worker: Workers.listWorkers){
            if(worker.isReporter)
                Workers.numReporters++;
            else
                Workers.numNonReporters++;
        }
        numReportersRound = 0;
        Workers workers = (Workers) state;

        if (workers.someoneCrashed) {
            //decide how many of the four coworkers reported
            for (Worker worker : Workers.listWorkers) {
                if (worker.isPlaying) {
                    currentWorkers.add(worker);
                    if (worker.isReporter && !worker.isCrash)
                        numReportersRound++;
                    if (worker.isCrash)
                        crashedWorker = worker;
                }
            }
           // System.out.println(numReportersRound);
            if(numReportersRound==0)
                numReportersRound=1;
            double actualV = Workers.reward / numReportersRound;
           // System.out.println(numReportersRound);
           // System.out.println(actualV);

            int firstReporter = ThreadLocalRandom.current().nextInt(0, 5); //0 to 4
            //System.out.println(firstReporter);
            if (crashedWorker.accountability >= 0.5 && crashedWorker.id % 5 == firstReporter) {
                //0 1 2 3 4         divide by 5 == //0 1 2 3 4
                //5 6 7 8 9         divide by 5 == //0 1 2 3 4
                //10 11 12 13 14    divide by 5 == //0 1 2 3 4
               // System.out.println("hi");
                if (crashedWorker.timeCrash == 1) {
                    crashedWorker.utility =crashedWorker.utility -crashedWorker.cost; //COST?
                } else {
                    //Punishment for the incident
                    crashedWorker.utility = crashedWorker.utility-(Workers.reward) - crashedWorker.cost;

                }
                for (Worker worker : currentWorkers) {
                    if (worker.isReporter)
                        worker.utility =worker.utility-worker.cost;
                    else {
                        worker.utility=worker.utility;
                    }
                }

            } else {
                for (Worker worker : currentWorkers) {
                    if(worker.isReporter) {
                        worker.utility =worker.utility+  actualV - worker.cost;
                       // System.out.println(worker.utility);
                    }else {
                        worker.utility=worker.utility ;
                    }
                }
                crashedWorker.utility = crashedWorker.utility-(Workers.reward);
            }
            workers.someoneCrashed = false;

        }
    }
}

