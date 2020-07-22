package game.onscreen.projectile;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;

import java.io.IOException;

/**
 * Class responsible for toxic projectiles
 * @author KarpatTech
 */
public class Toxic extends Projectile {

    /**
     * Constructor
     * @param spawn, curve, damage, speed - projectile's properties
     */
    public Toxic(Position spawn, float curve, int damage, float speed) {
        super(spawn, curve, damage, speed);
    }

    /**
     * Function responsible for creating a toxic projectile
     * @param target - an enemy
     * @return a toxic projectile
     */
    public Toxic shoot(Enemy target) {
        Toxic proj = new Toxic(spawn, curve, damage, speed);
        proj.setTarget(target);
        proj.spawnTime = GameTimer.getTickNum();
        proj.setDrawComponent(this.drawComp);
        return proj;
    }

    /**
     * Responsible for cloning a toxic projectile
     * @return a clone
     */
    public Toxic clone() {
        Toxic clone = new Toxic(spawn, curve, damage, speed);
        clone.setDrawComponent(drawComp);
        return clone;
    }

    /**
     * Void responsible for doing damages
     * @throws IOException when some resources are missing
     */
    @Override
    public void doDamage() throws IOException {
        super.doDamage();
        target.slowDown();
    }
}
