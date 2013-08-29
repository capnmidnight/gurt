/**
 * FILE: Map.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.client.screens;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import gurt.client.GameState;
import gurt.client.sound.AudioOutput;
import gurt.physics.Renderable;
import gurt.physics.Universe;
import gurt.util.ErrorHandler;

/**
 * A screen that displays the solar system layout and the location of specific
 * objects
 * 
 * @author stm
 * deprecated
 */
public class Map extends Screen {

    private BufferedImage back;
    private Universe univ;
    private ArrayList<Renderable> items;

    /**
     * Create a new map screen for a specific universe<br>
     * 
     * @param univ
     */
    public Map(Universe univ) {
        if (univ != null) {
            this.univ = univ;
            this.items = univ.getMapObjects();
            try {
                ClassLoader cl = this.getClass().getClassLoader();
                URL url = cl.getResource("out.png");
                this.back = ImageIO.read(url);
            } catch (Exception e) {
                ErrorHandler.printError(e, "Could not open map");
            }
        }
    }

    /**
     * Create the map as an image.
     * 
     * deprecated
     */
    private void makeImage(Graphics2D g) {
        float[] bounds = univ.getBounds();
        float sx = Screen.width / bounds[2];
        float sy = Screen.height / bounds[3];
        float tx = -bounds[0];
        float ty = -bounds[1];
        for (int i = 0; i < items.size(); ++i) {
            Renderable obj = items.get(i);
            obj.renderMap(g, sx, sy, tx, ty);
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        univ.update(dt);
    }

    @Override
    public void sound(AudioOutput audio) {
    }

    @Override
    public void render(Graphics2D g) {
        if (univ != null) {
            g.drawImage(back, 0, 0, null);
            makeImage(g);
        }
    }

    @Override
    public void doInput() {
        int key = popKey();
        switch (key) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_M:
                this.nextState = GameState.Game;
                this.stateChanged = true;
                break;
            default:
                break;
        }
    }
}
