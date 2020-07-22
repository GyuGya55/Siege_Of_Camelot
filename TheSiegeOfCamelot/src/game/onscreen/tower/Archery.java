package game.onscreen.tower;

import game.hud.Command;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;
import game.onscreen.entity.Archer;
import game.onscreen.entity.Enemy;
import game.onscreen.entity.Facing;
import game.onscreen.projectile.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsible for archer towers
 * @author KarpatTech
 */
public class Archery extends Tower {
    private ArrayList<Archer> archers;

    /**
     * Constructor
     * @param pos position
     * @throws IOException when some resources are missing
     */
    public Archery(Position pos) throws IOException {
        super.pos = pos;
        super.range = 100;
        super.xpToUpgrade = new int[]{2, 5};
        super.priceOfSpecA = 350;
        super.priceOfSpecB = 250;
        super.setDrawComponent(new SingleSprite("archery1.png"));

        archers = new ArrayList<>();
        spawnArchers();
    }

    /**
     * Void responsible for spawning archers
     * @throws IOException when some resources are missing
     */
    public void spawnArchers() throws IOException {
        Archer archer1 = new Archer(new Position(pos.x - 10, pos.y - 30), Facing.LEFT);
        Archer archer2 = new Archer(new Position(pos.x + 15, pos.y - 30), Facing.RIGHT);
        archers.add(archer1);
        archers.add(archer2);
    }

    /**
     * Called once every tick.
     * @throws IOException when some resources are missing
     */
    @Override
    public void tick() throws IOException {
        for (Archer archer : archers) {
            xp += archer.tick();
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
        int i = 0;
        while (i < enemies.size() && !(isInRange(enemies.get(i)) && enemies.get(i).canBeAttacked())) {
            ++i;
        }
        if (i < enemies.size()) {
            int j = 0;
            while (j < archers.size() && !archers.get(j).shoot(enemies.get(i))) {
                ++j;
            }
            if (j < archers.size()) {
                if (enemies.get(i).getPos().x < archers.get(j).getPos().x) {
                    archers.get(j).setFacing(Facing.LEFT);
                } else {
                    archers.get(j).setFacing(Facing.RIGHT);
                }
            }
        }
        if (level == 5) {
            archers.get(0).handleEnemies(enemies);
        }
    }

    /**
     * Void responsible for specializing archer towers
     * @param spec
     * @throws IOException when some resources are missing
     */
    @Override
    public void specialize(Command spec) throws IOException {
        switch (spec) {
            case SPEC_A: // Crossbow
                level = 4;
                super.setDrawComponent(new SingleSprite("crossbows.png"));
                Toxic arrow = new Toxic(pos, 0.4f, 10, 7);
                arrow.setDrawComponent(new SingleSprite("arrow.png"));
                range = 150;
                int delay = 72;
                for (Archer archer : archers) {
                    archer.setArrow(arrow);
                    archer.setDelay(delay);
                }
                break;
            case SPEC_B: // Crossbow
                level = 5;
                super.setDrawComponent(new SingleSprite("ballist.png"));
                Projectile ballist = new Lasarrow(pos, 0.0f, 15, 8);
                ballist.setDrawComponent(new SingleSprite("arrow.png"));
                range = 150;
                delay = 48;
                archers = new ArrayList<>();
                Archer archer = new Archer(new Position(pos.x + 5, pos.y - 30), Facing.RIGHT);
                archer.setArrow(ballist);
                archer.setDelay(delay);
                archer.setIsBallist();
                archers.add(archer);
                break;
        }
    }

    /**
     * Void responsible for upgrading archer towers
     * @throws IOException when some resources are missing
     */
    @Override
    public void upgrade() throws IOException {
        xp = 0;
        ++level;
        super.setDrawComponent(new SingleSprite("archery" + level + ".png"));
        Projectile arrow = new Projectile(pos, 0.4f, level + 2, 7);
        arrow.setDrawComponent(new SingleSprite("arrow.png"));
        for (Archer archer : archers) {
            archer.setArrow(arrow);
        }
    }

    /**
     * Void responsible for drawing archers and arrows
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        ArrayList<Projectile> arrows = new ArrayList<>();
        for (Archer archer : archers) {
            archer.draw(g2);
            arrows.addAll(archer.getArrows());
        }
        super.draw(g2);
        for (Projectile arrow : arrows) {
            arrow.draw(g2);
        }
    }

    public Projectile getProjectile(int ind) {
        return archers.get(ind).getArrow();
    }

    public int getDelay() {
        return archers.get(0).getDelay();
    }

    public Position getArcherPos(int ind) {
        return archers.get(ind).getPos();
    }
}
