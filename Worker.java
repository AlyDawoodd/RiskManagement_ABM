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
    public boolean individualLearning(double forgetting, double experimenting, double N) {
		
        if (this.action == true) {
     qReport = qReport * (1 - forgetting) + ( this.utility * (1 - experimenting));
     qDontReport = qDontReport * (1 - forgetting)+ this.utility*experimenting/(N-1);
        } else if (this.action == false) {
            qDontReport = qDontReport * (1 - forgetting) + (this.utility * (1 - experimenting));
            qReport = qReport * (1 - forgetting)+this.utility*experimenting/(N-1);
        }
	  if(this.utility>0){

        pReport = qReport / (qReport + qDontReport);
        pDontReport = qDontReport / (qReport + qDontReport);
	  }
	  else{
		 pReport =Math.exp(qReport)/Math.exp(qReport + qDontReport);
		 pDontReport=Math.exp(qDontReport)/Math.exp(qReport + qDontReport);
		
	  }
        Pi = Math.random();
      //  System.out.println("P: " + Pi + "   pReport:" + pReport + "  pDontReport:" + pDontReport);


        if(pReport==pDontReport){
            return this.action;
        }
        else if (pDontReport <= pReport && Pi < pDontReport) { // pDontReport LOWER THAN pReport AND P LOWER THAN pDontReport SO Dont report

            return false;
        }
      else   if (pDontReport <= pReport && Pi > pDontReport) {// pDontReport LOWER THAN pReport AND P HIGHER THAN pDontReport SO Report

            return true;
        }
        else if (pReport <= pDontReport && Pi < pReport) {// pReport LOWER THAN pDontReport AND P LOWER THAN pReport SO Report

            return true;
        } else { // pReport LOWER THAN pDontReport AND P HIGHER THAN pReport SO Dont report

            return false;
        }


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