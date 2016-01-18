import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Frame extends JPanel{

    private ArrayList<Point> fillCells;
    private int x;
    private int y;

    public Frame() {
        super();
        fillCells = new ArrayList<>(25);

        JFrame window = new JFrame();
        window.setSize(840, 560);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.setVisible(true);

        x = 2;
        y = 5;
        this.fillCell(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Point fillCell : fillCells) {
            int cellX = 10 + (fillCell.x * 10);
            int cellY = 10 + (fillCell.y * 10);
            g.setColor(Color.RED);
            g.fillRect(cellX, cellY, 10, 10);
        }
        g.setColor(Color.BLACK);
        g.drawRect(10, 10, 800, 500);

        for (int i = 10; i <= 800; i += 10) {
            g.drawLine(i, 10, i, 510);
        }

        for (int i = 10; i <= 500; i += 10) {
            g.drawLine(10, i, 810, i);
        }
    }

    public void fillCell(int x, int y) {
        fillCells.removeAll(fillCells);
        fillCells.add(new Point(x, y));
        repaint();
    }

    public int deplacerGauche() {
        if (x > 0 && x < 50) {
            x -= 1;
            this.fillCell(x, y);
            return 0;
        }
        return 1;
    }

    public int deplacerDroite() {
        if (x > 0 && x < 50) {
            x += 1;
            this.fillCell(x, y);
            return 0;
        }
        return 1;
    }

    public int deplacerHaut() {
        if (y > 0 && y < 50) {
            y -= 1;
            this.fillCell(x, y);
            return 0;
        }
        return 1;
    }

    public int deplacerBas() {
        if (y > 0 && y < 50) {
            y += 1;
            this.fillCell(x, y);
            return 0;
        }
        return 1;
    }
}
