package game.onscreen.entity;

import game.onscreen.OnScreenElement;
import game.onscreen.Position;

import java.awt.*;

/**
 * Abstract class, responsible for entities
 * @author KarpatTech
 */
public abstract class Entity extends OnScreenElement {
    protected Facing facing;

    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    public Facing getFacing() {
        return facing;
    }

    /**
     * Void responsible for drawing
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        boolean isRight = (facing == Facing.RIGHT);
        drawComp.draw(g2, pos, isRight);
    }
}
