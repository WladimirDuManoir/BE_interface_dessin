import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

import java.awt.geom.Point2D;

/**
 * This Class makes the relationship between ivy and the palette.
 * @author rooty
 */


public class IvyPaletteAgent {
    private Ivy bus;
    //private static Stroke stroke; // TODO delete
    private String lastReleaseX;
    private String lastReleaseY;
   // private Gestes gestes; // TODO delete
    private Controller c;
    private String obj;

    public String getObjColor() {
        return objColor;
    }

    public void setObjColor(String objColor) {
        this.objColor = objColor;
    }

    private String objColor;

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        System.out.println("Obj set :: " + obj);
        this.obj = obj;
    }

    public IvyPaletteAgent() throws IvyException { // TODO Put stroke in para
        // stroke = new Stroke();
        // stroke.init();
        bus = new Ivy("IvyPaletteAgent", "IvyPaletteAgent Ready", null);
        prete();
        obj = "Not-defined";
        setObjColor("Not-defined");
        // gestes = new Gestes(); // TODO in controller
        mousePressed();
        mouseReleased();
        mouseDragged();
        bus.start(null);
        lastReleaseX = "0";
        lastReleaseY = "0";
        ResultatPoint();
        SetObjectInfo();
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

                try {
                    c.setPosX(Integer.parseInt(x));
                    c.setPosY(Integer.parseInt(y));
                    c.newMovement();
                    c.getStroke().init();
                    bus.sendMsg("Mouse Pressed. x=" + x + " " + "y=" + y);
                    c.getStroke().addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));
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
                    c.getStroke().addPoint(new Point2D.Double (Double.parseDouble(lastReleaseX),Double.parseDouble(lastReleaseY)));
                    c.analyseStroke(c.getStroke());
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
                c.getStroke().addPoint(new Point2D.Double (Double.parseDouble(x),Double.parseDouble(y)));

            }
        });
    }

    /**
     * This check if the palette has a figure in a specific point.
     * @throws IvyException
     */
    private void ResultatPoint() throws IvyException {
        bus.bindMsg(".*Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                setObj(args[2]);
            }
        });
    }

    public void SetObjectInfo() throws IvyException {
        bus.bindMsg(".*Palette:Info nom=.* x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                System.out.println("x=" + args[0] + " y=" + args[0] + " longueur=" + args[0] + " hauteur=" + args[0] + " couleurFond=" + args[0] + " couleurContour=" + args[0] );
                setObjColor(args[5]);
            }
        });
    }

    public String GetColorOnSelectedObjet() throws IvyException {
        getSelectedObject();
        System.out.println("-----------------Demande info pour l'object " + getObj() );
        bus.sendMsg("Palette:DemanderInfo nom=" + getObj());
        return getObjColor();
    }

    /**
     * This check if the palette has a figure in a specific point.
     * @throws IvyException
     */
    public void testPoint() throws IvyException {
        bus.sendMsg("Palette:TesterPoint x=" + c.getPosX() + " y=" + c.getPosY());
    }

    public String getSelectedObject() {
        try {
            testPoint();
        } catch (IvyException e) {
            e.printStackTrace();
        }
        return getObj();
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
            e.printStackTrace();
        }
    }

    public void register (Controller c) {
        this.c = c;
    }

    // create shapes
    public void delete(String obj) throws IvyException {
        getSelectedObject();
        System.out.println("Palette:SupprimerObjet nom=" + obj);
        bus.sendMsg("Palette:SupprimerObjet nom=" + obj);
    }

    public void creerObjet(Controller.Object obj, Controller.Color color, Point2D.Double position) {
        String x = Integer.toString((int) position.getX());
        String y = Integer.toString((int) position.getY());
        String couleur = null;
        switch(color){
            case BLEU:
                couleur="Blue";
                break;
            case ROUGE:
                couleur="Red";
                break;
            case VERT:
                couleur="Green";
                break;
            case JAUNE:
                couleur="Yellow";
                break;
            case NULL:
                couleur="Orange";
                break;
            default:
                couleur="Pink";
                break;
        }
        switch(obj){
            case ELLIPSE:
                try {
                    bus.sendMsg("Palette:CreerEllipse x="+x+" y="+y+" longueur=100 couleurFond="+couleur);
                } catch (IvyException e) {
                    e.printStackTrace();
                }
                break;
            case RECTANGLE:
                try {
                    bus.sendMsg("Palette:CreerRectangle x="+x+" y="+y+" longueur=100 couleurFond="+couleur);
                } catch (IvyException e) {
                    e.printStackTrace();
                }
                break;
            case OBJECT:
                break;
            case NULL:
                break;
            default:
                break;
        }
        System.out.println("Creation - obj : " + obj + " " + couleur + " "+ x + " "+ y);
    }

    public void creerObjet(Controller.Object obj) throws IvyException {
        Point2D.Double position = new Point2D.Double(20, 20);
        creerObjet(obj, Controller.Color.VERT, position);
    }

    public void creerObjet(Controller.Object obj, Controller.Color color) throws IvyException {
        Point2D.Double position = new Point2D.Double(20, 20);
        creerObjet(obj, color, position);
    }

    public void creerObjet(Controller.Object obj, Point2D.Double position) throws IvyException {
        creerObjet(obj, Controller.Color.VERT, position);
    }



        public void move(String obj)  throws IvyException {
            System.out.println("Moved " + obj);
        bus.sendMsg("Palette:DeplacerObjet nom=" + obj + " x=" + c.getPosX() + " y=" + c.getPosY());
    }
}