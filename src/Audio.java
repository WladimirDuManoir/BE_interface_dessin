import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class Audio implements IvyMessageListener{

    private Ivy bus;
    private Frame frame;

    public Audio() throws IvyException {
        frame = new Frame();
        bus = new Ivy("IvyTranslater","IvyTranslater Ready",null);
        bus.bindMsg(".*cette position.*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                s = s.replace(',', '.');
                float f = Float.parseFloat(s);
                if (f >= 0.8) {
                    int res = frame.deplacerGauche();
                        try {
                            bus.sendMsg("ppilot5 Say=\"position understood\"");
                        } catch (IvyException e) {
                            e.printStackTrace();
                        }
                    }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.bindMsg(".*couleur (.*) .*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                System.out.println(args[1]);
                String couleur = args[0];
                String confiance = args[1];
                confiance = confiance.replace(',', '.');
                float f = Float.parseFloat(confiance);
                if (f >= 0.8) {
                    int res = frame.deplacerDroite();
                    try {
                        bus.sendMsg("ppilot5 Say=\"Color understood\"");
                    } catch (IvyException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.bindMsg(".*deplace.*haut.*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                s = s.replace(',', '.');
                float f = Float.parseFloat(s);
                if (f >= 0.8) {
                    int res = frame.deplacerHaut();
                    if (res == 0) {
                        try {
                            bus.sendMsg("ppilot5 Say=\"Up\"");
                        } catch (IvyException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bus.sendMsg("ppilot5 Say=\"Up size limit\"");
                        } catch (IvyException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.bindMsg(".*deplace.*bas.*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                s = s.replace(',', '.');
                float f = Float.parseFloat(s);
                if (f >= 0.8) {
                    int res = frame.deplacerBas();
                    if (res == 0) {
                        try {
                            bus.sendMsg("ppilot5 Say=\"Down\"");
                        } catch (IvyException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bus.sendMsg("ppilot5 Say=\"Down size limit\"");
                        } catch (IvyException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.start(null);
    }

    private void rienCompris(Ivy bus) {
        try {
            bus.sendMsg("ppilot5 Say=\"I don't understand\"");
        } catch (IvyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(IvyClient ivyClient, String[] strings) {

    }

}
