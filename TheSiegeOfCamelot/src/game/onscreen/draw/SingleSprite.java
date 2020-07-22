package game.onscreen.draw;

import game.Game;
import game.onscreen.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

/**
 * Class for drawing constant, not changing images
 * @author KarpatTech
 */
public class SingleSprite extends DrawComponent{

    /**
     * The image
     */
    private Image img;

    /**
     * Constructor
     * @param img image filepath + filename (relative to textures folder)
     * @throws IOException if the image file doesn't exist
     */
    public SingleSprite(String img) throws IOException {
        this.img = ImageIO.read(new File("textures/" + img));

        super.width = this.img.getWidth(null);
        super.height = this.img.getHeight(null);
    }

    /**
     * Basic draw method
     * @param g2 parameter for drawing
     * @param pos center position of image
     */
    @Override
    public void draw(Graphics2D g2, Position pos) {
        int scale = Game.getScale();
        g2.drawImage(img, ((int)pos.x - width / 2) * scale, ((int)pos.y - height/2) * scale,
                width * scale, height * scale, null);
    }

    /**
     * Draw method for Entities (able to draw with facing)
     * @param g2 parameter for drawing
     * @param pos center position of image
     * @param isRight if false, then image will be drawn vertically mirrored
     */
    @Override
    public void draw(Graphics2D g2, Position pos, boolean isRight) {
        if (isRight) {
            draw(g2, pos);
        } else {
            int scale = Game.getScale();
            g2.drawImage(img, ((int)pos.x + width / 2) * scale, ((int)pos.y - height/2) * scale,
                    -width * scale, height * scale, null);
        }
    }

    /**
     * Draw method for Projectile (able to rotate images)
     * @param g2 parameter for drawing
     * @param pos center position of image
     * @param angle angle of image (used for projectiles)
     */
    @Override
    public void draw(Graphics2D g2, Position pos, float angle) {
        int scale = Game.getScale();
        AffineTransform at = AffineTransform.getTranslateInstance(pos.x * scale, pos.y * scale);
        at.rotate(angle);
        at.scale(scale, scale);
        g2.drawImage(img, at, null);
    }

    /**
     * Draw method with bonus scaling
     * @param g2 parameter for drawing
     * @param pos center position of image
     * @param inScale additional scale
     */
    @Override
    public void drawWithScale(Graphics2D g2, Position pos, int inScale) {
        int scale = Game.getScale();
        g2.drawImage(img, ((int)pos.x - width * inScale / 2) * scale, ((int)pos.y - height * inScale / 2) * scale,
                width * scale * inScale, height * scale * inScale, null);
    }
}
