package game.onscreen.projectile;

import game.GameTimer;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProjectileTest {

    private Projectile proj0;
    private ArrayList<Projectile> projs;
    private ArrayList<Enemy> enemies;

    @Before
    public void setUp() throws Exception {
        Position center = new Position(200, 200);
        proj0 = new Projectile(center, 0f, 1, 5);

        projs = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            projs.add(new Projectile(center, i/10.0f, 1, 5));
        }

        enemies = new ArrayList<>();
        enemies.add(new Enemy("", new Position(100, 100), center,0));
        enemies.add(new Enemy("", new Position(100, 200), center,0));
        enemies.add(new Enemy("", new Position(100, 300), center,0));
        enemies.add(new Enemy("", new Position(300, 100), center,0));
        enemies.add(new Enemy("", new Position(300, 200), center,0));
        enemies.add(new Enemy("", new Position(300, 300), center,0));

    }

    @Test
    public void moveStraight_Distance() {
        for (Enemy e : enemies) {
            Projectile straight = proj0.shoot(e);
            float lastDist = e.getPos().distance(straight.getPos());
            GameTimer.tick();
            while (!straight.move()) {
                float currDist = e.getPos().distance(straight.getPos());
                assertEquals(lastDist - straight.getSpeed(), currDist, 0.01f);
                lastDist = currDist;
                GameTimer.tick();
            }
        }
    }

    @Test
    public void moveStraight_Angle() {
        for (Enemy e : enemies) {
            Projectile straight = proj0.shoot(e);
            double angle = Math.atan((straight.getPos().y - e.getPos().y) / (straight.getPos().x - e.getPos().x));
            GameTimer.tick();
            while (!straight.move()) {
                double currAngle = Math.atan((straight.getPos().y - e.getPos().y) / (straight.getPos().x - e.getPos().x));
                assertEquals(angle, currAngle, 0.01f);
                GameTimer.tick();
            }
        }
    }

    @Test
    public void move() {
        for (Projectile proj : projs) {
            for (Enemy e : enemies) {
                Projectile straight = proj0.shoot(e);
                Projectile shot = proj.shoot(e);

                while (!shot.move()) {
                    straight.move();
                    assertTrue(straight.getPos().distance(shot.getPos()) <= e.getPos().distance(new Position(200, 200)) * shot.getCurve());
                    GameTimer.tick();
                }
            }
        }
    }

    @Test
    public void moveVertical_Up() throws IOException {
        Enemy e = new Enemy("", new Position(200, 100), new Position(200, 100),0);
        for (Projectile proj : projs) {
            Projectile shot = proj.shoot(e);
            float lastDist = e.getPos().distance(shot.getPos());
            GameTimer.tick();
            while (!shot.move()) {
                float currDist = e.getPos().distance(shot.getPos());
                assertEquals(lastDist - shot.getSpeed(), currDist, 0.01f);
                assertEquals(200f, shot.getPos().x, 0.01f);
                lastDist = currDist;
                GameTimer.tick();
            }
        }
    }

    @Test
    public void moveVertical_Down() throws IOException {
        Enemy e = new Enemy("", new Position(200, 300), new Position(200, 300),0);
        for (Projectile proj : projs) {
            Projectile shot = proj.shoot(e);
            float lastDist = e.getPos().distance(shot.getPos());
            GameTimer.tick();
            while (!shot.move()) {
                float currDist = e.getPos().distance(shot.getPos());
                assertEquals(lastDist - shot.getSpeed(), currDist, 0.01f);
                assertEquals(200f, shot.getPos().x, 0.01f);
                lastDist = currDist;
                GameTimer.tick();
            }
        }
    }
}