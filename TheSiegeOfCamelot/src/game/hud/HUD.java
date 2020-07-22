package game.hud;

import game.onscreen.Position;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class for storing and managing UI elements (buttons, texts, images, etc..). Also manages click
 * @author KarpatTech
 */
public abstract class HUD
{
    /**
     * Command of the last click in the last tick. Command.NONE if there was no click
     */
    protected Command lastCommand;
    protected ArrayList<UIElement> Elements;

    /**
     * Abstract function for redrawing itself
     * @param g2 parameter for drawing
     */
    public abstract void draw(Graphics2D g2);

    /**
     * Handles click
     * @param pos position of click
     * @return true if any of the buttons is clicked
     */
    public boolean onClick(Position pos){
        for (UIElement elem : Elements){
            if(elem.isActive() && elem.isClickable() && elem.isClicked(pos)){
                lastCommand = elem.getCommand();
                return true;
            }
        }
        return false;
    }

    public void addUIElement(UIElement ui){
        Elements.add(ui);
    }

    public UIElement getUIElement(int ind) {
        return Elements.get(ind);
    }

    /**
     * Manages last command
     * @return last command, and sets it to Command.NONE
     */
    public Command getLastCommand(){
        Command commandTMP;
        commandTMP = lastCommand;
        lastCommand = Command.NONE;
        return commandTMP;
    }
}
