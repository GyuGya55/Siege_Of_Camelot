package game.hud;

import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HUD class for TowerPlace
 * @author KarpatTech
 */
public class TowerHUD extends HUD {

    /**
     * Represents the state of the tower
     */
    // 0 - no tower, 1 - archery, 2 - barracks, 3 - cannons
    // 11 - crosbows, 12 - ballist
    // 21 - assassins, 22 - knights
    // 31 - mortat, 32 - trebuchet
    private int state = 0;
    private boolean opened = false;
    private boolean specialized = false;

    /**
     * Constructor. Builds TowerHUD
     * @param pos position of HUD
     * @throws IOException hwen some resources are missing
     */
    public TowerHUD(Position pos) throws IOException {
        super.Elements = new ArrayList<>();
        super.lastCommand = Command.NONE;

        int R = 30, r = 10;
        Command commands[] = {Command.OPEN, Command.ARCHERY, Command.BARRACKS, Command.CANNONS, Command.CANCEL, Command.DESTROY,
                Command.SPEC_A, Command.SPEC_B, Command.SPEC_A, Command.SPEC_B, Command.SPEC_A, Command.SPEC_B, Command.DESTROY, Command.CANCEL};
        String textures[] = {"empty.png", "towerhud_archery.png", "towerhud_barracks.png", "towerhud_cannons.png",
                "towerhud_cancel.png", "towerhud_destroy.png", "towerhud_crossbow.png", "towerhud_ballist.png",
                "towerhud_assassins.png", "towerhud_knights.png", "towerhud_mortar.png", "towerhud_trebuchet.png",
                "towerhud_destroy_big.png", "towerhud_cancel_big.png"};
        int clickModes[] = {0, 1, 2, 3, 4, 2, 1, 3, 1, 3, 1, 3, 5, 6};
        for (int i = 0; i < 14; i++) {
            switch (clickModes[i]) {
                case 0:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            return clickPos.distance(this.pos) < 2 * r;
                        }
                    });
                    break;
                case 1:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            if (clickPos.distance(this.pos) < R && clickPos.distance(this.pos) > r) {
                                Position relPos = new Position(clickPos.x - pos.x, clickPos.y - pos.y);
                                return relPos.y > relPos.x && relPos.y < -relPos.x;
                            }
                            return false;
                        }
                    });
                    break;
                case 2:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            if (clickPos.distance(this.pos) < R && clickPos.distance(this.pos) > r) {
                                Position relPos = new Position(clickPos.x - pos.x, clickPos.y - pos.y);
                                return relPos.y < relPos.x && relPos.y < -relPos.x;
                            }
                            return false;
                        }
                    });
                    break;
                case 3:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            if (clickPos.distance(this.pos) < R && clickPos.distance(this.pos) > r) {
                                Position relPos = new Position(clickPos.x - pos.x, clickPos.y - pos.y);
                                return relPos.y < relPos.x && relPos.y > -relPos.x;
                            }
                            return false;
                        }
                    });
                    break;
                case 4:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            if (clickPos.distance(this.pos) < R && clickPos.distance(this.pos) > r) {
                                Position relPos = new Position(clickPos.x - pos.x, clickPos.y - pos.y);
                                return relPos.y > relPos.x && relPos.y > -relPos.x;
                            }
                            return false;
                        }
                    });
                    break;
                case 5:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            if (clickPos.distance(this.pos) < R && clickPos.distance(this.pos) > r) {
                                Position relPos = new Position(clickPos.x - pos.x, clickPos.y - pos.y);
                                return relPos.y < 0;
                            }
                            return false;
                        }
                    });
                    break;
                case 6:
                    Elements.add(new UIElement(commands[i], pos){
                        @Override
                        public boolean isClicked(Position clickPos) {
                            if (clickPos.distance(this.pos) < R && clickPos.distance(this.pos) > r) {
                                Position relPos = new Position(clickPos.x - pos.x, clickPos.y - pos.y);
                                return relPos.y > 0;
                            }
                            return false;
                        }
                    });
                    break;
            }
            Elements.get(i).setDrawComponent(new SingleSprite(textures[i]));
            if (i != 0) {
                Elements.get(i).setActive(false);
            }
        }
    }

    /**
     * Sets state and UI elements according to that
     * @param state new state
     */
    public void setState(int state) {
        this.state = state;
        for (UIElement ui : Elements) {
            ui.setActive(false);
        }
        switch (state) {
            case 0:
                Elements.get(1).setActive(true);
                Elements.get(2).setActive(true);
                Elements.get(3).setActive(true);
                Elements.get(4).setActive(true);
                break;
            case 1:
                Elements.get(4).setActive(true);
                Elements.get(5).setActive(true);
                Elements.get(6).setActive(true);
                Elements.get(7).setActive(true);
                break;
            case 2:
                Elements.get(4).setActive(true);
                Elements.get(5).setActive(true);
                Elements.get(8).setActive(true);
                Elements.get(9).setActive(true);
                break;
            case 3:
                Elements.get(4).setActive(true);
                Elements.get(5).setActive(true);
                Elements.get(10).setActive(true);
                Elements.get(11).setActive(true);
                break;
            case 11: case 12:
            case 21: case 22:
            case 31: case 32:
                Elements.get(12).setActive(true);
                Elements.get(13).setActive(true);
                break;
        }
    }

    public int getState() {
        return state;
    }

    public void open() {
        opened = true;
        setState(state);
    }

    public void close() {
        opened = false;
        Elements.get(0).setActive(true);
        for (int i = 1; i < Elements.size(); ++i) {
            Elements.get(i).setActive(false);
        }
    }

    /**
     * Redraws the active UI elements in every tick
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        for(UIElement elem : super.Elements){
            if(elem.isActive()) {
                elem.draw(g2);
            }
        }
    }
}
