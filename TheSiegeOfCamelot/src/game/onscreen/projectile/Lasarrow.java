package game.onscreen.projectile;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;

/**
 * Class responsible for lasarrow projectiles
 * @author KarpatTech
 */
public class Lasarrow extends Projectile {
    private boolean isHitTarget = false;
    private Position farPos;

    /**
     * Constructor
     * @param spawn, curve, damage, speed - projectile's properties
     */
    public Lasarrow(Position spawn, float curve, int damage, float speed) {
        super(spawn, curve, damage, speed);
    }

    /**
     * Function responsible for creating a lasarrow projectile
     * @param target - an enemy
     * @return a lasarrow projectile
     */
    public Lasarrow shoot(Enemy target) {
        Lasarrow proj = new Lasarrow(spawn, curve, damage, speed);
        proj.setTarget(target);
        proj.spawnTime = GameTimer.getTickNum();
        proj.setDrawComponent(this.drawComp);
        return proj;
    }

    /**
     * Responsible for cloning a toxic projectile
     * @return a clone
     */
    public Lasarrow clone() {
        Lasarrow clone = new Lasarrow(spawn, curve, damage, speed);
        clone.setDrawComponent(drawComp);
        return clone;
    }

    /**
     * Responsible for moving a lasarrow projectile
     */
    @Override
    public boolean move() {
        if (!isHitTarget) {
            Position vec = target.getPos().minus(pos);
            isHitTarget = vec.distance(new Position(0, 0)) < 3 * speed;
            drawAngle = vec.getAngle();
            pos = pos.plus(vec.mult(speed));
            if (isHitTarget) {
                farPos = pos.plus(target.getPos().minus(pos).mult(100f));
            }
        } else {
            Position vec = farPos.minus(pos);
            drawAngle = vec.getAngle();
            pos = pos.plus(vec.mult(speed));
        }

        return (pos.x < 0 || pos.x > 640) && (pos.y < 0 || pos.y > 480);
    }
}
