import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.*;

import sim.field.continuous.*;

import sim.field.network.*;
import sim.util.Bag;
import sim.util.Double2D;



public class Worker  implements Steppable{
    int id;
    public boolean isReporter;
    int timeCrash = 0;
    boolean isPlaying = false;
    boolean isCrash = false;
    public double cost;
    public int number;
    public double utility=0;
    double accountability;
    double qReport = 1;
    double qDontReport = 1;
    double pReport = 0.5;
    double pDontReport = 0.5;
    double Pi;
    double Ps;
    double probAcc;

     public static int updateT(int t){
         if(t!=1)
           t--;
         return t;
     }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        if(cost>=0) this.cost = cost;
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

    public boolean individualLearning(double forgetting, double experimenting, double N, int T) {

        System.out.println(T);
        if (this.isReporter) {
            qReport = qReport * (1 - forgetting) + (this.utility * (1 - experimenting));
            qDontReport = qDontReport * (1 - forgetting) + this.utility * experimenting / (N - 1);
        } else {
            qDontReport = qDontReport * (1 - forgetting) + (this.utility * (1 - experimenting));
            qReport = qReport * (1 - forgetting) + this.utility * experimenting / (N - 1);
        }
        System.out.println(qReport);
        System.out.println(qDontReport);
        if((Math.exp(qReport/T)+ Math.exp(qDontReport/T))==0){ // avoiding 0 in the denominator-- WHEN QS are almost 0
            pReport = Math.exp(qReport / T);
            pDontReport = Math.exp(qDontReport / T);

        }
        else {
            pReport = Math.exp(qReport / T) / (Math.exp(qReport / T) + Math.exp(qDontReport / T));
            pDontReport = Math.exp(qDontReport / T) / (Math.exp(qReport / T) + Math.exp(qDontReport / T));
        }

        Pi = Math.random();
       System.out.println("Probabiity: " + Pi + "   pReport:" + pReport + "  pDontReport:" + pDontReport);


        if (pReport == pDontReport) {
            return this.isReporter;
        } else if (pDontReport < pReport && Pi < pDontReport) { // pDontReport LOWER THAN pReport AND P LOWER THAN pDontReport SO Dont report

            return false;
        } else if (pDontReport < pReport && Pi > pDontReport) {// pDontReport LOWER THAN pReport AND P HIGHER THAN pDontReport SO Report

            return true;
        } else if (pReport < pDontReport && Pi < pReport) {// pReport LOWER THAN pDontReport AND P LOWER THAN pReport SO Report

            return true;
        } else { // pReport LOWER THAN pDontReport AND P HIGHER THAN pReport SO Dont report

            return false;
        }


    }

    public Worker(boolean isReporter, double cost, double accountability, int id) {
        this.isReporter = isReporter;
        this.cost = cost;
        this.accountability = accountability;
        this.id = id;
    }

    @Override
    public void step(SimState state) {
        Workers workers = (Workers) state;

        //accountability function
        //action function
        //report or not
        //...
        if (this.isPlaying)
            this.isReporter = individualLearning(workers.individualForgetting, workers.individualExperimenting, 2,Workers.T);



    }



}