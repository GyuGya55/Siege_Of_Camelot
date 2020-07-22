package game;

import game.onscreen.Boundary;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;

import java.awt.*;
import java.io.IOException;

/**
 * Class for representing places, where boundary can be built
 * @author KarpatTech
 */
public class BoundaryPlace {

    private Boundary boundary;

    private Position pos;
    private boolean vertical;
    /**
     * Ticknum, when bouldary will be built
     */
    private int buildAt = -1;

    /**
     * Contructor
     * @param pos position of boundary
     * @param verical is boundary vertical
     */
    public BoundaryPlace(Position pos, boolean verical) {
        this.pos = pos;
        this.vertical = verical;
    }

    /**
     * Called once in every tick (24 times per second)
     * @throws IOException
     */
    public boolean tick() throws IOException {
        if (GameTimer.getTickNum() == buildAt) {
            boundary = new Boundary(pos, vertical);
        }
        if (boundary != null) {
            if (boundary.getHp() <= 0) {
                boundary = null;
                return true; // Boundary just destroyed
            }
        }
        return false;
    }

    public boolean isInRange(Enemy e) {
        if (boundary != null) {
            return boundary.isInRange(e);
        }
        return false;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public Position getPos() {
        return pos;
    }

    public void buildWhen(int tickNum) {
        buildAt = tickNum;
    }

    /**
     * Draws boundary if its build
     * @param g2 parameter for drawing
     */
    public void draw(Graphics2D g2) {
        if (boundary != null) {
            boundary.draw(g2);
        }
    }
}
