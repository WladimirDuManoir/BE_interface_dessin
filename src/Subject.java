import java.awt.geom.Point2D;
import java.util.Observer;

/**
 * Created by rooty on 05/02/2016.
 */
public interface Subject {
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObservers(Point2D.Double p);
}

