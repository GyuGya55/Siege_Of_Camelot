package game.onscreen;

import game.onscreen.draw.DrawComponent;
import java.awt.*;

/**
 * Abstract class, responsible for screen elements
 * @author KarpatTech
 */
public abstract class OnScreenElement implements Comparable<OnScreenElement> {
    protected Position pos;
    protected DrawComponent drawComp;

    public void draw(Graphics2D g2) {
        drawComp.draw(g2, this.pos);
    }

    public void setDrawComponent(DrawComponent drawComp) {
        this.drawComp = drawComp;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos.clone();
    }

    @Override
    public int compareTo(OnScreenElement onScreenElement) {
        if (pos.y > onScreenElement.pos.y) {
            return 1;
        } else if (pos.y == onScreenElement.pos.y) {
            return 0;
        }
        return -1;
    }
}
