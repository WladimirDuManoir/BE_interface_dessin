import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class Audio implements IvyMessageListener {

    private Ivy bus;

    public Audio() throws IvyException {
        System.out.println("Audio Sarted");
        bus = new Ivy("IvyTranslater","IvyTranslater Ready",null);
        bus.bindMsg(".*Position.*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                s = s.replace(',', '.');
                float f = Float.parseFloat(s);
                if (f >= 0.8) {
                    move();
                } else {
                    rienCompris(bus);
                }
            }
        });
        bus.bindMsg(".*Color :(.*) .*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                System.out.println(args[1]);
                String couleur = args[0];
                String confiance = args[1];
                confiance = confiance.replace(',', '.');
                float f = Float.parseFloat(confiance);
                if (f >= 0.8) {
                    color(couleur);
                }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.bindMsg(".*Object :(.*).*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                String confiance = args[1];
                confiance = confiance.replace(',', '.');
                float f = Float.parseFloat(confiance);
                if (f >= 0.8) {
                    object(s);
                }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.start(null);
    }

    private void rienCompris(Ivy bus) {
        System.out.println("Rien compris");
        try {
            bus.sendMsg("ppilot5 Say=\"I don't understand\"");
        } catch (IvyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(IvyClient ivyClient, String[] strings) {

    }


    // In Controler

    public enum Color {
        ROUGE, JAUNE, VERT
    }

    public enum Object {
        OBJECT, RECTANGLE, ELLIPSE
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
        System.out.println("Color _" + color+"_");
        if (color.equals("rouge")) {
            return Color.ROUGE;
        } else if (color.equals("jaune")) {
            return Color.JAUNE;
        } else if (color.equals("vert")) {
            return Color.VERT;
        }
        return Color.ROUGE;
    }

    /**
     *
     * @param objet
     */
    public Object object(String objet){
        System.out.println("Object _" + objet+"_");
        if (objet.equals("rouge")) {
            return Object.OBJECT;
        } else if (objet.equals("jaune")) {
            return Object.RECTANGLE;
        } else if (objet.equals("vert")) {
            return Object.ELLIPSE;
        }
        return Object.OBJECT;
    }

}
