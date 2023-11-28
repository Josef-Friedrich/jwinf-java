package rocks.friedrich.jwinf.platform.gui.level;

import ea.Scene;
import ea.Vector;
import rocks.friedrich.jwinf.platform.gui.Color;
import rocks.friedrich.jwinf.platform.gui.map.Grid;
import rocks.friedrich.jwinf.platform.gui.map.ItemMapPainter;
import rocks.friedrich.jwinf.platform.gui.robot.ImageRobot;
import rocks.friedrich.jwinf.platform.logic.level.Level;
import rocks.friedrich.jwinf.platform.logic.robot.RobotWrapper;

/**
 * Klasse, die eine Version einer Trainingsaufgabe zusammenbaut.
 */
public class LevelAssembler
{
    Level level;

    public LevelAssembler(Level level)
    {
        this.level = level;
    }

    public Grid createGrid()
    {
        Grid grid = new Grid(level.cols, level.rows);
        grid.setColor(new Color(level.getGridColor()));
        grid.setBackground(new Color(level.task.getBackgroundColor()));
        return grid;
    }

    public RobotWrapper createRobot() throws Exception
    {
        String className = "rocks.friedrich.jwinf.tasks.en.conditionals.candle.Robot";
        if (level.task.data.packagePath != null)
        {
            className = "rocks.friedrich.jwinf.en.tasks.%s.Robot"
                    .formatted(level.task.data.packagePath.replace("/", "."));
        }
        RobotWrapper robot = RobotWrapper.class.getClassLoader()
                .loadClass(className).asSubclass(RobotWrapper.class)
                .getDeclaredConstructor().newInstance();
        var context = level.createContext();
        robot.actor = new ImageRobot("images/candle/robot.png", context.robot);
        return robot;
    }

    /**
     * @param x - x-Koordinate der linken unteren Ecke
     * @param y - y-Koordinate der linken unteren Ecke
     */
    public AssembledLevel placeActorsInScene(Scene scene, float x, float y)
    {
        AssembledLevel l = new AssembledLevel(level, scene, x, y);
        // Grid
        l.grid = createGrid();
        l.grid.setPosition(x - 0.5f, y - 0.5f);
        scene.add(l.grid);
        // ItemGrid
        new ItemMapPainter(level.getMap()).paint(scene, x - 0.5f, y - 0.5f);
        try
        {
            l.robot = createRobot();
            Vector robotPosition = l.translate.toVector(level.getInitItem().row,
                    level.getInitItem().col);
            ImageRobot robot = (ImageRobot) l.robot.actor;
            robot.setCenter(robotPosition.getX(), robotPosition.getY());
            scene.add(robot);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return l;
    }
}
