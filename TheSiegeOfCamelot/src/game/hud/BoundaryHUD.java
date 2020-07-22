package game.hud;

import game.Game;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HUD class for BounaryPlace
 * @author KarpatTech
 */
public class BoundaryHUD extends HUD {

    private boolean isActive = false;
    private int lastClickResult = -1;

    /**
     * Constructor
     */
    public BoundaryHUD() {
        Elements = new ArrayList<>();
    }

    /**
     * Adds a build boundary button to a gven place
     * @param pos position of button
     * @throws IOException if some resources are missing
     */
    public void addPlace(Position pos) throws IOException {
        UIElement elem = new UIElement(Command.NONE, pos, 30, 30);
        elem.setDrawComponent(new SingleSprite("build_boundary.png"));
        Elements.add(elem);
    }

    public void open() {
        isActive = true;
    }

    public void close() {
        isActive = false;
    }

    public int getLastClickResult() {
        return lastClickResult;
    }

    /**
     * Handles click if is active
     * @param pos position of click
     * @return true if any button is clicked
     */
    @Override
    public boolean onClick(Position pos) {
        if (isActive) {
            int i = 0;
            while (i < Elements.size() && !Elements.get(i).isClicked(pos)) {
                ++i;
            }
            if (i < Elements.size()) {
                lastClickResult = i;
                return true;
            }
        }
        return false;
    }

    /**
     * Redraws the active UI elements in every tick
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        if (isActive) {
            for (UIElement elem : Elements) {
                if (elem.isActive()) {
                    elem.draw(g2);
                }
            }
        }
    }
}
