import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This Class makes the relationship between ivy and the palette.
 * @author rooty
 */


public class IvyPaletteAgent {
    private Ivy bus;
    private static Stroke stroke; // TODO delete
    private  Action action;
    private String lastReleaseX;
    private String lastReleaseY;
    private Gestes gestes; // TODO delete


    public enum Action {
        DELETE, RECLANGLE, ELLIPSE, MOVE, NOTHING
    }


    public IvyPaletteAgent() throws IvyException { // TODO Put stroke in para
        stroke = new Stroke();
        stroke.init();
        bus = new Ivy("IvyPaletteAgent", "IvyPaletteAgent Ready", null);
        prete();
        gestes = new Gestes(); // TODO in controller
        mousePressed();
        mouseReleased();
        mouseDragged();
        testPoint();
        testRectangle();
        bus.start(null);
        action = Action.NOTHING;
        lastReleaseX = "0";
        lastReleaseY = "0";
        testRectangle();
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

    /**
     * This check if the palette has reseed a mouse pressed.
     * @throws IvyException
     */
    private void mousePressed() throws IvyException {
        bus.bindMsg(".*MousePressed x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String x = args[0];
                String y = args[1];
                stroke.init();
                try {
                    bus.sendMsg("Mouse Pressed. x=" + x + " " + "y=" + y);
                    stroke.addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This check if the palette has reseed a mouse released.
     * @throws IvyException
     */
    private void mouseReleased() throws IvyException {
        bus.bindMsg(".*MouseReleased x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                lastReleaseX = args[0];
                lastReleaseY = args[1];

                try {
                    bus.sendMsg("Mouse released. x=" + lastReleaseX + " " + "y=" + lastReleaseY);
                    stroke.addPoint(new Point2D.Double (Double.parseDouble(lastReleaseX),Double.parseDouble(lastReleaseY)));
                    stroke.centroid = stroke.calculCentroid();
                    analyseStroke(stroke);
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This check if the palette has reseed a mouse dragged.
     * @throws IvyException
     */
    private void mouseDragged() throws IvyException {
        bus.bindMsg(".*MouseDragged x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String x = args[0];
                String y = args[1];
                System.out.println("x:"+x+",y"+y);
                //bus.sendMsg("Mouse dragged. x=" + x + " " + "y=" + y);
                stroke.addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
            }
        });
    }

    /**
     * This check if the palette has a figure in a specific point.
     * @throws IvyException
     */
    private void testPoint() throws IvyException {
        bus.bindMsg(".*Palette:ResultatTesterPoint.*nom=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String obj = args[0];
                try {
                    ApplyOnShape(obj);
                } catch (IvyException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // TODO PUT IN Controller
    /**
     * Analyse and compare the template and the stroke.
     * @param stroke
     */
    private void analyseStroke (Stroke stroke) throws IvyException {
        int i;
        int nbPts=0;
        Double distance = 0.0;
        System.out.println("Analysing stroke ...." );
        stroke.normalize();
        gestes. setTemplate();
       // afficher(stroke);
        determinerStrokeGests();

    }

    private void afficher(Stroke stroke) {
        AffichageStroke affichageTemplate = new AffichageStroke(stroke);
        JFrame affichageFrame = new JFrame();
        affichageFrame.add(affichageTemplate);
        affichageFrame.setVisible(true);

    }

    // TODO PUT IN Controller
    private void determinerStrokeGests() throws IvyException {

        switch (gestes.determinerStroke(stroke)){
            case 1 : System.out.println("Supprimer");
                action = Action.DELETE;
                break;
            case 2 : System.out.println("Rectangle");
                action = Action.RECLANGLE;
                ApplyOnShape("rectangle");
                break;
            case 3 : System.out.println("Ellipse");
                action = Action.ELLIPSE;
                ApplyOnShape("eclipse");
                break;
            case 4 : System.out.println("Deplacer");
                action = Action.MOVE;
                break;
            default:
                action = Action.NOTHING;
        }

    }

    // TODO PUT IN Controller
    private void ApplyOnShape (String obj) throws IvyException {
        bus.sendMsg("Palette:CreerEllipse x=30 y=30 longueur=100");

        switch (action) {
            case DELETE:
                System.out.println("DELETE "+ obj);
                bus.sendMsg("Palette:SupprimerObject nom=" + obj);
                // TODO delete object in model
                break;
            case RECLANGLE:
                System.out.println("CREATION REC "+ obj);
                bus.sendMsg("Palette:CreerRectangle x=30 y=30 longueur=100");
                // TODO add object in model
                break;
            case ELLIPSE:
                System.out.println("CREATION ELLIPSE "+ obj);
                bus.sendMsg("Palette:CreerEllipse x=30 y=30 longueur=100");
                // TODO add object in model
                break;
            case MOVE:
                System.out.println("MOVE "+ obj);
                bus.sendMsg("Palette:DeplacerObjet nom=" + obj + " x=300");
                // TODO update object in model
                break;
            case NOTHING:
                System.out.println("NONE SUPPORTED"+ obj);
                break;

        }
    }

    private void testRectangle () {
        try {
            bus.sendMsg("Palette:CreerRectangle x=90");
            bus.sendMsg("Palette:CreerRectangle x=100");
            bus.sendMsg("Palette:CreerRectangle x=400");
            bus.sendMsg("Palette:CreerRectangle x=30");
            bus.sendMsg("Palette:CreerRectangle x=80");
            bus.sendMsg("Palette:CreerRectangle x=200");
            bus.sendMsg("Palette:CreerRectangle x=10");
        } catch (IvyException e) {
            System.out.println("dfgdhdfhgdxfhgdfhxdf");
            e.printStackTrace();
        }
    }
}