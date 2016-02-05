import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by ferreisi on 26/01/2016.
 */
public class AffichageTemplate extends JComponent {

    Stroke templateSupprimer = new Stroke();
    Stroke templateRectangle = new Stroke();
    Stroke templateEllipse = new Stroke();
    Stroke templateDeplacer = new Stroke();
    int i;


    public AffichageTemplate() {

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

    @Override
    public void paint(Graphics g) {

        for (i=0;i<templateDeplacer.listePoint.size()-1;i++){
            g.drawLine((int) templateDeplacer.getPoint(i).getX(),(int) templateDeplacer.getPoint(i).getY(),(int) templateDeplacer.getPoint(i+1).getX(),(int) templateDeplacer.getPoint(i+1).getY());
        }
    }
}
