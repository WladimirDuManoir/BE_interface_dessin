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
public class AffichageStroke extends JComponent {

    int i;
    Stroke stroke = new Stroke();


    public AffichageStroke(Stroke s) {
        stroke = s;
    }



    @Override
    public void paint(Graphics g) {

        for (i=0;i<stroke.listePoint.size()-1;i++){
            g.drawLine((int) stroke.getPoint(i).getX(),(int) stroke.getPoint(i).getY(),(int) stroke.getPoint(i+1).getX(),(int) stroke.getPoint(i+1).getY());
        }
    }
}