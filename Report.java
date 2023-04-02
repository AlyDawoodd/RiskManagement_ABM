import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;
import java.util.List;

public class Report implements Steppable {

    public static List<Worker> currentWorkers = new ArrayList<Worker>();
    public int numReporters = 0;
    Worker crashedWorker = null;
    double costProbability = 0;

    public void step(SimState state) {
        currentWorkers.clear();
        numReporters = 0;
        Workers workers = (Workers) state;

        if(workers.someoneCrashed){
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
            if (crashedWorker.accountability >= 0.5) {
                //this worker pays or not based on
                if(crashedWorker.timeCrash==1){
                    //nothing
                }
                //and so on


                //OR
                costProbability = (1-crashedWorker.timeCrash) * 0.25;
                if(costProbability > 1)
                    costProbability = 1;
                crashedWorker.cost = crashedWorker.accountability * costProbability; //or anything else
            }
            else{
                //now we use number of players who report for equations
            }

        }
        workers.someoneCrashed = false;

    }
}

