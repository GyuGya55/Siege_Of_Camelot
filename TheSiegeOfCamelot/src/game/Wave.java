package game;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import game.onscreen.entity.Enemy;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clas for representing wave
 * @author KarpatTech
 */
public class Wave {

    protected ArrayList<Enemy> enemies;
    protected ArrayList<Integer> delays;
    protected int numOfSpawned = 0;
    protected int nextSpawn;

    /**
     * Constructor
     */
    public Wave() {
        enemies = new ArrayList<>();
        delays = new ArrayList<>();
    }

    public void addEnemy(Enemy e, int delay) {
        enemies.add(e);
        delays.add(delay);
    }

    public void start() throws IOException {
        nextSpawn = GameTimer.getTickNum();
        tick();
    }

    /**
     * Called once in every tick (24 times per second)
     * @throws IOException
     */
    public void tick() throws IOException {
        if (numOfSpawned < enemies.size() && GameTimer.getTickNum() == nextSpawn) {
            nextSpawn += delays.get(numOfSpawned);
            ++numOfSpawned;
        }
    }

    /**
     * Draws the spawned and living enemies
     * @param g2 parameter for draw
     */
    public void draw(Graphics2D g2) {
        ArrayList<Enemy> spawned = getSpawnedEnemies();
        Collections.sort(spawned);
        for(Enemy enemy : spawned){
            enemy.draw(g2);
        }
    }

    public Enemy getEnemy(int ind) {
        return enemies.get(ind);
    }

    /**
     * Get spawned enemies
     * @return spawned and not dead enemies in an ArrayList
     */
    public ArrayList<Enemy> getSpawnedEnemies() {
        ArrayList<Enemy> spawned = new ArrayList<>();
        for (int i = 0; i < numOfSpawned; ++i) {
            if (enemies.get(i).getHp() > 0) {
                spawned.add(enemies.get(i));
            }
        }
        return spawned;
    }

    /**
     * Get price
     * @return price of killed enemies in last tick (0 if was no enemy killed)
     */
    public int getPrice() {
        int price = 0;
        for (Enemy e : enemies) {
            if (e.getHp() <= 0) {
                price += e.payPriceIfNotPayed();
            }
        }
        return price;
    }

    /**
     * Is wave ended
     * @return true if every enemy is spawned and dead
     */
    public boolean isEnded() {
        for (int i = 0; i < numOfSpawned; ++i) {
            if (enemies.get(i).getHp() > 0) {
                return false;
            }
        }
        return numOfSpawned == enemies.size();
    }
}
