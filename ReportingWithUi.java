import sim.portrayal.network.*;
import sim.portrayal.continuous.*;
import sim.engine.*;
import sim.display.*;
import sim.portrayal.simple.*;
import sim.portrayal.*;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;

public class ReportingWithUi extends GUIState {
    public Display2D display;
    public JFrame displayFrame;
    ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
    NetworkPortrayal2D buddiesPortrayal = new NetworkPortrayal2D();

    public static void main(String[] args) {
        ReportingWithUi vid = new ReportingWithUi();
        Console c = new Console(vid);
        c.setVisible(true);
    }

    public ReportingWithUi() {
        super(new Workers(System.currentTimeMillis()));
    }

    public ReportingWithUi(SimState state) {
        super(state);
    }

    public static String getName() {
        return "Risk Management Game";
    }

    public Object getSimulationInspectedObject() {
        return state;
    }

    public Inspector getInspector() {
        Inspector i = super.getInspector();
        i.setVolatile(true);
        return i;
    }

    public void start() {
        super.start();
        setupPortrayals();
    }

    public void load(SimState state) {
        super.load(state);
        setupPortrayals();
    }

    public void setupPortrayals() {
        Workers workers = (Workers) state;
// tell the portrayals what to portray and how to portray them
        yardPortrayal.setField(Workers.yard);
        yardPortrayal.setPortrayalForAll(new OvalPortrayal2D(){
            public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
            {
                Worker worker = (Worker) object;

                if(worker.isPlaying)
                    paint = new Color(0, 255, 0);
                else paint  = new Color(0, 128, 128) ;
                if(worker.isCrash)
                    paint = new Color(255, 0, 0);
                super.draw(object, graphics, info);
            }
        });
        buddiesPortrayal.setField(new SpatialNetwork2D(Workers.yard, workers.reporting));
        buddiesPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());
// reschedule the displayer
        display.reset();
        display.setBackdrop(Color.white);
// redraw the display
        display.repaint();
    }

    public void init(Controller c) {
        super.init(c);
// make the displayer
        display = new Display2D(600, 600, this);
// turn off clipping
        display.setClipping(false);
        displayFrame = display.createFrame();
        displayFrame.setTitle("Risk Management Display");
        c.registerFrame(displayFrame);
        displayFrame.setVisible(true);
        display.attach(buddiesPortrayal, "Buddies");
        display.attach(yardPortrayal, "Yard");
    }

    public void quit() {
        super.quit();
        if (displayFrame != null) displayFrame.dispose();
        displayFrame = null;
        display = null;
    }
}
