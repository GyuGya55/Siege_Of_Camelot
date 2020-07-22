package game.onscreen.tower;

import game.GameTimer;
import game.hud.Command;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;
import game.onscreen.entity.Assassin;
import game.onscreen.entity.Enemy;
import game.onscreen.entity.Knight;
import game.onscreen.entity.Soldier;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class responsible for barrack towers
 * @author KarpatTech
 */
public class Barracks extends Tower{
    private ArrayList<Soldier> soldiers;
    private Position soldiersPlace;
    private int soldierNum = 3;
    private int delayToSpawn = 240;
    private int nextTimeToSpawn = 0;
    private Position soldierSpawnPos;

    /**
     * Constructor
     * @param pos, direction
     * @throws IOException when some resources are missing
     */
    public Barracks(Position pos, int direction) throws IOException {
        super.pos = pos;
        super.range = 100;
        super.setDrawComponent(new SingleSprite("barracks1.png"));
        super.xpToUpgrade = new int[]{5, 10};
        priceOfSpecA = 420;
        priceOfSpecB = 300;

        switch (direction) {
            case 0: // up
                soldiersPlace = new Position(pos.x, pos.y - range / 2);
                soldierSpawnPos = new Position(pos.x, pos.y - 10);
                break;
            case 1: // right
                soldiersPlace = new Position(pos.x + range / 2, pos.y);
                soldierSpawnPos = new Position(pos.x + 20, pos.y);
                break;
            case 2: // down
                soldiersPlace = new Position(pos.x, pos.y + range / 2);
                soldierSpawnPos = new Position(pos.x, pos.y + 20);
                break;
            case 3: // left
                soldiersPlace = new Position(pos.x - range / 2, pos.y);
                soldierSpawnPos = new Position(pos.x - 20, pos.y);
                break;
        }

        soldiers = new ArrayList<>();
        spawnSoldier(40, 1f, 3, 30);
        spawnSoldier(40, 1f, 3, 30);
        spawnSoldier(40, 1f, 3, 30);
    }

    /**
     * Void responsible for spawning soldiers
     * @param hp, speed, damage, attackrate - soldiers' properties;
     * @throws IOException when some resources are missing
     */
    public void spawnSoldier(int hp, float speed, int damage, int attackrate) throws IOException {
        Soldier sold = new Soldier(soldierSpawnPos, hp, speed, damage, attackrate);
        Random rand = new Random();
        sold.setTargetPos(new Position(soldiersPlace. x + rand.nextInt(60) - 30, soldiersPlace. y + rand.nextInt(60) - 30));
        soldiers.add(sold);
    }

    /**
     * Void responsible for spawning assassins
     * @throws IOException when some resources are missing
     */
    public void spawnAssassin() throws IOException {
        Soldier sold = new Assassin(soldierSpawnPos);
        Random rand = new Random();
        sold.setTargetPos(new Position(soldiersPlace. x + rand.nextInt(60) - 30, soldiersPlace. y + rand.nextInt(60) - 30));
        soldiers.add(sold);
    }

    /**
     * Void responsible for spawning knights
     * @throws IOException when some resources are missing
     */
    public void spawnKnight() throws IOException {
        Soldier sold = new Knight(soldierSpawnPos);
        Random rand = new Random();
        sold.setTargetPos(new Position(soldiersPlace. x + rand.nextInt(60) - 30, soldiersPlace. y + rand.nextInt(60) - 30));
        soldiers.add(sold);
    }

    /**
     * Void responsible for upgrading barrack towers
     * @throws IOException when some resources are missing
     */
    @Override
    public void upgrade() throws IOException {
        clear();

        xp = 0;
        ++ level;
        super.setDrawComponent(new SingleSprite("barracks" + level + ".png"));

        switch (level) {
            case 2:
                delayToSpawn = 220;
                soldiers = new ArrayList<>();
                spawnSoldier(50, 1f, 5, 30);
                spawnSoldier(50, 1f, 5, 30);
                spawnSoldier(50, 1f, 5, 30);
                break;
            case 3:
                delayToSpawn = 200;
                soldiers = new ArrayList<>();
                spawnSoldier(60, 1.5f, 7, 30);
                spawnSoldier(60, 1.5f, 7, 30);
                spawnSoldier(60, 1.5f, 7, 30);
                break;

        }

    }

    /**
     * Called once every tick.
     * @throws IOException when some resources are missing
     */
    @Override
    public void tick() throws IOException {
        if (soldiers.size() < soldierNum && GameTimer.getTickNum() >= nextTimeToSpawn) {
            switch (level) {
                case 1:
                    spawnSoldier(40, 1f, 3, 30);
                    break;
                case 2:
                    spawnSoldier(50,1f,5,30);
                    break;
                case 3:
                    spawnSoldier(60, 1.5f, 7, 30);
                    break;
                case 4:
                    spawnAssassin();
                    break;
                case 5:
                    spawnKnight();
                    break;
            }
            nextTimeToSpawn = GameTimer.getTickNum() + delayToSpawn;
        }
        int i = 0;
        while (i < soldiers.size()) {
            if (soldiers.get(i).getHp() <= 0) {
                soldiers.remove(i);
                if (soldiers.size() == 2) {
                    nextTimeToSpawn = GameTimer.getTickNum() + delayToSpawn;
                }
            } else {
                xp += soldiers.get(i).tick();
                ++i;
            }
        }

        if (level < 3 && xp >= xpToUpgrade[level - 1]) {
            upgrade();
        }
    }

    @Override
    public void clear() {
        super.clear();
        for (Soldier sold : soldiers) {
            if (sold.getTarget() != null) {
                sold.getTarget().setTarget(null);
            }
        }
    }

    /**
     * Void responsible for fighting with enemies
     * @param enemies list of enemies
     */
    @Override
    public void handleEnemies(ArrayList<Enemy> enemies) {
        int i = 0;
        while (i < enemies.size() && !(isInRange(enemies.get(i)) && enemies.get(i).getState() == 0 && (enemies.get(i).canBeAttacked() || level == 4))){
            ++i;
        }
        if (i < enemies.size()) {
            int j = 0;
            while (j < soldiers.size() && soldiers.get(j).getState() != 0) {
                ++j;
            }
            if (j < soldiers.size()) {
                soldiers.get(j).setTarget(enemies.get(i));
                enemies.get(i).setTarget(soldiers.get(j));
            }
        }
    }

    /**
     * Void responsible for specializing barack towers
     * @param spec
     * @throws IOException when some resources are missing
     */
    @Override
    public void specialize(Command spec) throws IOException{
        switch (spec) {
            case SPEC_A: // Assassins
                level = 4;
                super.setDrawComponent(new SingleSprite("assassins.png"));
                super.range = 150;
                delayToSpawn = 150;
                soldierNum = 2;

                clear();
                soldiers = new ArrayList<>();
                spawnAssassin();
                spawnAssassin();
                break;

            case SPEC_B: // Knights
                level = 5;
                super.setDrawComponent(new SingleSprite("knights.png"));
                super.range = 100;
                delayToSpawn = 280;

                clear();
                soldiers = new ArrayList<>();
                spawnKnight();
                spawnKnight();
                spawnKnight();
                break;
        }
    }

    /**
     * Void responsible for drawing soldiers
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        boolean isTowerDrawn = false;
        Collections.sort(soldiers);
        for (Soldier sold : soldiers) {
            if (!isTowerDrawn && pos.y + 10 < sold.getPos().y) {
                super.draw(g2);
                isTowerDrawn = true;
            }
            sold.draw(g2);
        }
        if (!isTowerDrawn) {
            super.draw(g2);
        }
    }
}
