package game.onscreen.tower;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArcheryTest {

    private Position center;
    private final int ACCURACY = 1;

    private Archery a;
    private Position dirUnits[];
    private ArrayList<Enemy> enemies;


    @Before
    public void setUp() throws Exception {
        center = new Position(200, 200);

        a = new Archery(center);

        enemies = new ArrayList<>();
        int range = a.getRange();
        dirUnits = new Position[]{
                new Position(1, 0),
                new Position(-1, 0),
                new Position(0, 1),
                new Position(0, -1)
        };
        for (int subRange = range - ACCURACY; subRange <= range + ACCURACY; subRange += ACCURACY) {
            for (int i = 0; i < dirUnits.length; ++i) {
                enemies.add(new Enemy("g", center.plus(dirUnits[i].mult(subRange)), center.plus(dirUnits[i].mult(subRange)), 0));
            }
        }
    }

    @Test
    public void doDamage() throws IOException {
        for (int i = 0; i < 2 * dirUnits.length; ++i) {
            int expectedHp = enemies.get(0).getHp();
            while (enemies.get(0).getHp() >= 0){
                assertEquals(expectedHp, enemies.get(0).getHp());
                for (int j = 0; j < a.getDelay(); ++j) {
                    doOneTick();
                }
                expectedHp -= 2 * a.getProjectile(0).getDamage();
            }
            enemies.remove(0);
        }
        // All enemies in range dead, tower should not shot
        int expectedHPs[] = new int[dirUnits.length];
        for (int i = 0; i < dirUnits.length; ++i) {
            expectedHPs[i] = enemies.get(i).getHp();
        }
        for (int i = 0; i < 3 * a.getDelay(); ++i) {
            doOneTick();
            for (int j = 0; j < dirUnits.length; ++j) {
                assertEquals(expectedHPs[j], enemies.get(j).getHp());
            }
        }
    }


    private void doOneTick() throws IOException {
        GameTimer.tick();
        a.handleEnemies(enemies);
        a.tick();
    }
}