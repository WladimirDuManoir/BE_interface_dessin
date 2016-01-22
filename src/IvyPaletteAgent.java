import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This Class makes the relationship between ivy and the palette.
 * @author rooty
 */

public class IvyPaletteAgent {
    private Ivy bus;
    private Stroke stroke;

    public IvyPaletteAgent() throws IvyException {
        stroke = new Stroke();
        stroke.init();
        bus = new Ivy("IvyPaletteAgent", "IvyPaletteAgent Ready", null);
        prete();
        mousePressed();
        mouseReleased();
        mouseDragged();
        testPoint();
        testRectangle();
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

    private void mouseReleased() throws IvyException {
        bus.bindMsg(".*MouseReleased x=(.*) y=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String x = args[0];
                String y = args[1];
                try {
                    bus.sendMsg("Mouse released. x=" + x + " " + "y=" + y);
                    stroke.addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
                    stroke.centroid = stroke.calculCentroid();
                    analyseStroke(stroke);
                    bus.sendMsg("Palette:CreerRectangle x=30");
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

    private void testPoint() throws IvyException {
        bus.bindMsg(".*Palette:ResultatTesterPoint.*nom=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                String obj = args[0];
                System.out.println("Test point detected" );
                try {
                    bus.sendMsg("My name is : " + obj);
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
    private void analyseStroke (Stroke stroke) throws IvyException {
        System.out.println("Analysing stroke ...." );
        stroke.normalize();
        setTemplate(stroke);
    }

    /**
     * Set up the template (Liste of points).
     */
    private void setTemplate (Stroke stroke) {
        System.out.println("Creating template .. ");
        lireFichier("templateSupprimer");
    }


    private void lireFichier(String nomFic){
    System.out.println("blabla");
        try
        {

            File f = new File (nomFic);
            Scanner scanner = new Scanner (f);

            scanner.useDelimiter(",");
            Double ptX;
            Double ptY;

            while (true)
            {
                try
                {
                    ptX = scanner.nextDouble();
                    ptY = scanner.nextDouble();

                    System.out.println (ptX +","+ptY);
                }
                catch (NoSuchElementException exception)
                {
                    System.out.println("blabla2"+exception.toString()
                    );

                    break;
                }
            }

            scanner.close();
        }
        catch (FileNotFoundException exception)
        {
            System.out.println ("Le fichier n'a pas été trouvé");
        }

    }

    public void ecrireFichier(String nomFic, String texte)
    {
        //on va chercher le chemin et le nom du fichier et on me tout ca dans un String
        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;

        //on met try si jamais il y a une exception
        try
        {
            /**
             * BufferedWriter a besoin d un FileWriter,
             * les 2 vont ensemble, on donne comme argument le nom du fichier
             * true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus

             */
            FileWriter fw = new FileWriter(adressedufichier, true);

            // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
            BufferedWriter output = new BufferedWriter(fw);

            //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
            output.write(texte);
            //on peut utiliser plusieurs fois methode write

            output.flush();
            //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter

            output.close();
            //et on le ferme
            System.out.println("fichier créé");
        }
        catch(IOException ioe){
            System.out.print("Erreur : ");
            ioe.printStackTrace();
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