package game.onscreen.entity;

import game.Game;
import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;
import game.onscreen.projectile.Lasarrow;
import game.onscreen.projectile.Projectile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsible for archers
 * @author KarpatTech
 */
public class Archer extends Entity {
    private Projectile arrow;
    private ArrayList<Projectile> arrows;
    private int delay = 36;
    private int nextShotTime = 0;
    private boolean isBallist = false;

    /**
     * Constructor
     * @param pos, facing  - properties of archers
     * @throws IOException when some resources are missing
     */
    public Archer(Position pos, Facing facing) throws IOException {
        super.pos = pos;
        super.facing = facing;
        super.setDrawComponent(new SingleSprite("empty.png"));

        arrow = new Projectile(pos, 0.4f, 3, 7);
        arrow.setDrawComponent(new SingleSprite("arrow.png"));
        arrows = new ArrayList<>();
    }


    /**
     * Function responsible for shooting an enemy
     * @param enemy - an enemy
     */
    public boolean shoot(Enemy enemy) {
        if (GameTimer.getTickNum() >= nextShotTime) {
            arrows.add(arrow.shoot(enemy));
            nextShotTime = GameTimer.getTickNum() + delay;
            return true;
        }
        return false;
    }

    /**
     * Called once every tick. Archer movements
     * @throws IOException when some resources are missing
     */
    public int tick() throws IOException {
        int xp = 0;
        for (int i = 0; i < arrows.size();) {
            Projectile arrow = arrows.get(i);
            if (isBallist && ((Lasarrow)arrow).move()) {
                arrows.remove(i);
            } else if (!isBallist && arrow.move()) {
                arrow.doDamage();
                if (arrow.getTarget().getHp() <= 0) {
                    xp += arrow.getTarget().getXp();
                }
                arrows.remove(i);
            } else {
                ++i;
            }
        }
        return xp;
    }

    /**
     * Void responsible for handling enemies
     * @param enemies list of enemies
     * @throws IOException when some resources are missing
     */
    public void handleEnemies(ArrayList<Enemy> enemies) throws IOException {
        for (Projectile arrow : arrows) {
            for (Enemy enemy : enemies) {
                if (arrow.getPos().distance(enemy.getPos()) < 10 && enemy.canBeAttacked()) {
                    enemy.sufferDamage(arrow.getDamage());
                }
            }
        }
    }

    public void setArrow(Projectile arrow) {
        arrow.setPos(pos);
        this.arrow = arrow.clone();
    }

    public Projectile getArrow() {
        return arrow;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setIsBallist() {
        isBallist = true;
    }

    public ArrayList<Projectile> getArrows() {
        return arrows;
    }

    /**
     * Void responsible for drawing archers
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
}
