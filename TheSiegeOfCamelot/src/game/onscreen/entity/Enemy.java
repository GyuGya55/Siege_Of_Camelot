package game.onscreen.entity;

import game.Game;
import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for enemies
 * @author KarpatTech
 */
public class Enemy extends FightingEntity{

    protected boolean isSlowedDown = false;
    protected int routeInd;
    protected int routeStatus = 1;

    protected int price;
    protected boolean pricePayed = false;
    protected int hpLoss;

    protected boolean xpPayed = false;
    protected int xp;

    // 0 - running on route, 1 - running to soldier, 2 - fighting
    protected int state = 0;

    /**
     * Constructor
     * @param type, pos, targetPos, routeInd  - properties of enemies
     * @throws IOException when some resources are missing
     */
    public Enemy(String type, Position pos, Position targetPos, int routeInd) throws IOException {
        switch (type) {
            case "g":
                maxHP = hp = 10;
                speed = 1.5f;
                damage = 1;
                attackRate = 12;
                price = 10;
                hpLoss = 1;
                xp = 5;
                setDrawComponent(new SingleSprite("goblin.png"));
                break;
            case "o":
                maxHP = hp = 50;
                speed = 1f;
                damage = 7;
                attackRate = 30;
                price = 20;
                hpLoss = 1;
                xp = 8;
                setDrawComponent(new SingleSprite("ork.png"));
                break;
            case "t":
                maxHP = hp = 100;
                speed = 0.3f;
                damage = 30;
                attackRate = 70;
                price = 50;
                hpLoss = 3;
                xp = 15;
                setDrawComponent(new SingleSprite("troll.png"));
                break;
        }
        super.pos = pos;
        super.setTargetPos(targetPos);
        this.routeInd = routeInd;
    }

    /**
     * Void responsible for doing damages
     * @throws IOException when some resources are missing
     */
    @Override
    public void doDamage() throws IOException {
        target.sufferDamage(damage);
    }

    public void nextPoint() {
        ++routeStatus;
    }

    public int getRouteInd() {
        return routeInd;
    }

    public int getRouteStatus() {
        return routeStatus;
    }

    public boolean isCloseToTarget() {
        return pos.distance(getTargetPos()) < speed;
    }

    public void setSlowedDown(boolean slowedDown) {
        isSlowedDown = slowedDown;
    }

    /**
     * Void responsible for slowing down enemies
     */
    public void slowDown() {
        if (!isSlowedDown) {
            speed *= 0.75f;
            isSlowedDown = true;
        }
    }

    public int getState() {
        return state;
    }

    public int payPriceIfNotPayed() {
        if (!pricePayed) {
            pricePayed = true;
            return price;
        }
        return 0;
    }

    public int getHpLoss() {
        return hpLoss;
    }

    public int getXp() {
        if (!xpPayed) {
            xpPayed = true;
            return xp;
        }
        return 0;
    }

    @Override
    public void setTarget(FightingEntity target) {
        super.setTarget(target);
        state = 1;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * Called once every tick. Enemy movements
     * @throws IOException when some resources are missing
     */
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
        }

        if (state == 2 && target.hp > 0 && GameTimer.getTickNum() % attackRate == 0) {
            doDamage();
        }
    }

    public boolean canBeAttacked() {
        return true;
    }
}
