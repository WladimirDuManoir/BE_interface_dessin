import fr.dgac.ivy.IvyException;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by rooty on 05/02/2016.
 * Class to for gesture recognitions.
 */
public class Gestes {
    Stroke templateSupprimer = new Stroke();
    Stroke templateRectangle = new Stroke();
    Stroke templateEllipse = new Stroke();
    Stroke templateDeplacer = new Stroke();

    public Gestes() {
        setTemplate();
    }

    /**
     * Set up the template (List of points).
     */
    public void setTemplate () {
        templateSupprimer = lireFichier("templateSupprimer");
        templateRectangle = lireFichier("templateRectangle");
        templateEllipse = lireFichier("templateEllipse");
        templateDeplacer = lireFichier("templateDeplacer");
    }

    /**
     * This function can read the nomFic file.
     * @param nomFic name file.
     * @return the stroke.
     */
    public Stroke lireFichier(String nomFic) {
        Stroke template = new Stroke();
        try {
            File f = new File (nomFic);
            Scanner scanner = new Scanner (f);
            scanner.useLocale(Locale.US);
            scanner.useDelimiter(", |Point2D.Double\\[|\\]Point2D.Double\\[");
            Double ptX;
            Double ptY;
            while (true) {
                try {
                    ptX = scanner.nextDouble();
                    ptY = scanner.nextDouble();
                    Point2D.Double point = new Point2D.Double(ptX,ptY);
                    template.listePoint.add(point);
                }
                catch (NoSuchElementException exception) {
                    break;
                }
            }
            scanner.close();
        }
        catch (FileNotFoundException exception) {
            System.out.println ("Le fichier n'a pas été trouvé");
        }
        return template;
    }

    public void ecrireFichier(String nomFic, String texte) {
        //on va chercher le chemin et le nom du fichier et on me tout ca dans un String
        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic;

        //on met try si jamais il y a une exception
        try {
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
        catch(IOException ioe) {
            System.out.print("Erreur : ");
            ioe.printStackTrace();
        }

    }

    /**
     * Determine what gestier has been made.
     * @param s
     * @return the chosen action. (!) default move
     * @throws IvyException
     */
    public int determinerStroke(Stroke s) throws IvyException {
        Double d1 = calculerDistance(s,templateSupprimer);
        Double d2 = calculerDistance(s, templateRectangle);
        if (d2 < d1) {
            return 2;
        }
        Double d3 = calculerDistance(s, templateEllipse);
        if ((d3 < d2) && (d3 < d1)) {
            return 3;
        }
        Double d4 = calculerDistance(s, templateDeplacer);
        if ((d4 < d1) && (d4 < d2) && (d4 < d3)) {
            return 4;
        }
        return 1;
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
            ajout = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
            distance += ajout;
        }
        System.out.println(distance);
        return distance;
    }

}
