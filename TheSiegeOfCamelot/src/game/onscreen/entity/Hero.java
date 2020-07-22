package game.onscreen.entity;

import game.Game;
import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsible for the hero
 * @author KarpatTech
 */
public class Hero extends FightingEntity {
    // 0 - standing, 1 - moving, 2 -fighting
    private int state = 1;
    private int range = 60;

    /**
     * Constructor
     * @param pos, hp, speed, damage, attackRate - properties of the hero
     * @throws IOException when some resources are missing
     */
    public Hero(Position pos, int hp, float speed, int damage, int attackRate) throws IOException {
        super(hp, speed, damage, attackRate);
        super.pos = pos;
        super.setDrawComponent(new SingleSprite("hero.png"));
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
     * Called once every tick. Hero movements
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

    public boolean isInRange(Enemy enemy) {
        return super.pos.distance(enemy.getPos()) <= range;
    }

    /**
     * Void responsible for handling enemies
     * @param enemies list of enemies
     */
    public void handleEnemies(ArrayList<Enemy> enemies) {
        if (state == 0) {
            int i = 0;
            while (i < enemies.size() && !(isInRange(enemies.get(i)) && enemies.get(i).getState() == 0)) {
                ++i;
            }
            if (i < enemies.size()) {
                setTarget(enemies.get(i));
                enemies.get(i).setTarget(this);
            }
        }
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
