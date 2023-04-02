import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;
import sim.util.Bag;

import java.util.concurrent.ThreadLocalRandom;

public class Game implements Steppable {

    Worker mainWorker = null;

    public void step(SimState state) {

        Workers workers = (Workers) state;
        if (!workers.listWorkers.isEmpty()) {
            //we reset all workers to notplaying
            workers.resetPlayCrash();
            //we pick a random number
            int accidentNumber = ThreadLocalRandom.current().nextInt(0, (int) (workers.numWorkers * 1.25));                //we pick that accident maker
            if (accidentNumber < workers.numWorkers) {
                workers.someoneCrashed = true;
                Worker accidentMaker = workers.listWorkers.get(accidentNumber);
                //now we assign his isCrash to true to differentiate between him and other players
                accidentMaker.isCrash = true;
                accidentMaker.timeCrash++;

                //we take the network that this player is in
                Bag out = workers.reporting.getEdges(accidentMaker, null);
                //this is to reach the main node that is connected to four other nodes
                Edge e = (Edge) (out.get(0));
                mainWorker = (Worker) e.from();
                out = workers.reporting.getEdges(mainWorker, null);
                mainWorker.isPlaying = true;
                //to assign all other players playing as true
                for (int i = 0; i < 4; i++) {
                    e = (Edge) (out.get(i));
                    Worker tempWorker = (Worker) e.to();
                    tempWorker.isPlaying = true;
                }
            }

        }
    }
}

