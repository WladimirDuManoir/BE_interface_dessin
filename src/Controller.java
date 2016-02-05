import fr.dgac.ivy.IvyException;

/**
 * Created by rooty on 22/01/2016.
 * Manages all the interactions between the different modules.
 */
public class Controller {

    private Stroke lastStroke;
    private IvyPaletteAgent paletteAgent;

    public Controller() {
        // create reconnaissance de gestes

        this.lastStroke = new Stroke();
        try {
            this.paletteAgent = new IvyPaletteAgent();
        } catch (IvyException e) {
            e.printStackTrace();
        }
        // add listner and notifier for the ivyAgent
        // apply moved functions to the stroke created

    }


}
