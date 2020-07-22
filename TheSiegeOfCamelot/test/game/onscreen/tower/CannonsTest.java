package game.onscreen.tower;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CannonsTest {

    private Position center;
    private final int ACCURACY = 1;

    private Cannons c;
    private Position dirUnits[];
    private ArrayList<Enemy> enemies;

    @Before
    public void setUp() throws Exception {
        center = new Position(200, 200);

        c = new Cannons(center);

        enemies = new ArrayList<>();
        int range = c.getRange();
        dirUnits = new Position[]{
                new Position(1, 0),
                new Position(-1, 0),
                new Position(0, 1),
                new Position(0, -1)
        };
        for (int subRange = range - ACCURACY; subRange <= range + ACCURACY; subRange += ACCURACY) {
            for (int i = 0; i < dirUnits.length; ++i) {
                enemies.add(new Enemy("t", center.plus(dirUnits[i].mult(subRange)), center.plus(dirUnits[i].mult(subRange)), 0));
            }
        }
    }

    @Test
    public void doDamage() throws IOException {
        int delay = c.getDelay();

        for (int i = 0; i < 2 * dirUnits.length; ++i) {
            while (enemies.get(0).getHp() >= 0) {
                // start shot
                int shootTime = (int) (enemies.get(0).getPos().distance(center) / c.getProjectile().getSpeed());
                int expectedHp = enemies.get(0).getHp();
                doNTicks(shootTime - 3, expectedHp); // wait till hit
                doOneTick();
                expectedHp -= c.getProjectile().getDamage();
                doNTicks(delay - shootTime + 2, expectedHp); // wait till next shot

            }
            enemies.remove(0);
        }
        // All enemies in range dead, tower should not shot
        int expectedHPs[] = new int[dirUnits.length];
        for (int i = 0; i < dirUnits.length; ++i) {
            expectedHPs[i] = enemies.get(i).getHp();
        }
        for (int i = 0; i < 3 * delay; ++i) {
            doOneTick();
            for (int j = 0; j < dirUnits.length; ++j) {
                assertEquals(expectedHPs[j], enemies.get(j).getHp());
            }
        }
    }


    private void doNTicks(int n, int expectedHP) throws IOException {
        for (int i = 0; i < n; ++i) {
            doOneTick();
            assertEquals(expectedHP, enemies.get(0).getHp());
        }
    }

    private void doOneTick() throws IOException {
        GameTimer.tick();
        c.handleEnemies(enemies);
        c.tick();
    }
}