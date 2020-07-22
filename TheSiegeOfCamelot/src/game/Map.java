package game;

import game.onscreen.BackgroundElement;
import game.onscreen.Position;
import game.onscreen.draw.DrawComponent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class for representing the map
 * @author KarpatTech
 */
public class Map {
    private ArrayList<BackgroundElement> decorations;
    private ArrayList<Route> routes;

    /**
     * Constructor
     */
    public Map() {
        decorations = new ArrayList<>();
        routes = new ArrayList<>();
    }

    public void addDecoration(BackgroundElement decoration) {
        decorations.add(decoration);
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public Route getRoute(int ind) {
        return routes.get(ind);
    }

    /**
     * Draws every decoration
     * @param g2 parameter or drawing
     */
    public void draw(Graphics2D g2) {
        for (BackgroundElement elem : decorations) {
            elem.draw(g2);
        }
    }
}
