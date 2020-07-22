package game;

import game.onscreen.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class for representing a route
 * @author KArpatTech
 */
public class Route {
    /**
     * Points of the route
     */
    private ArrayList<Position> points;

    /**
     * Constructor
     */
    public Route() {
        points = new ArrayList<>();
    }

    public void addPoint(Position point) {
        points.add(point);
    }

    public int size() {
        return points.size();
    }

    public Position getPoint(int ind) {
        return points.get(ind);
    }

    /**
     * Get point with random
     * @param ind point index
     * @param random size of random
     * @return random point in range of random
     */
    public Position getPointWithRandom(int ind, int random) {
        return points.get(ind).randomize(random);
    }
}
