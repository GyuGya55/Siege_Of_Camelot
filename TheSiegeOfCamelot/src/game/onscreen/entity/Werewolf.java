package game.onscreen.entity;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for werewolves
 * @author KarpatTech
 */
public class Werewolf extends Enemy {

    private float slowspeed = 1f;
    private boolean isRunning = true;

    /**
     * Constructor
     * @param pos, targetPos, routeInd  - properties of the boss
     * @throws IOException when some resources are missing
     */
    public Werewolf(Position pos, Position targetPos, int routeInd) throws IOException {
        super("", pos, targetPos, routeInd);
        maxHP = hp = 200;
        speed = 3f;
        damage = 50;
        attackRate = 30;
        price = 80;
        hpLoss = 3;
        xp = 30;
        super.setDrawComponent(new SingleSprite("werewolf_running.png"));
    }

    /**
     * Called once every tick. Werewolf movements
     * @throws IOException when some resources are missing
     */
    @Override
    public void tick() throws IOException {
        if (target == null) {
            state = 0;
        }
        if (target != null && target.hp <= 0) {
            target = null;
            state = 0;
        }
        if (state < 2) {
            super.move();
        }
        if (state == 1 && pos.distance(getTargetPos()) < 30) {
            state = 2;
            if (isRunning) {
                isRunning = false;
                speed = slowspeed;
                super.setDrawComponent(new SingleSprite("werewolf.png"));
                doDamage();
            }
        }

        if (state == 2 && target.hp > 0 && GameTimer.getTickNum() % attackRate == 0) {
            doDamage();
        }
    }
}
