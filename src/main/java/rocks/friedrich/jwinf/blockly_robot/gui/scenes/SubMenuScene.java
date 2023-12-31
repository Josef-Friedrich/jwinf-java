package rocks.friedrich.jwinf.blockly_robot.gui.scenes;

import ea.Game;
import ea.Scene;
import ea.actor.Text;
import ea.internal.Bounds;
import rocks.friedrich.jwinf.blockly_robot.gui.Color;
import rocks.friedrich.jwinf.blockly_robot.gui.Controller;
import rocks.friedrich.jwinf.blockly_robot.gui.TextMaker;
import rocks.friedrich.jwinf.blockly_robot.gui.State;

public class SubMenuScene extends Scene implements WindowScene
{
    private final float FONT_SIZE = 0.8f;

    private final float INITIAL_X = 0;

    private final float INITIAL_Y = 0;

    /**
     * aktuelle x-Position.
     */
    private float x = INITIAL_X;

    /**
     * aktuelle y-Position.
     */
    private float y = INITIAL_Y;

    private final String main;

    public SubMenuScene(String main)
    {
        this.main = main;
        State.menu.getSub(main).forEach((sub, id) -> {
            Text text = new Text(sub, FONT_SIZE);
            text.setFont(TextMaker.regular);
            if (id != null)
            {
                text.setColor(Color.BLACK);
                text.addMouseClickListener((vector, mouseButton) -> {
                    if (text.contains(vector))
                    {
                        LevelsScene.launch(id);
                    }
                });
                text.addFrameUpdateListener((deltaSeconds) -> {
                    if (text.contains(Game.getMousePositionInCurrentScene()))
                    {
                        text.setOpacity(0.5f);
                    }
                    else
                    {
                        text.setOpacity(1f);
                    }
                });
            }
            else
            {
                text.setColor(Color.GRAY);
            }
            text.setPosition(x, y);
            add(text);
            y -= 2 * FONT_SIZE;
        });
    }

    public Bounds getWindowBounds()
    {
        return new Bounds(INITIAL_X - 2, y, 12, INITIAL_Y - y + 2);
    }

    public String getTitle()
    {
        return main;
    }

    public static void main(String[] args)
    {
        Controller.launchScene((WindowScene) new SubMenuScene(
                "Bedingte Anweisungen – Übungen"));
    }
}
