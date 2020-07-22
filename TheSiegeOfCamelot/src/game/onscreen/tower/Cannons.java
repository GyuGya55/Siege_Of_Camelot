package game.onscreen.tower;

import game.Game;
import game.GameTimer;
import game.hud.Command;
import game.onscreen.Position;
import game.onscreen.draw.DrawComponent;
import game.onscreen.draw.SingleSprite;
import game.onscreen.entity.Enemy;
import game.onscreen.projectile.AOE;
import game.onscreen.projectile.Blocker;
import game.onscreen.projectile.Projectile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsible for cannon towers
 * @author KarpatTech
 */
public class Cannons extends Tower{
    private Projectile projectile;
    private ArrayList<Projectile> cannonballs;
    private int delay = 100;
    private int nextShotTime = 0;

    private DrawComponent loaded;
    private boolean isLoaded = false;

    /**
     * Constructor
     * @param pos position
     * @throws IOException when some resources are missing
     */
    public Cannons(Position pos) throws IOException {
        super.pos = pos;
        super.range = 100;
        super.xpToUpgrade = new int[]{5, 10};
        super.priceOfSpecA = 500;
        super.priceOfSpecB = 600;
        super.setDrawComponent(new SingleSprite("cannons1.png"));
        loaded = new SingleSprite("trebuchet_loaded.png");

        projectile = new Projectile(pos, 0.1f, 15, 7);
        projectile.setDrawComponent(new SingleSprite("cannonball.png"));

        cannonballs = new ArrayList<>();
    }

    /**
     * Called once every tick.
     * @throws IOException when some resources are missing
     */
    @Override
    public void tick() throws IOException {
        int i = 0;
        while (i < cannonballs.size()) {
            Projectile cannonball = cannonballs.get(i);
            if (cannonball.move()) {
                if (level < 5) {
                    cannonball.doDamage();
                    if (cannonball.getTarget().getHp() <= 0) {
                        xp += cannonball.getTarget().getXp();
                    }
                }
                if (level != 4) {
                    cannonballs.remove(i);
                } else {
                    ++i;
                }
            } else {
                ++i;
            }
        }

        if (level < 3 && xp >= xpToUpgrade[level - 1]) {
            upgrade();
        }
    }

    /**
     * Void responsible for shooting enemies
     * @param enemies list of enemies
     * @throws IOException when some resources are missing
     */
    @Override
    public void handleEnemies(ArrayList<Enemy> enemies) throws IOException {
        if (level < 5) {
            int i = 0;
            while (i < enemies.size() && !(isInRange(enemies.get(i)) && enemies.get(i).canBeAttacked())) {
                ++i;
            }
            if (i < enemies.size() && GameTimer.getTickNum() >= nextShotTime) {
                cannonballs.add(projectile.shoot(enemies.get(i)));
                nextShotTime = GameTimer.getTickNum() + delay;
            }

            // Do AOE
            if (level == 4) {
                i = 0;
                while (i < cannonballs.size()) {
                    Projectile cannonball = cannonballs.get(i);
                    if (cannonball.isWasHit()) {
                        AOE areal = (AOE) cannonball;
                        for (Enemy e : enemies) {
                            if (areal.isInRange(e) && e.canBeAttacked()) {
                                areal.doDamageTo(e);
                            }
                        }
                        cannonballs.remove(i);
                    } else {
                        ++i;
                    }
                }
            }
        }

        // Do Trebuchet
        if (level == 5 && GameTimer.getTickNum() >= nextShotTime) {
            isLoaded = true;
        }
    }

    public void shotAt(Position pos) {
        isLoaded = false;
        nextShotTime = GameTimer.getTickNum() + delay;
        cannonballs.add(((Blocker)projectile).shot(pos));
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Void responsible for specializing cannon towers
     * @param spec
     * @throws IOException when some resources are missing
     */
    @Override
    public void specialize(Command spec) throws IOException {
        switch (spec) {
            case SPEC_A: // Mortar
                level = 4;
                super.setDrawComponent(new SingleSprite("mortar.png"));
                projectile = new AOE(new Position(pos.x, pos.y - 30), 0.7f, 35, 3);
                projectile.setDrawComponent(new SingleSprite("cannonball.png"));
                range = 200;
                delay = 200;
                break;
            case SPEC_B: // Trebuchet
                level = 5;
                isLoaded = true;
                super.setDrawComponent(new SingleSprite("trebuchet.png"));
                projectile = new Blocker(new Position(pos.x + 20, pos.y - 30), 0.4f, 0, 6);
                projectile.setDrawComponent(new SingleSprite("cannonball.png"));
                range = 0;
                delay = 300;
                break;
        }
    }

    /**
     * Void responsible for upgrading cannon towers
     * @throws IOException when some resources are missing
     */
    @Override
    public void upgrade() throws IOException {
        xp = 0;
        ++level;
        super.setDrawComponent(new SingleSprite("cannons" + level + ".png"));
        projectile = new Projectile(pos, 0.1f, 10 + 5 * level, 7);
        projectile.setDrawComponent(new SingleSprite("cannonball.png"));
    }

    /**
     * Void responsible for drawing cannonballs
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        if (isLoaded) {
            loaded.draw(g2, pos);
        }
        super.draw(g2);
        for (Projectile proj : cannonballs) {
            proj.draw(g2);
        }
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public int getDelay() {
        return delay;
    }
}
