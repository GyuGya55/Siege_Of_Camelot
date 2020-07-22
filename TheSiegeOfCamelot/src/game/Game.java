package game;

import game.onscreen.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Root class, responsible for core mechanics
 * @author KarpatTech
 */
public class Game {

    private JFrame frame;
    private JPanel content;
    private int currentLevel;

    /**
     * Timer, which calls tick functions 24 times a second
     */
    private Timer timer;

    /**
     * Resolution width without scaling
     */
    private final static int BASE_WIDTH  = 640;
    /**
     * Reolution height without scaling
     */
    private final static int BASE_HEIGHT = 360;
    private static int scale = 2;
    private static boolean isPaused = false;

    /**
     * Constructor. Initializes the frame, and the Menu
     * @throws IOException when some resources are missing
     */
    public Game() throws IOException {
        frame = new JFrame("The Siege of Camelot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        currentLevel = 0;

        content = new MenuUI(0);
        content.setPreferredSize(new Dimension(BASE_WIDTH * scale, BASE_HEIGHT * scale));
        frame.getContentPane().add(BorderLayout.CENTER, content);

        timer = new Timer(42, new FPSTimer());

        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int x = mouseEvent.getX() / scale;
                int y = (mouseEvent.getY() - 30) / scale;
                Position pos = new Position((float)x, (float)y);
                if (currentLevel == 0) {
                    ((MenuUI)content).clicked(pos);
                } else {
                    ((LevelUI)content).clicked(pos);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {}

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {}

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {}

            @Override
            public void mouseExited(MouseEvent mouseEvent) {}
        });

        timer.start();
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static int getBaseWidth() {
        return BASE_WIDTH;
    }

    public static int getBaseHeight() {
        return BASE_HEIGHT;
    }

    public static int getScale() {
        return scale;
    }

    public static void setScale(int scale) {
        Game.scale = scale;
    }

    public static boolean isGamePaused() {
        return isPaused;
    }
    public static void pause() {
        isPaused = true;
    }

    public static void resume() {
        isPaused = false;
    }

    /**
     * Nested class for calling tick functions
     */
    private class FPSTimer implements ActionListener {
        /**
         * Called 24 times per second by the timer
         * @param ae action parameter
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!isPaused) {
                GameTimer.tick();
            }
            //frame.minimumSize(BASE_WIDTH * scale, BASE_HEIGHT * scale);
            if (currentLevel == 0) {
                int signal = ((MenuUI)content).tick();
                if (signal > 0) {
                    try {
                        currentLevel = signal;
                        frame.getContentPane().remove(content);
                        content = new LevelUI(currentLevel);
                        content.setPreferredSize(new Dimension(BASE_WIDTH * scale, BASE_HEIGHT * scale));
                        frame.getContentPane().add(BorderLayout.CENTER, content);
                        frame.pack();
                    } catch (IOException e) {
                        System.out.println("ERROR: Missing resources");
                    }
                } else if (signal == -1) {
                    frame.pack();
                }
            } else {
                try {
                    int signal = ((LevelUI)content).tick();
                    if (signal == 1) { // BackToMenu
                        currentLevel = 0;
                        frame.getContentPane().remove(content);
                        content = new MenuUI(1);
                        content.setPreferredSize(new Dimension(BASE_WIDTH * scale, BASE_HEIGHT * scale));
                        frame.getContentPane().add(BorderLayout.CENTER, content);
                        frame.pack();
                    }
                } catch (IOException e) {
                    System.out.println("ERROR: Missing resources");
                }
            }
            frame.repaint();
        }
    }
}
