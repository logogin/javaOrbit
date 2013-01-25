package orbit;

import javax.swing.JFrame;
import javax.swing.Timer;

import orbit.ui.WorldPanel;

/**
 * OrbitMain.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class OrbitMain {

    public static void main(String[] args) {
        WorldPanel panel = new WorldPanel();
        panel.setFocusable(true);

        JFrame frame = new JFrame("Orbit");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(1, panel);
        timer.start();
    }
}
