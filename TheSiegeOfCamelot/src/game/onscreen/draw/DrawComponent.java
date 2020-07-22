package game.onscreen.draw;

import game.onscreen.Position;

import java.awt.*;

/**
 * Class responsible for drawing
 * @author KarpatTech
 */
public abstract class DrawComponent {
    /**
     * Height of image(s)
     */
    protected int width;
    /**
     * Width of image(s)
     */
    protected int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Basic abstract draw method
     * @param g2 parameter for drawing
     * @param pos center position of image
     */
    public abstract void draw(Graphics2D g2, Position pos);

    /**
     * Abstract draw method
     * @param g2 parameter for drawing
     * @param pos center position of image
     * @param isRight if false, then image will be drawn vertically mirrored
     */
    public abstract void draw(Graphics2D g2, Position pos, boolean isRight);

    /**
     * Abstract draw method
     * @param g2 parameter for drawing
     * @param pos center position of image
     * @param angle angle of image (used for projectiles)
     */
    public abstract void draw(Graphics2D g2, Position pos, float angle);

    /**
     * Abstract draw method, capable of bonus scaling
     * @param g2 parameter for drawing
     * @param pos center position of image
     * @param inScale additional scale
     */
    public abstract void drawWithScale(Graphics2D g2, Position pos, int inScale);
}
