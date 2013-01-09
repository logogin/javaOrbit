package orbit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import physics.Celestial;
import physics.Position;
import physics.Vector;

/**
 * World.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class World {

    public final Position center = Position.make(500, 500);

    public class Controls {
        public double magnification;
        public Position center;
        public boolean trails;
        public boolean clear;
    }

    public double sizeByMass(double m) {
        return Math.sqrt(m);
    }

    public Color colorByMass(double m) {
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

    public void drawObject(Graphics g, Celestial obj, Controls controls) {
        double mag = controls.magnification;
        Position sunCenter = controls.center;
        double xOffset = center.x - mag * sunCenter.x;
        double yOffset = center.y - mag * sunCenter.y;
        double x = xOffset + mag * obj.position.x;
        double y = yOffset + mag * obj.position.y;
        double s = Math.max(2, mag * sizeByMass(obj.mass));
        double halfS = s / 2;
        Color c = colorByMass(obj.mass);
        g.setColor(c);
        g.fillOval((int)Math.round(x - halfS), (int)Math.round(y - halfS), (int)Math.round(s), (int)Math.round(s));
    }

    public Celestial findSun(List<Celestial> world) {
        for ( Celestial obj : world ) {
            if ( obj.name.contains("sun") ) {
                return obj;
            }
        }
        throw new IllegalStateException("The Sun has gone!");
    }

    public void drawWorld(Graphics g, List<Celestial> world, Controls controls) {
        Celestial sun = findSun(world); //???
        for ( Celestial obj : world ) {
            drawObject(g, obj, controls);
        }
        g.clearRect(0, 0, 1000, 20);
        g.drawString(String.format("Objects: %d, Magnification: %4.3g", world.size(), controls.magnification), 20, 20);
    }

    public void updateWorld(List<Celestial> world, Controls controls) {
        Celestial sun = findSun(world);
        sun.updateAll(world);
    }

    public void magnify(double factor, Controls controls, List<Celestial> world) {
        Position sunPosition = findSun(world).position;
        double newMag = factor * controls.magnification;
        controls.magnification = newMag;
        controls.center = sunPosition;
        controls.clear = true;
    }

    public void resetScreenState(Controls controls) {
        controls.clear = false;
    }

    public void toggleTrails(Controls controls) {
        controls.trails = !controls.trails;
    }

    public boolean quitKey(char key) {
        return key == 'q';
    }

    public boolean plusKey(char key) {
        return key == '+' || key == '=';
    }

    public boolean minusKey(char key) {
        return key == '-' || key == '_';
    }

    public boolean spaceKey(char key) {
        return key == ' ';
    }

    public boolean trailKey(char key) {
        return key == 't';
    }

    public void handlekey(char key, List<Celestial> world, Controls controls) {
        if ( quitKey(key) ) {
            System.exit(0);
        } else if ( plusKey(key) ) {
            magnify(1.1, controls, world);
        } else if ( minusKey(key) ) {
            magnify(0.9, controls, world);
        } else if ( spaceKey(key) ) {
            magnify(1.0, controls, world);
        } else if ( trailKey(key) ) {
            toggleTrails(controls);
        }
    }

    public class WorldPanel extends JPanel implements ActionListener {

        private List<Celestial> world;
        private Controls controls;

        public WorldPanel(List<Celestial> world, Controls controls) {
            this.world = world;
            this.controls = controls;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if ( controls.clear || !controls.trails ) {
                super.paintComponent(g);
            }
            drawWorld(g, world, controls);
            resetScreenState(controls);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 1000);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateWorld(world, controls);
            repaint();
        }
    }

    public WorldPanel worldPanel(final List<Celestial> world, final Controls controls) {
        final WorldPanel panel = new WorldPanel(world, controls);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handlekey(e.getKeyChar(), world, controls);
                panel.repaint();
            }
        });
        return panel;
    }

    public Vector randomVelocity(Position p, Celestial sun) {
        Position sp = sun.position;
        double sd = Position.distance(p, sp);
        double v = Math.sqrt(1 / sd);
        Vector direction = Vector.rotate90(Vector.unit(Vector.subtract(p, sp)));
        return Vector.scale(direction, rand(0.01) + v * 13.5);
    }

    public Position randomPosition(Position sunPosition) {
        double r = rand(300) + 30;
        double theta = rand(2 * Math.PI);
        return Position.add(sunPosition, Vector.make(r * Math.cos(theta), r * Math.sin(theta)));
    }

    public Celestial randomObject(Celestial sun, int n) {
        Position sp = sun.position;
        Position p = randomPosition(sp);
        return Celestial.make(p, rand(0.2), randomVelocity(p, sun), Vector.make(), "r" + n);
    }

    public double rand(double range) {
        return Math.random() * range;
    }

    public List<Celestial> createWorld() {
        List<Celestial> world = new LinkedList<>();
        Vector v0 = Vector.make();
        Celestial sun = Celestial.make(center, 150, Vector.make(0, 0), v0, "sun");
        world.add(sun);
        for ( int n = 0; n < 500; n++ ) {
            world.add(randomObject(sun, n));
        }
        return world;
    }

    public void worldFrame() {
        final Controls controls = new Controls();
        controls.magnification = 1.0;
        controls.center = center;
        controls.trails = false;
        controls.clear = false;

        final List<Celestial> world = createWorld();
        JFrame frame = new JFrame("Orbit");
        WorldPanel panel = worldPanel(world, controls);
        Timer timer = new Timer(1, panel);
        panel.setFocusable(true);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timer.start();
    }

    public void runWorld() {
        worldFrame();
    }

}
