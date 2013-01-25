package orbit.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import orbit.physics.Celestial;
import orbit.physics.Position;
import orbit.physics.Vector;
import orbit.physics.World;

/**
 * WorldPanel.java
 *
 * @created Jan 9, 2013
 * @author Pavel Danchenko
 */
public class WorldPanel extends JPanel implements ActionListener {

    public static final Point CENTER = new Point(500, 500);

    public class Controls {
        public double magnification;
        public double time;
        public Position center;
        public boolean trails;
        public boolean clear;

        public void magnify(double factor) {
            magnification *= factor;
            controls.center = world.findSun().position;
            clear = true;
        }

        public void toggleTrails() {
            trails = !trails;
        }
    }

    private final Controls controls = new Controls();
    private final World world = new World();

    public WorldPanel() {
        controls.magnification = 1.0;
        controls.time = 86400.0/2;
        controls.center = Position.ZERO;
        controls.trails = false;
        controls.clear = false;

        addKeyListener(new KeyHandler() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyChar(), controls);
                repaint();
            }
        });

        world.create();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if ( controls.clear || !controls.trails ) {
            super.paintComponent(g);
        }
        drawWorld(g);
        resetScreenState();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        world.update(controls.time);
        repaint();
    }

    private void drawWorld(Graphics g) {
        for ( Celestial obj : world.getObjects() ) {
            drawObject(g, obj);
        }
        g.clearRect(0, 0, 1000, 20);
        g.setColor(Color.black);
        g.drawString(String.format("Objects: %d, Magnification: %4.3g, Time: %4.3g", world.getObjects().size(), controls.magnification, controls.time), 20, 20);
    }

    private void drawObject(Graphics g, Celestial obj) {
        Position p = obj.position.move(Vector.of(controls.center).scale(-1));
        double x = CENTER.x + controls.magnification * p.x * 1e-9;
        double y = CENTER.y + controls.magnification * p.y * 1e-9;
        double s = Math.max(2, sizeByMass(obj.mass));
        double halfS = s / 2;
        Color c = colorByMass(obj.mass);
        g.setColor(c);
        g.fillOval((int)Math.round(x - halfS), (int)Math.round(y - halfS), (int)Math.round(s), (int)Math.round(s));
    }

    private double sizeByMass(double m) {
        //return 16;
        return controls.magnification * 16 * m / World.SOLAR_MASS;
    }

    private Color colorByMass(double m) {
        m = 50 * m / World.SOLAR_MASS;
        if ( m < 1 ) {
            return Color.black;
        } else if ( m < 2 ) {
            return new Color(210, 105, 30);
        } else if ( m < 5 ) {
            return Color.red;
        } else if ( m < 10 ) {
            return new Color(107, 142, 35);
        } else if ( m < 20 ) {
            return Color.magenta;
        } else if ( m < 40 ) {
            return Color.blue;
        }
        return new Color(255, 215, 0);
    }

    private void resetScreenState() {
        controls.clear = false;
    }

}
