import sim.engine.*;

import sim.field.continuous.*;

import sim.field.network.*;
import sim.util.Bag;
import sim.util.Double2D;

import java.util.*;

public class Workers extends SimState {
    public static Continuous2D yard = new Continuous2D(1.0, 100, 100);
    public static int reporters = 0;
    public static int sumOfReportesPerSim;
    public static int numWorkers = 50;
    public static int numReporters = 0;
    public static int numNonReporters = 0;
    public static double reward = 50.0;
    public static List<Worker> listWorkers = new ArrayList<Worker>();
    public static List<Double> reportersPerSimWithAvg = new ArrayList<>();
    public static List<Integer> reportersPerSimWithoutAvg = new ArrayList<>();
    public Game game;
    public Report report;
    public Network reporting = new Network(false);
    public boolean someoneCrashed = false;
    public static double individualForgetting = 0.75;
    public static double individualExperimenting = 0.6;

    public static int sampleSize = 1000;
     public static int T= sampleSize+1;





    public static boolean UI = false;
   // public static boolean Sameplayer = false;

    public int getNumReporters() {
        return numReporters;
    }

    public static int getNumWorkers() {
        return numWorkers;
    }

    public static double getReward() {
        return reward;
    }

    public static void setReward(double reward) {
        Workers.reward = reward;
    }

    public int getNumNonReporters() {
        return numNonReporters;
    }

    public static void printResults(String players, String numOfPlayer, String learning, char caseStudy, int sampleSize) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(players + "/" + numOfPlayer + "/" + learning + "/" + caseStudy + "/SampleSize" + sampleSize + ".csv");
            for (int y = 0; y < Workers.reportersPerSimWithAvg.size(); y++) {
                double temp = reportersPerSimWithAvg.get(y);
                outFile.format("%s%s", temp, '\n');
            }
        } catch (Exception e) {
            System.out.println("Error Printing Out");
        }
        outFile.close();
    }

    public double getRandomUniform(int min, int max) { //http://www.fredosaurus.com/notes-java/summaries/summary-random.html
        double n = random.nextInt(max + 1);
        while (n < min) {
            n = random.nextInt(max + 1);
        }
        return n;
    }

    public double getRandomGaussian(double M, double SD) { // https://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml
        Random r = new Random();
        return r.nextGaussian() * SD + M;
    }

    public void resetPlayCrash() {
        for (Worker worker : listWorkers) {
            worker.isPlaying = false;
            worker.isCrash = false;
        }
    }

    public Workers(long seed) {
        super(seed);

    }

    public void start() {
        super.start();
        //clearing before initializing the game
        yard.clear();
        reporting.clear();
        listWorkers.clear();

        //creating a game that decides the result of each round/step
        game = new Game();
        schedule.scheduleRepeating(game, 1, 1);

        report = new Report();
        schedule.scheduleRepeating(report, 3, 1); //result of the round's accident

        for (int i = 0; i < numWorkers; i++) { // creation of workers
            double cost = getRandomUniform(1, 5);
            double accountability = Math.random();
            boolean isReporting= random.nextBoolean();
            if(numReporters==numWorkers/2)
                isReporting=false;
            if(numNonReporters==numWorkers/2)
                isReporting=true;
            Worker worker = new Worker(isReporting, cost, accountability, i); //creating an agent
            if(worker.isReporter)
                numReporters++;
            else
                numNonReporters++;
            //this is to adjust the GUI
            int x = i / 5;
            int y = x * 5;
            yard.setObjectLocation(worker,
                    new Double2D(yard.getWidth() * 0.5 + (double) (i - y),
                            yard.getHeight() * 0.5 + (double) x + x * 0.5));

            listWorkers.add(worker); //add to list of workers
            reporting.addNode(worker); //add to network

            schedule.scheduleRepeating(worker, 2, 1); //schedule workers as the second thing to run
        }

        //Collections.shuffle(listWorkers); // shuffling the list
        //define  relationships
        Bag workers = reporting.getAllNodes();
        for (int i = 0; i < workers.size(); i += 5) {
            //we take the first worker out of every five workers
            Object worker = workers.get(i);

            Object workerB = null;
            //we then take the next four players and connect them to him
            for (int j = 1; j < 5; j++) {
                workerB = workers.get(i + j);
                reporting.addEdge(worker, workerB, 1);//EDIT HERE
            }
        }
    }

    public static void main(String[] args) {
        UI = true;
        //int sampleSize = 1000;
        char caseStudy = '2';
        float sum;
        float avg;

        String learning = "Social";
        String numOfPlayers = "10players";
        String players = "DiffPlayers";
        int simulationNumber = 1;
        for (int i = 0; i < sampleSize; i++) {
            reportersPerSimWithoutAvg.add(0);
        }
        for (int i = 0; i < simulationNumber; i++) {
            SimState state = new Workers(System.currentTimeMillis());

           // System.out.println(T);
            state.start();
            do {
                //System.out.println(state.schedule.getSteps());
               T= Worker.updateT(T);
               // System.out.println(T);
                if (!state.schedule.step(state)) break;
            }
            while (state.schedule.getSteps() < sampleSize);

            state.finish();
            System.out.println(numReporters);
        }
        for (int i = 0; i < reportersPerSimWithoutAvg.size(); i++) {
            sum = reportersPerSimWithoutAvg.get(i);
            avg = sum / simulationNumber;
            reportersPerSimWithAvg.add(Math.floor(avg + 0.5));
        }
       // System.out.println(reportersPerSimWithAvg);
        //  printResults(players, numOfPlayers, learning, caseStudy, sampleSize);
        System.exit(0);
    }
}
