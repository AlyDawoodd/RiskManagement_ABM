import sim.engine.SimState;
import sim.engine.Steppable;

public class Worker implements Steppable, Comparable<Worker> {
    int id;
    public boolean isReporter;
    int timeCrash=0;
    boolean isPlaying = false;
    boolean isCrash = false;
    public double cost;
    public double reward=10.0;
    public int number;
    public double utility = 0;
    double accountability;


    public double getCost() {
        return cost;
    }

    public boolean getReporter() {
        return isReporter;
    }

    public double getUtility() {
        return utility;
    }

    public int getRandomInt(double max) { //https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
        return (int) Math.floor(Math.random() * max);
    }

    public int getId() {
        return id;
    }

    public int getIndex(Worker worker) {
        for (int i = 0; i < Workers.listWorkers.size(); i++) {
            if (Workers.listWorkers.get(i) == worker)
                return i;
        }
        return -1;
    }

    public Worker(boolean isReporter, double cost, double accountability,int id) {
        this.isReporter = isReporter;
        this.cost = cost;
        this.accountability=accountability;
        this.id=id;
    }

    @Override
    public void step(SimState state) {
        if(this.isPlaying){
            Workers workers = (Workers) state;
            //insert here your actions and equations
        }
    }

    @Override
    public int compareTo(Worker o) {
        return (int) (o.utility - this.utility);
    }
}