package game.onscreen.entity;

import game.Game;
import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for soldiers
 * @author KarpatTech
 */
public class Soldier extends FightingEntity {
    // 0 - standing, 1 - moving, 2 -fighting
    protected int state = 1;

    /**
     * Constructor
     * @param pos, hp, speed, damage, attackRate  - properties of soldiers
     * @throws IOException when some resources are missing
     */
    public Soldier(Position pos, int hp, float speed, int damage, int attackRate) throws IOException {
        super(hp, speed, damage, attackRate);
        super.pos = pos;
        super.setDrawComponent(new SingleSprite("soldier.png"));
    }

    public int getState() {
        return state;
    }

    @Override
    public void setTarget(FightingEntity target) {
        super.setTarget(target);
        state = 1;
    }

    /**
     * Called once every tick. Soldier movements
     * @throws IOException when some resources are missing
     */
    public int tick() throws IOException {
        int xpEarned = 0;
        if (target != null && target.hp <= 0) {
            xpEarned += ((Enemy)target).getXp();
            target = null;
            state = 0;
        }
        switch (state) {
            case 0:
                // Do nothing
                break;
            case 1:
                super.move();
                if (target != null) {
                    if (pos.distance(getTargetPos()) < 30) {
                        state = 2;
                    }
                } else {
                    if (pos.distance(getTargetPos()) < 3 * speed) {
                        state = 0;
                    }
                }
                break;
            case 2:
                if (GameTimer.getTickNum() % attackRate == 0) {
                    doDamage();
                }
                break;
        }
        return xpEarned;
    }

    /**
     * Void responsible for doing damages
     * @throws IOException when some resources are missing
     */
    @Override
    public void doDamage() throws IOException {
        target.sufferDamage(damage);
    }
}
