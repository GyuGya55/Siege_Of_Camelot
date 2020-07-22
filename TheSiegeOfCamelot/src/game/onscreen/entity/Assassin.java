package game.onscreen.entity;

import game.Game;
import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for assassins
 * @author KarpatTech
 */
public class Assassin extends Soldier {

    private boolean isFirstHit = true;

    /**
     * Constructor
     * @param pos position
     * @throws IOException when some resources are missing
     */
    public Assassin(Position pos) throws IOException {
        super(pos, 35, 2f, 15, 12);
        super.setDrawComponent(new SingleSprite("assassin.png"));
    }

    /**
     * Called once every tick. Assassin movements
     * @return earned xp
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
                        isFirstHit = true;
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
                    if (isFirstHit) {
                        isFirstHit = false;
                        doDamage();
                    }
                }
                break;
        }
        return xpEarned;
    }
}
