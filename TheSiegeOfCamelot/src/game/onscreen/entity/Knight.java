package game.onscreen.entity;

import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.io.IOException;

/**
 * Class responsible for knights
 * @author KarpatTech
 */
public class Knight extends Soldier {

    /**
     * Constructor
     * @param pos position
     * @throws IOException when some resources are missing
     */
    public Knight(Position pos) throws IOException {
        super(pos, 100, 0.8f, 10, 40);
        super.setDrawComponent(new SingleSprite("knight.png"));
    }

    /**
     * Void responsible for suffering damages
     * @param dmg damage
     * @throws IOException when some resources are missing
     */
    @Override
    public void sufferDamage(int dmg) throws IOException {
        super.sufferDamage(dmg);
        target.sufferDamage((int)(damage*0.05f) + 1);
    }
}
