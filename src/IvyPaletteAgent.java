import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

import java.awt.geom.Point2D;

/**
 * This Class makes the relationship between ivy and the palette.
 * @author rooty
 */

public class IvyPaletteAgent {
    private Ivy bus;
    private Stroke stroke;

    public IvyPaletteAgent() throws IvyException {
        stroke = new Stroke();
        bus = new Ivy("IvyPaletteAgent", "IvyPaletteAgent Ready", null);
        prete();
        mousePressed();
        mouseReleased();
        mouseDragged();
        bus.start(null);
    }

    /**
     * This check if the palette is ready.
     * @throws IvyException
     */
    private void prete() throws IvyException {
        bus.bindMsg(".*La palette est pr.*", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                try {
                    bus.sendMsg("Agent detected palette ready.");
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mousePressed() throws IvyException {
        bus.bindMsg(".*MousePressed x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String x = args[0];
                String y = args[1];
                stroke = new Stroke();
                try {
                    bus.sendMsg("Mouse Pressed. x=" + x + " " + "y=" + y);
                    stroke.addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mouseReleased() throws IvyException {
        bus.bindMsg(".*MouseReleased x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String x = args[0];
                String y = args[1];
                try {
                    bus.sendMsg("Mouse released. x=" + x + " " + "y=" + y);
                    stroke.addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
                    analyseStroke(stroke);
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mouseDragged() throws IvyException {
        bus.bindMsg(".*mouseDragged x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String x = args[0];
                String y = args[1];
                try {
                    bus.sendMsg("Mouse dragged. x=" + x + " " + "y=" + y);
                    stroke.addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Analyse and compare the template and the stroke.
     * @param stroke
     */
    private void analyseStroke (Stroke stroke) {
        System.out.println("Analysing stroke ....");
    }

    /**
     * Set up the template (Liste of points).
     */
    private void setTemplate () {

    }
}