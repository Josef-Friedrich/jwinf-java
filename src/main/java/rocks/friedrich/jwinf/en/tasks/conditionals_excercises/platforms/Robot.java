package rocks.friedrich.jwinf.en.tasks.conditionals_excercises.platforms;

import rocks.friedrich.jwinf.platform.logic.robot.RobotWrapper;

/**
 * https://jwinf.de/task/1160
 */
public class Robot extends RobotWrapper
{
    /**
     * gehe vorwärts
     */
    public void forward()
    {
        actor.forward();
    }

    /**
     * spring hoch
     */
    public void jump()
    {
        // actor.jump();
    }

    /**
     * Holz einsammeln
     */
    public void collectFirewood()
    {
    }

    /**
     * Holz ablegen
     */
    public void dropFirewood()
    {
    }

    /**
     * drehe um
     */
    public void turnAround()
    {
        actor.turnAround();
    }

    /**
     * gehe rückwärts
     */
    public void backwards()
    
    {
        actor.backwards();
    }
}