package game.onscreen.entity;

import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for skinwalkers
 * @author KarpatTech
 */
public class Skinwalker extends Enemy {

    private boolean canAttack = false;

    /**
     * Constructor
     * @param pos, targetPos, routeInd  - properties of the boss
     * @throws IOException when some resources are missing
     */
    public Skinwalker(Position pos, Position targetPos, int routeInd) throws IOException {
        super("", pos, targetPos, routeInd);
        maxHP = hp = 80;
        speed = 1f;
        damage = 60;
        attackRate = 20;
        price = 100;
        hpLoss = 4;
        xp = 25;
        super.setDrawComponent(new SingleSprite("skinwalker_human.png"));
    }

    @Override
    public boolean canBeAttacked() {
        return canAttack;
    }

    /**
     * Void responsible for suffering damages
     * @param dmg damage
     */
    @Override
    public void sufferDamage(int dmg) throws IOException {
        super.sufferDamage(dmg);
        if (!canAttack) {
            canAttack = true;
            super.setDrawComponent(new SingleSprite("skinwalker.png"));
        }
    }
}
