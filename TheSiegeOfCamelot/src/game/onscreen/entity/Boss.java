package game.onscreen.entity;

import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for the boss
 * @author KarpatTech
 */
public class Boss extends Enemy {

    /**
     * Constructor
     * @param pos, targetPos, routeInd  - properties of the boss
     * @throws IOException when some resources are missing
     */
    public Boss(Position pos, Position targetPos, int routeInd) throws IOException {
        super("", pos, targetPos, routeInd);
        maxHP = hp = 5000;
        speed = 0.25f;
        damage = 100;
        attackRate = 24;
        price = 500;
        hpLoss = 10;
        xp = 500;
        super.setDrawComponent(new SingleSprite("boss.png"));
    }
}
