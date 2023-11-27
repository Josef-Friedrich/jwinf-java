package rocks.friedrich.jwinf.platform.logic.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rocks.friedrich.jwinf.platform.logic.Task;

public class MovementTest
{
    Robot robot;

    @BeforeEach
    void setUp()
    {
        Task task = Task
                .loadByRelPath("conditionals_excercises/find_the_destination");
        robot = task.getVirtualRobot();
    }

    @Test
    void testRotationTurnLeft()
    {
        assertEquals(robot.turnLeft().rotation, -1);
    }

    @Test
    void testRotationTurnRight()
    {
        assertEquals(robot.turnRight().rotation, 1);
    }

    @Test
    void testRotationTurnAround()
    {
        assertEquals(robot.turnAround().rotation, 2);
    }

    @Test
    void testRelocatedTrue()
    {
        assertTrue(robot.forward().relocated);
    }

    @Test
    void testRelocatedFalse()
    {
        robot.turnLeft();
        assertFalse(robot.forward().relocated);
    }

    @Test
    void testRelocatedFalseRotation()
    {
        assertFalse(robot.turnLeft().relocated);
    }
}