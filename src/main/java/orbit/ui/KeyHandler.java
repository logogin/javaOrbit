package orbit.ui;

import java.awt.event.KeyAdapter;

/**
 * KeyHandler.java
 *
 * @created Jan 9, 2013
 * @author Pavel Danchenko
 */
public abstract class KeyHandler extends KeyAdapter {

    private boolean quitKey(char key) {
        return key == 'q';
    }

    private boolean plusKey(char key) {
        return key == '+' || key == '=';
    }

    private boolean minusKey(char key) {
        return key == '-' || key == '_';
    }

    private boolean spaceKey(char key) {
        return key == ' ';
    }

    private boolean trailKey(char key) {
        return key == 't';
    }

    public void handleKey(char key, WorldPanel.Controls controls) {
        if ( quitKey(key) ) {
            System.exit(0);
        } else if ( plusKey(key) ) {
            controls.magnify(1.1);
        } else if ( minusKey(key) ) {
            controls.magnify(0.9);
        } else if ( spaceKey(key) ) {
            controls.magnify(1.0);
        } else if ( trailKey(key) ) {
            controls.toggleTrails();
        } else if ( 'f' == key ) {
            controls.time += 100.0;
        } if ( 'b' == key ) {
            controls.time -= 100.0;
        }

    }
}
