import fr.dgac.ivy.IvyException;

/**
 * Created by rooty on 22/01/2016.
 * Manages all the interactions between the different modules.
 */
public class Controller {

    // TODO Create full option for mouvements
    // TODO Create emum for the diff states
    // Create Automate
    // do audio interaction

    public enum Action {
        DELETE, RECTANGLE, ELLIPSE, MOVE, NOTHING
    }

    private Action action;
    private Stroke stroke;
    private IvyPaletteAgent paletteAgent;
    private Gestes gestes;

    public Controller() {
        action = Action.NOTHING;
        gestes = new Gestes();
        this.stroke = new Stroke();
        stroke.init();
        try {
            this.paletteAgent = new IvyPaletteAgent();
        } catch (IvyException e) {
            e.printStackTrace();
        }
        paletteAgent.register(this);
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    private void determinerStrokeGests() throws IvyException {
        switch (gestes.determinerStroke(stroke)) {
            case 1 :
                System.out.println("Deplacer");
                action = Action.MOVE;
                break;
            case 2 :
                System.out.println("Rectangle");
                action = Action.RECTANGLE;
                ApplyOnShape("rectangle");
                break;
            case 3 :
                System.out.println("Ellipse");
                action = Action.ELLIPSE;
                ApplyOnShape("eclipse");
                break;
            case 4 :
                System.out.println("Supprimer");
                action = Action.DELETE;
                break;
            default:
                action = Action.NOTHING;
        }
    }

    private void ApplyOnShape (String obj) throws IvyException {
        switch (action) {
            case DELETE:
                System.out.println("DELETE "+ obj);
                paletteAgent.delete(obj);
                // TODO delete object in model
                break;
            case RECTANGLE:
                System.out.println("CREATION REC "+ obj);
                paletteAgent.creatRec(obj);
                // TODO add object in model
                break;
            case ELLIPSE:
                System.out.println("CREATION ELLIPSE "+ obj);
                paletteAgent.creatEllipse(obj);
                // TODO add object in model
                break;
            case MOVE:
                System.out.println("MOVE "+ obj);
                paletteAgent.move(obj);
                // TODO update object in model
                break;
            case NOTHING:
                System.out.println("NONE SUPPORTED"+ obj);
                break;
        }
    }

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
        gestes.setTemplate();
        // afficher(stroke);
        determinerStrokeGests();
    }

    public void newMovement () throws IvyException {
        stroke.centroid =  stroke.calculCentroid();
        analyseStroke(stroke);
    }
}
