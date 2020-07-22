package game.onscreen.entity;

import game.onscreen.Position;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class EnemyTest {

    private Position center;
    private final int TEST_LENGTH = 100;

    private Enemy eUp;
    private Enemy eDown;
    private Enemy eLeft;
    private Enemy eRight;

    private ArrayList<Enemy> enemies;

    @Before
    public void setUp() throws Exception {
        center = new Position(200, 200);

        eUp = new Enemy("g", center, center.minus(new Position(0, TEST_LENGTH)), 0);
        eDown = new Enemy("g", center, center.plus(new Position(0, TEST_LENGTH)), 0);
        eLeft = new Enemy("g", center, center.minus(new Position(TEST_LENGTH, 0)), 0);
        eRight = new Enemy("g", center, center.plus(new Position(TEST_LENGTH, 0)), 0);

        enemies = new ArrayList<>();
        String type[] = new String[]{"g", "o", "t", "g"};
        int i = 0;
        for (int x = -TEST_LENGTH; x <= TEST_LENGTH; x += 2 * TEST_LENGTH) {
            for (int y = -TEST_LENGTH; y <= TEST_LENGTH; y += 2 * TEST_LENGTH) {
                enemies.add(new Enemy(type[i++], center, center.plus(new Position(x, y)), 0));
            }
        }
    }

    @Test
    public void moveUp() {
        float speed = eUp.getSpeed();
        float expectedY = center.y;
        while (expectedY >= center.y - TEST_LENGTH) {
            assertEquals(center.x, eUp.getPos().x, 0.01f);
            assertEquals(expectedY, eUp.getPos().y, 0.01f);
            expectedY -= speed;
            eUp.move();
        }
    }

    @Test
    public void moveDown() {
        float speed = eDown.getSpeed();
        float expectedY = center.y;
        while (expectedY <= center.y + TEST_LENGTH) {
            assertEquals(center.x, eDown.getPos().x, 0.01f);
            assertEquals(expectedY, eDown.getPos().y, 0.01f);
            expectedY += speed;
            eDown.move();
        }
    }

    @Test
    public void moveLeft() {
        float speed = eLeft.getSpeed();
        float expectedX = center.x;
        while (expectedX >= center.x - TEST_LENGTH) {
            assertEquals(expectedX, eLeft.getPos().x, 0.01f);
            assertEquals(center.y, eLeft.getPos().y, 0.01f);
            expectedX -= speed;
            eLeft.move();
        }
    }

    @Test
    public void moveRight() {
        float speed = eRight.getSpeed();
        float expectedX = center.x;
        while (expectedX <= center.x + TEST_LENGTH) {
            assertEquals(expectedX, eRight.getPos().x, 0.01f);
            assertEquals(center.y, eRight.getPos().y, 0.01f);
            expectedX += speed;
            eRight.move();
        }
    }

    @Test
    public void move() {
        for (Enemy e : enemies) {
            float speed = e.getSpeed();
            float expectedX = center.x, expectedY = center.y;
            int dx = (e.getTargetPos().x > center.x) ? 1 : -1;
            int dy = (e.getTargetPos().y > center.y) ? 1 : -1;
            while (e.getPos().distance(center) <= Math.sqrt(2) * TEST_LENGTH) {
                assertEquals(expectedX, e.getPos().x, 0.01f);
                assertEquals(expectedY, e.getPos().y, 0.01f);
                expectedX += Math.sqrt(speed * speed / 2) * dx;
                expectedY += Math.sqrt(speed * speed / 2) * dy;
                e.move();
            }
        }
    }

    @Test
    public void doDamage() throws IOException {
        for (Enemy e : enemies) {
            Hero h = new Hero(center, 100, 1f, 60, 30);
            e.setTarget(h);
            int damage = e.getDamage();
            int expectedHP = h.getHp();
            while (h.getHp() >= 0) {
                assertEquals(expectedHP, h.getHp());
                expectedHP -= damage;
                e.doDamage();
            }
        }
    }
}