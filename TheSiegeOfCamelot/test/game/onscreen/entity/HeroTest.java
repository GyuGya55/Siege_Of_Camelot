package game.onscreen.entity;

import game.onscreen.Position;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class HeroTest {

    private Position center;

    private Hero hUp;
    private Hero hDown;
    private Hero hRight;
    private Hero hLeft;

    private Enemy eUp;
    private Enemy eDown;
    private Enemy eRight;
    private Enemy eLeft;

    @Before
    public void setUp() throws Exception {
        center = new Position(200, 200);

        hUp = new Hero(center, 100, 1.0f, 20, 30);
        hDown = new Hero(center, 100, 1.0f, 20, 30);
        hRight = new Hero(center, 100, 1.0f, 20, 30);
        hLeft = new Hero(center, 100, 1.0f, 20, 30);

        eUp = new Enemy("t", new Position(200, 100), center, 0);
        eDown = new Enemy("t", new Position(200, 300), center, 0);
        eRight = new Enemy("t", new Position(300, 200), center, 0);
        eLeft = new Enemy("t", new Position(100, 200), center, 0);
    }

    @Test
    public void moveUp() {
        hUp.setTarget(eUp);
        float speed = hUp.getSpeed();
        float expectedY = center.y;
        while (expectedY >= center.y - 100) {
            assertEquals(center.x, hUp.getPos().x, 0.01f);
            assertEquals(expectedY, hUp.getPos().y, 0.01f);
            expectedY -= speed;
            hUp.move();
        }
    }

    @Test
    public void moveDown() {
        hDown.setTarget(eDown);
        float speed = hDown.getSpeed();
        float expectedY = center.y;
        while (expectedY <= center.y + 100) {
            assertEquals(center.x, hDown.getPos().x, 0.01f);
            assertEquals(expectedY, hDown.getPos().y, 0.01f);
            expectedY += speed;
            hDown.move();
        }
    }

    @Test
    public void moveRight() {
        hRight.setTarget(eRight);
        float speed = hRight.getSpeed();
        float expectedX = center.x;
        while (expectedX <= center.x + 100) {
            assertEquals(center.y, hRight.getPos().y, 0.01f);
            assertEquals(expectedX, hRight.getPos().x, 0.01f);
            expectedX += speed;
            hRight.move();
        }
    }

    @Test
    public void moveLeft() {
        hLeft.setTarget(eLeft);
        float speed = hLeft.getSpeed();
        float expectedX = center.x;
        while (expectedX >= center.x - 100) {
            assertEquals(center.y, hLeft.getPos().y, 0.01f);
            assertEquals(expectedX, hLeft.getPos().x, 0.01f);
            expectedX -= speed;
            hLeft.move();
        }
    }

    @Test
    public void doDamage() throws IOException {
        hUp.setTarget(eUp);
        int damage = hUp.getDamage();
        int expectedHP = eUp.getHp();
        while (eUp.getHp() >= 0) {
            assertEquals(expectedHP, eUp.getHp());
            expectedHP -= damage;
            hUp.doDamage();
        }
    }
}