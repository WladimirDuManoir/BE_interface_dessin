import fr.dgac.ivy.IvyException;

import java.util.Timer;
import java.util.TimerTask;

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
    public enum State {
        E_INIT,

        E_COULEUR,
        E_CREER_OBJET,
        E_POSITION,

        E_DEPLACER_OBJ,
        E_DEPLACER_POS,
        E_DEPLACER,

        E_SUPPRIMER_COL,
        E_SUPPRIMER
    }

    private Action action;
    private Stroke stroke;
    private IvyPaletteAgent paletteAgent;
    private Gestes gestes;
    private Audio audio;
    private State state;
    private Timer timer_couleur;
    private Timer timer_creerObjet;
    private Timer timer_position;
    private Timer timer_suprimer;

    public Controller() {
        state = State.E_INIT;
        action = Action.NOTHING;
        SetAllTimer();
        gestes = new Gestes();
        this.stroke = new Stroke();
        stroke.init();
        try {
            audio = new Audio();
        } catch (IvyException e) {
            e.printStackTrace();
        }
        try {
            this.paletteAgent = new IvyPaletteAgent();
        } catch (IvyException e) {
            e.printStackTrace();
        }
        paletteAgent.register(this);
        audio.register(this);
    }


    public void SetAllTimer() {
        timer_couleur = new Timer();
        timer_couleur.schedule(new TimerTaskColor(), 1000 * 1000);
        timer_creerObjet = new Timer();
        timer_creerObjet.schedule(new TimerTaskObject(), 1000 * 1000);
        timer_position = new Timer();
        timer_position.schedule(new TimerTaskPosition(), 1000 * 1000);
        timer_suprimer = new Timer();
        timer_suprimer.schedule(new TimerTaskSupprimer(), 1000 * 1000);
    }

    public void StopAllTimer() {
        timer_couleur.cancel();
        timer_creerObjet.cancel();
        timer_position.cancel();
        timer_suprimer.cancel();
    }

    class TimerTaskColor extends TimerTask {
        public void run() {
            System.out.println("Time's up! Color");
        }
    }

    class TimerTaskObject extends TimerTask {
        public void run() {
            System.out.println("Time's up! Obj");
        }
    }

    class TimerTaskPosition extends TimerTask {
        public void run() {
            System.out.println("Time's up! Pos");
        }
    }

    class TimerTaskSupprimer extends TimerTask {
        public void run() {
            System.out.println("Time's up! Sup");
        }
    }
    /**
     * For some states the Color and object attribut are not always used.
     * @param s
     * @param color
     * @param objet
     */
    private void goToState (State s, Color color, Object objet) {
        switch (s) {
            case E_INIT:
                System.out.println("Go to state INIT");
                StopAllTimer();
                state = State.E_INIT;
                break;
            case E_COULEUR:
                System.out.println("Go to state COULEUR");
                StopAllTimer();
                state = State.E_COULEUR;
                break;
            case E_CREER_OBJET:
                System.out.println("Go to state CREEROBJET");
                StopAllTimer();
                state = State.E_CREER_OBJET;
                break;
            case E_POSITION:
                System.out.println("Go to state POSITION");
                StopAllTimer();
                state = State.E_POSITION;
                break;
            case E_DEPLACER_OBJ:
                System.out.println("Go to state DEPLACER_OBJ");
                StopAllTimer();
                state = State.E_DEPLACER_OBJ;
                break;
            case E_DEPLACER_POS:
                System.out.println("Go to state DEPLACER_POS");
                StopAllTimer();
                state = State.E_DEPLACER_POS;
                break;
            case E_DEPLACER:
                System.out.println("Go to state DEPLACER");
                StopAllTimer();
                state = State.E_DEPLACER;
                break;

            case E_SUPPRIMER_COL:
                // TODO activation du timer timer_sup.start
                System.out.println("Go to state SUPPRIMERCOL");
                StopAllTimer();
                state = State.E_SUPPRIMER_COL;
                break;
            case E_SUPPRIMER:
                System.out.println("Go to state SUPPRIMER");
                StopAllTimer();
                state = State.E_SUPPRIMER;
                break;
            default:
                System.out.println("Not in State Ennum");
                StopAllTimer();
                break;
        }
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    private void determinerStrokeGestes() throws IvyException {
        switch (gestes.determinerStroke(stroke)) {
            case 1 :
                System.out.println("Supprimer");
                goToState(State.E_SUPPRIMER, Color.NULL, Object.NULL);

                break;
            case 2 :
                System.out.println("Rectangle");
                goToState(State.E_CREER_OBJET, Color.NULL, Object.RECTANGLE);
                break;
            case 3 :
                System.out.println("Ellipse");
                goToState(State.E_CREER_OBJET, Color.NULL, Object.ELLIPSE);
                break;
            case 4 :
                System.out.println("Deplacer");
                goToState(State.E_DEPLACER, Color.NULL, Object.NULL);
                break;
            default:
        }
    }


    /**
     * Analyse and compare the template and the stroke.
     * @param stroke
     */
    public  void analyseStroke (Stroke stroke) throws IvyException {
        int i;
        int nbPts=0;
        Double distance = 0.0;
        System.out.println("Analysing stroke ...." );
        stroke.normalize();
        gestes.setTemplate();
        determinerStrokeGestes();

    }

    public void newMovement () throws IvyException {
        this.stroke = new Stroke();
        stroke.init();
    }

    // Audio

    public enum Color {
        ROUGE, JAUNE, VERT, BLEU , NULL
    }

    public enum Object {
        OBJECT, RECTANGLE, ELLIPSE, NULL
    }

    /**
     *
     */
    public void move() {
        System.out.println("Move");
    }

    /**
     *
     * @param color
     */
    public Color color(String color){
        System.out.println("Color _" + color + "_");
        if (color.equals("rouge")) {
            return Color.ROUGE;
        } else if (color.equals("jaune")) {
            return Color.JAUNE;
        } else if (color.equals("vert")) {
            return Color.VERT;
        } else if (color.equals("bleu")){
            return Color.BLEU;
        }
        return Color.ROUGE;
    }

    /**
     * Call went recived an audio evenements.
     * @param objet
     */
    public void object(String objet){
        System.out.println("Object _" + objet + "_");
        if (objet.equals("cet objet ")) {
            switch (state) {
                case E_INIT:
                    break;
                case E_COULEUR:
                    break;
                case E_CREER_OBJET:
                    break;
                case E_POSITION:
                    break;

                case E_DEPLACER_OBJ:
                    break;
                case E_DEPLACER_POS:
                    break;
                case E_DEPLACER:
                    break;
                case E_SUPPRIMER_COL:
                    break;
                case E_SUPPRIMER:
                    goToState(state.E_SUPPRIMER_COL, Color.NULL, Object.OBJECT);
                    break;
                default:
                    System.out.println("Not in State Ennum");
                    break;
            }
        } else if (objet.equals("ce rectangle")) {
            switch (state) {
                case E_INIT:
                    break;
                case E_COULEUR:
                    break;
                case E_CREER_OBJET:
                    break;
                case E_POSITION:
                    break;

                case E_DEPLACER_OBJ:
                    break;
                case E_DEPLACER_POS:
                    break;
                case E_DEPLACER:
                    break;

                case E_SUPPRIMER_COL:
                    break;
                case E_SUPPRIMER:
                    goToState(state.E_SUPPRIMER_COL, Color.NULL, Object.RECTANGLE);
                    break;
                default:
                    System.out.println("Not in State Ennum");
                    break;
            }
        } else if (objet.equals("cette ellipse")) {
            switch (state) {
                case E_INIT:
                    break;
                case E_COULEUR:
                    break;
                case E_CREER_OBJET:
                    break;
                case E_POSITION:
                    break;

                case E_DEPLACER_OBJ:
                    break;
                case E_DEPLACER_POS:
                    break;
                case E_DEPLACER:
                    break;

                case E_SUPPRIMER_COL:
                    break;
                case E_SUPPRIMER:
                    goToState(state.E_SUPPRIMER_COL, Color.NULL, Object.ELLIPSE);
                    break;
                default:
                    System.out.println("Not in State Ennum");
                    break;
            }
        }
    }
}
