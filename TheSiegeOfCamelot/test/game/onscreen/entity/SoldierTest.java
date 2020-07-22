package game.onscreen.entity;

import game.onscreen.Position;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SoldierTest {

    private Position center;

    private Soldier sUp;
    private Soldier sDown;
    private Soldier sRight;
    private Soldier sLeft;

    private Enemy eUp;
    private Enemy eDown;
    private Enemy eRight;
    private Enemy eLeft;

    @Before
    public void setUp() throws Exception {
        center = new Position(200, 200);

        sUp = new Soldier(center, 40, 1.0f, 3, 30);
        sDown = new Soldier(center, 40, 1.0f, 3, 30);
        sRight = new Soldier(center, 40, 1.0f, 3, 30);
        sLeft = new Soldier(center, 40, 1.0f, 3, 30);

        eUp = new Enemy("g", new Position(200, 100), center, 0);
        eDown = new Enemy("g", new Position(200, 300), center, 0);
        eRight = new Enemy("g", new Position(300, 200), center, 0);
        eLeft = new Enemy("g", new Position(100, 200), center, 0);
    }

    @Test
    public void moveUp() {
        sUp.setTarget(eUp);
        float speed = sUp.getSpeed();
        float expectedY = center.y;
        while (expectedY >= center.y - 100) {
            assertEquals(center.x, sUp.getPos().x, 0.01f);
            assertEquals(expectedY, sUp.getPos().y, 0.01f);
            expectedY -= speed;
            sUp.move();
        }
    }

    @Test
    public void moveDown() {
        sDown.setTarget(eDown);
        float speed = sDown.getSpeed();
        float expectedY = center.y;
        while (expectedY <= center.y + 100) {
            assertEquals(center.x, sDown.getPos().x, 0.01f);
            assertEquals(expectedY, sDown.getPos().y, 0.01f);
            expectedY += speed;
            sDown.move();
        }
    }

    @Test
    public void moveRight() {
        sRight.setTarget(eRight);
        float speed = sRight.getSpeed();
        float expectedX = center.x;
        while (expectedX <= center.x + 100) {
            assertEquals(center.y, sRight.getPos().y, 0.01f);
            assertEquals(expectedX, sRight.getPos().x, 0.01f);
            expectedX += speed;
            sRight.move();
        }
    }

    @Test
    public void moveLeft() {
        sLeft.setTarget(eLeft);
        float speed = sLeft.getSpeed();
        float expectedX = center.x;
        while (expectedX >= center.x - 100) {
            assertEquals(center.y, sLeft.getPos().y, 0.01f);
            assertEquals(expectedX, sLeft.getPos().x, 0.01f);
            expectedX -= speed;
            sLeft.move();
        }
    }

    @Test
    public void doDamage() throws IOException {
        sUp.setTarget(eUp);
        int damage = sUp.getDamage();
        int expectedHP = eUp.getHp();
        while (eUp.getHp() >= 0) {
            assertEquals(expectedHP, eUp.getHp());
            expectedHP -= damage;
            sUp.doDamage();
        }
    }

}