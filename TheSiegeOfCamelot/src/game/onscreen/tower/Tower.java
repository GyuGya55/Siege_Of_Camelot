package game.onscreen.tower;

import game.hud.Command;
import game.onscreen.OnScreenElement;
import game.onscreen.entity.Enemy;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract class, responsible for towers
 * @author KarpatTech
 */
abstract class Tower extends OnScreenElement {
    // 1, 2, 3, 4 - Spec A, 5 - Spec B
    protected int level = 1;
    protected int xp = 0;
    protected int range;

    protected int[] xpToUpgrade;
    protected int priceOfSpecA;
    protected int priceOfSpecB;

    public boolean isInRange(Enemy enemy) {
        return super.pos.distance(enemy.getPos()) <= range;
    }

    public int getPriceOfSpecA() {
        return priceOfSpecA;
    }

    public int getPriceOfSpecB() {
        return priceOfSpecB;
    }

    public int getLevel() {
        return level;
    }

    public int getRange() {
        return range;
    }

    public void clear() {}

    public abstract void upgrade() throws IOException;

    public abstract void tick() throws IOException;

    public abstract void handleEnemies(ArrayList<Enemy> enemies) throws IOException;

    public abstract void specialize(Command spec) throws IOException;
}
