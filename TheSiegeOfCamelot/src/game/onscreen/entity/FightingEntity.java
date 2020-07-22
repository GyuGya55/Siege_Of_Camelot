package game.onscreen.entity;

import game.Game;
import game.onscreen.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Abstract class, responsible for fighting entities
 * @author KarpatTech
 */
public abstract class FightingEntity extends Entity{

    protected int maxHP;
    protected int hp;
    protected int attackRate;
    protected float speed;
    protected FightingEntity target;
    protected Position targetPos;
    protected int damage;

    protected Image hpBarBG;
    protected Image hpBarNo;
    protected Image hpBarHP;

    /**
     * Constructor
     * @throws IOException when some resources are missing
     */
    public FightingEntity() throws IOException {
        this.hpBarBG = ImageIO.read(new File("textures/grey.png"));
        this.hpBarNo = ImageIO.read(new File("textures/red.png"));
        this.hpBarHP = ImageIO.read(new File("textures/green.png"));
    }

    /**
     * Constructor
     * @param hp, speed, damage, attackRate - properties of fighting entities
     * @throws IOException when some resources are missing
     */
    public FightingEntity(int hp, float speed, int damage, int attackRate) throws IOException {
        this();
        this.maxHP = hp;
        this.hp = hp;
        this.speed = speed;
        this.damage = damage;
        this.attackRate = attackRate;
    }

    /**
     * Responsible for moving fighting entities
     */
    public void move() {
        Position vec = getTargetPos().minus(pos);
        vec.normalize();
        if (vec.x < 0) {
            facing = Facing.LEFT;
        } else {
            facing = Facing.RIGHT;
        }
        pos = pos.plus(vec.mult(speed));
    }

    public abstract void doDamage() throws IOException;

    public void setTarget(FightingEntity target) {
        this.target = target;
    }

    public void setTargetPos(Position targetPos) {
        this.targetPos = targetPos;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHp() {
        return hp;
    }

    public float getSpeed() {
        return speed;
    }

    public FightingEntity getTarget() {
        return target;
    }

    public int getDamage() {
        return damage;
    }

    public Position getTargetPos() {
        if (target == null) {
            return targetPos;
        }
        return target.getPos();
    }

    public void sufferDamage(int dmg) throws IOException {
        this.hp -= dmg;
    }

    /**
     * Void responsible for drawing health bars
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        int height = super.drawComp.getHeight();
        int width = super.drawComp.getWidth();
        int scale = Game.getScale();
        g2.drawImage(hpBarBG, (int)(pos.x - width / 2 - 1) * scale, (int)(pos.y - height / 2  - 6) * scale,
                (width + 2) * scale, 4 * scale, null);
        g2.drawImage(hpBarNo, (int)(pos.x - width / 2) * scale, (int)(pos.y - height / 2  - 5) * scale,
                width * scale, 2 * scale, null);
        g2.drawImage(hpBarHP, (int)(pos.x - width / 2) * scale, (int)(pos.y - height / 2  - 5) * scale,
                (width * hp / maxHP) * scale, 2 * scale, null);
    }
}
