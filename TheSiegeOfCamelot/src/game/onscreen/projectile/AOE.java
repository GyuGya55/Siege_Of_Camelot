package game.onscreen.projectile;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;;import java.io.IOException;

/**
 * Class responsible for AOE projectiles
 * @author KarpatTech
 */
public class AOE extends Projectile{

    private int range = 100;

    /**
     * Constructor
     * @param spawn, curve, damage, speed - projectile's properties
     */
    public AOE(Position spawn, float curve, int damage, float speed) {
        super(spawn, curve, damage, speed);
    }

    /**
     * Function responsible for creating an AOE projectile
     * @param target - an enemy
     * @return an AOE projectile
     */
    public AOE shoot(Enemy target) {
        AOE proj = new AOE(spawn, curve, damage, speed);
        proj.setTarget(target);
        proj.spawnTime = GameTimer.getTickNum();
        proj.setDrawComponent(this.drawComp);
        return proj;
    }

    /**
     * Responsible for cloning an AOE projectile
     * @return a clone
     */
    public AOE clone() {
        AOE clone = new AOE(spawn, curve, damage, speed);
        clone.setDrawComponent(drawComp);
        return clone;
    }

    public boolean isInRange(Enemy e) {
        return super.pos.distance(e.getPos()) <= range;
    }

    /**
     * Void responsible for doing damages
     * @throws IOException when some resources are missing
     */
    public void doDamageTo(Enemy e) throws IOException {
        e.sufferDamage(damage);
    }
}
