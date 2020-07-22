package game.onscreen.projectile;

import game.GameTimer;
import game.onscreen.Position;

/**
 * Class responsible for blocker projectiles
 * @author KarpatTech
 */
public class Blocker extends Projectile {

    private Position targetPos;

    /**
     * Constructor
     * @param spawn, curve, damage, speed - projectile's properties
     */
    public Blocker(Position spawn, float curve, int damage, float speed) {
        super(spawn, curve, damage, speed);
    }

    public void setTargetPos(Position targetPos) {
        this.targetPos = targetPos;
    }

    /**
     * Function responsible for creating a blocker projectile
     * @param pos - a position
     * @return a blocker projectile
     */
    public Blocker shot(Position pos) {
        Blocker proj = new Blocker(spawn, curve, damage, speed);
        proj.setTargetPos(pos);
        proj.spawnTime = GameTimer.getTickNum();
        proj.setDrawComponent(this.drawComp);
        return proj;
    }

    /**
     * Responsible for cloning a toxic projectile
     * @return a clone
     */
    public Blocker clone() {
        Blocker clone = new Blocker(spawn, curve, damage, speed);
        clone.setDrawComponent(drawComp);
        return clone;
    }

    /**
     * Responsible for moving a blocker projectile
     */
    @Override
    public boolean move() {
        return super.move(targetPos);
    }
}
