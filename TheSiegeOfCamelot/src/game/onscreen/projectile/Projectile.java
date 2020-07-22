package game.onscreen.projectile;

import game.GameTimer;
import game.onscreen.OnScreenElement;
import game.onscreen.Position;
import game.onscreen.entity.Enemy;

import java.awt.*;
import java.io.IOException;

/**
 * Class responsible for projectiles
 * @author KarpatTech
 */
public class Projectile extends OnScreenElement {

    protected Position spawn;
    protected Enemy target;
    protected float curve;

    protected int damage;
    protected float speed;
    protected int spawnTime;

    protected boolean wasHit = false;

    protected double drawAngle = 0;

    /**
     * Constructor
     * @param spawn, curve, damage, speed - projectile's properties
     */
    public Projectile(Position spawn, float curve, int damage, float speed) {
        super.pos = spawn;
        this.spawn = spawn;
        this.curve = curve;
        this.damage = damage;
        this.speed = speed;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public Enemy getTarget() {
        return target;
    }

    /**
     * Function responsible for creating a projectile
     * @param target - an enemy
     * @return a projectile
     */
    public Projectile shoot(Enemy target) {
        Projectile proj = new Projectile(spawn, curve, damage, speed);
        proj.setTarget(target);
        proj.spawnTime = GameTimer.getTickNum();
        proj.setDrawComponent(this.drawComp);
        return proj;
    }

    /**
     * Void responsible for doing damages
     * @throws IOException when some resources are missing
     */
    public void doDamage() throws IOException {
        target.sufferDamage(damage);
        wasHit = true;
    }

    public boolean move() {
        return move(target.getPos());
    }

    /**
     * Responsible for moving the projectiles
     * @param targetPos - the target's position
     */
    public boolean move(Position targetPos) {
        if (spawn.x == targetPos.x) {
            return moveVertical(targetPos);
        }
        Position prevPos = pos.clone(); // for draw
        // Setting up points
        Position movement[] = new Position[3];
        movement[0] = new Position(spawn.x, spawn.y);
        movement[2] = new Position(targetPos.x, targetPos.y);
        movement[1] = new Position((movement[0].x + movement[2].x) / 2, (movement[0].y + movement[2].y) / 2);
        //Rotation
        double angle = Math.atan((movement[0].y - movement[2].y) / (movement[0].x - movement[2].x));
        Position origo = new Position(0, 0);
        for (Position pos : movement) {
            double r = pos.distance(origo);
            double fi = Math.atan(pos.y / pos.x) - angle;
            pos.x = (float)(r * Math.cos(fi));
            pos.y = (float)(r * Math.sin(fi));
        }
        // Adding curve
        movement[1].y -= curve * Math.cos(angle) * movement[0].distance(movement[2]);
        // Interpolation
        float x, y = 0;
        if (movement[0].x > movement[2].x) {
            x = Math.max(movement[0].x - speed * (GameTimer.getTickNum() - spawnTime), movement[2].x);
        } else {
            x = Math.min(movement[0].x + speed * (GameTimer.getTickNum() - spawnTime), movement[2].x);
        }
        for (int i = 0; i < 3; ++i) {
            float product = movement[i].y;
            for (int j = 0; j < 3; ++j) {
                if (i != j) {
                    product *= (x - movement[j].x) / (movement[i].x - movement[j].x);
                }
            }
            y += product;
        }
        //Rotating back
        super.pos = new Position(x, y);
        double r = pos.distance(origo);
        double fi = Math.atan(pos.y / pos.x) + angle;
        if (pos.x < 0) {
            fi += Math.PI;
        }
        pos.x = (float)(r * Math.cos(fi));
        pos.y = (float)(r * Math.sin(fi));

        // for draw
        Position diff = pos.minus(prevPos);
        drawAngle = Math.atan(diff.y / diff.x);
        if (diff.x < 0) {
            drawAngle += Math.PI;
        }

        return pos.distance(targetPos) < 3 * speed;
    }

    /**
     * Responsible for moving the projectiles
     * @param targetPos - the target's position
     */
    private boolean moveVertical(Position targetPos) {
        if (targetPos.y > pos.y) {
            drawAngle = Math.PI * 3 / 2;
            super.pos = new Position(pos.x, Math.min(pos.y + speed, targetPos.y));
        } else {
            drawAngle = Math.PI / 2;
            super.pos = new Position(pos.x, Math.max(pos.y - speed, targetPos.y));
        }
        return pos.distance(targetPos) < 3 * speed;
    }

    public float getCurve() {
        return curve;
    }

    public float getSpeed() {
        return speed;
    }

    public Position getPos() {
        return pos;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isWasHit() {
        return wasHit;
    }

    /**
     * Responsible for cloning a projectile
     * @return a clone
     */
    public Projectile clone() {
        Projectile clone = new Projectile(spawn, curve, damage, speed);
        clone.setDrawComponent(drawComp);
        return clone;
    }

    /**
     * Void responsible for setting a position
     * @param pos position
     */
    @Override
    public void setPos(Position pos) {
        super.setPos(pos);
        spawn = pos.clone();
    }

    /**
     * Void responsible for drawing a projectile
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        drawComp.draw(g2, pos, (float)drawAngle);
    }
}
