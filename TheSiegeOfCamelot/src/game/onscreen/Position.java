package game.onscreen;

import java.io.IOException;
import java.util.Random;

/**
 * Class responsible for screen positions
 * @author KarpatTech
 */
public class Position {
    public float x;
    public float y;

    /**
     * Constructor
     * @param x - float
     * @param y - float
     */
    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Responsible for determining the distance between positions
     * @param pos position
     * @return distance between positions
     */
    public float distance(Position pos) {
        final float sqrt = (float) Math.sqrt((x - pos.x)*(x - pos.x) + (y - pos.y)*(y - pos.y));
        return sqrt;
    }

    /**
     * Void responsible for normalizing distance
     */
    public void normalize() {
        float r = distance(new Position(0, 0));
        x /= r;
        y /= r;
    }

    public double getAngle() {
        normalize();
        double angle = Math.asin(y);
        if (x < 0) {
            return angle + Math.PI;
        }
        return angle;
    }

    public Position plus(Position pos) {
        return new Position(x + pos.x, y + pos.y);
    }

    public Position minus(Position pos) {
        return new Position(x - pos.x, y - pos.y);
    }

    public Position mult(float f) {
        return new Position(f * x, f * y);
    }

    public Position clone() {
        return new Position(x, y);
    }

    /**
     * Void responsible for randomizing positions
     * @return position
     */
    public Position randomize(int range) {
        Random rand = new Random();
        int dx = rand.nextInt(2 * range + 1) - range;
        int dy = rand.nextInt(2 * range + 1) - range;
        return new Position(x + dx, y + dy);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
