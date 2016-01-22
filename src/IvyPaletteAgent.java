import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

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
    private Stroke stroke;
    Stroke templateSupprimer = new Stroke();
    Stroke templateRectangle = new Stroke();
    Stroke templateEllipse = new Stroke();
    Stroke templateDeplacer = new Stroke();


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
        Double distance = 0.0;
        System.out.println("Analysing stroke ...." );
        stroke.normalize();
        setTemplate();
        determinerStroke(stroke);
    }


    private void determinerStroke(Stroke s){
        int formeReconnue = 1;
        Double d1 = calculerDistance(stroke, templateSupprimer);
        Double d2 = calculerDistance(stroke, templateRectangle);
        if (d2 < d1){
            formeReconnue = 2;
        }
        Double d3 = calculerDistance(stroke, templateEllipse);
        if ((d3 < d2) &&(d3 < d1)){
            formeReconnue = 3;
        }
        Double d4 = calculerDistance(stroke, templateDeplacer);
        if ((d4<d1)&&(d4<d2)&&(d4<d3)){
            formeReconnue = 4;
        }

        switch (formeReconnue){
            case 1 : System.out.println("Supprimer");
                break;
            case 2 : System.out.println("Rectangle");
                break;
            case 3 : System.out.println("Ellipse");
                break;
            case 4 : System.out.println("Deplacer");
                break;
        }

    }

    private Double calculerDistance(Stroke s1, Stroke s2){
        Double distance = 0.0;
        int i;
        Double x1;
        Double x2;
        Double y1;
        Double y2;
        Double ajout;
        for (i=0;i<s1.listePoint.size()-1;i++){
            x1 = s1.listePoint.get(i).getX();
            x2 = s2.listePoint.get(i).getX();
            y1 = s1.listePoint.get(i).getY();
            y2 = s2.listePoint.get(i).getY();
            ajout = Math.abs((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
            distance += ajout;
        }
        return distance;
    }

    /**
     * Set up the template (Liste of points).
     */
    private void setTemplate () {
        templateSupprimer = lireFichier("templateSupprimer");
        templateRectangle = lireFichier("templateRectangle");
        templateEllipse = lireFichier("templateEllipse");
        templateDeplacer = lireFichier("templateDeplacer");

    }


    private Stroke lireFichier(String nomFic){
        Stroke template = new Stroke();
        try
        {

            File f = new File (nomFic);
            Scanner scanner = new Scanner (f);

            scanner.useLocale(Locale.US);
            scanner.useDelimiter(", |Point2D.Double\\[|\\]Point2D.Double\\[");
            Double ptX;
            Double ptY;

            while (true)
            {
                try
                {
                    ptX = scanner.nextDouble();
                    ptY = scanner.nextDouble();
                    Point2D.Double point = new Point2D.Double(ptX,ptY);
                    template.listePoint.add(point);

                }
                catch (NoSuchElementException exception)
                {
                    break;
                }
            }

            scanner.close();
        }
        catch (FileNotFoundException exception)
        {
            System.out.println ("Le fichier n'a pas été trouvé");
        }
        return template;
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