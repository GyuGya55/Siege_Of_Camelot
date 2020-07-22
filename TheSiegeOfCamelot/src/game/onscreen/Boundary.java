package game.onscreen;

import game.onscreen.draw.SingleSprite;
import game.onscreen.entity.Enemy;
import game.onscreen.entity.FightingEntity;

import java.io.IOException;

/**
 * Class responsible for boundaries
 * @author KarpatTech
 */
public class Boundary extends FightingEntity {

    private int roadThickness = 80;
    private boolean vertical;

    /**
     * Constructor
     * @param pos position, vertical
     * @throws IOException when some resources are missing
     */
    public Boundary(Position pos, boolean vertical) throws IOException {
        super(250, 0f, 0, 1);
        super.pos = pos;
        this.vertical = vertical;
        if (vertical) {
            super.setDrawComponent(new SingleSprite("boundary_vertical.png"));
        } else {
            super.setDrawComponent(new SingleSprite("boundary_horizontal.png"));
        }
    }

    public boolean isInRange(Enemy e) {
        if (vertical) {
            return Math.abs(pos.x - e.getPos().x) <= roadThickness / 4 && Math.abs(pos.y - e.getPos().y) <= roadThickness / 2;
        }
        return Math.abs(pos.x - e.getPos().x) <= roadThickness / 2 && Math.abs(pos.y - e.getPos().y) <= roadThickness / 3;
    }

    @Override
    public void doDamage() {}
}
