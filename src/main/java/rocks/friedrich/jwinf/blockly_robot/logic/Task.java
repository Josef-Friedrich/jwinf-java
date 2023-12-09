package rocks.friedrich.jwinf.blockly_robot.logic;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import rocks.friedrich.jwinf.blockly_robot.data.JsonLoader;
import rocks.friedrich.jwinf.blockly_robot.data.model.GridInfosData;
import rocks.friedrich.jwinf.blockly_robot.data.model.TaskData;
import rocks.friedrich.jwinf.blockly_robot.logic.context.Context;
import rocks.friedrich.jwinf.blockly_robot.logic.item.ItemCreator;
import rocks.friedrich.jwinf.blockly_robot.logic.level.Difficulty;
import rocks.friedrich.jwinf.blockly_robot.logic.level.Level;
import rocks.friedrich.jwinf.blockly_robot.logic.level.LevelCollection;
import rocks.friedrich.jwinf.blockly_robot.logic.robot.VirtualRobot;

/**
 * Eine Trainingsaufgabe (Task) besteht aus mehreren (in der Regel 3)
 * Schwierigkeitsgraden (Difficulty). Ein Schwierigkeitsgrad kann einen oder
 * mehrere Tests (Level) haben.
 */
public class Task
{
    /**
     * @param taskPath Der relative Pfad zu resources/data/tasks
     */
    public static Task loadByTaskPath(String taskPath)
    {
        return new Task("data/tasks/%s.json".formatted(taskPath));
    }

    public static String extractTaskPath(String path)
    {
        path = path.replace(".json", "").replace(".class", "").replace(".", "/")
                .replaceAll(".*/tasks/", "").replaceAll("^/", "")
                .replaceAll("/$", "");
        String[] segments = path.split("/");
        return "%s/%s".formatted(segments[0], segments[1]);
    }

    String taskPath;

    public TaskData data;

    private GridInfosData context;

    /**
     * Zum Beispiel „Edelsteine einsammeln“
     */
    public String title;

    /**
     * Zum Beispiel „Programmiere den Roboter“
     */
    public String intro;

    public LevelCollection levels;

    private ItemCreator itemCreator;

    /**
     * Die Anzahl an Tests (Level) der Schwierigkeitsstufe mit den meisten
     * Tests.
     */
    private int maxLevelsPerDifficulty;

    public Task(String filePath)
    {
        try
        {
            data = JsonLoader.loadTask(filePath);
            taskPath = extractTaskPath(filePath);
            title = data.title;
            intro = data.intro;
            if (data.gridInfos.contextType != null)
            {
                var contexts = JsonLoader.loadContexts();
                context = contexts.get(data.gridInfos.contextType);
            }
            itemCreator = setupItemCreator();
            levels = new LevelCollection(data.levels, this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private ItemCreator setupItemCreator()
    {
        if (data.gridInfos.itemTypes != null)
        {
            return new ItemCreator(data.gridInfos.itemTypes);
        }
        else if (context != null && context.itemTypes != null)
        {
            return new ItemCreator(context.itemTypes);
        }
        return null;
    }

    public String getTaskPath()
    {
        return taskPath;
    }

    public TaskData getData()
    {
        return data;
    }

    public String getTitle()
    {
        return title;
    }

    public String getIntro()
    {
        return intro;
    }

    public ItemCreator getItemCreator()
    {
        return itemCreator;
    }

    /**
     * Returns the grid color.
     *
     * @return the grid color as a String.
     */
    public String getBorderColor()
    {
        String borderColor = data.gridInfos.borderColor;
        if (borderColor != null)
        {
            return borderColor;
        }
        if (context != null && context.borderColor != null)
        {
            return context.borderColor;
        }
        return null;
    }

    /**
     * Returns the background color.
     *
     * @return the background color as a String.
     */
    public String getBackgroundColor()
    {
        String backgroundColor = data.gridInfos.backgroundColor;
        if (backgroundColor != null)
        {
            return backgroundColor;
        }
        if (context != null && context.backgroundColor != null)
        {
            return context.backgroundColor;
        }
        return null;
    }

    public int getBagSize()
    {
        return data.gridInfos.bagSize;
    }

    public int getMaxFallAltitude()
    {
        int max = data.gridInfos.maxFallAltitude;
        if (max == 0)
        {
            return 2;
        }
        return max;
    }

    public int getNbPlatforms()
    {
        return data.gridInfos.nbPlatforms;
    }

    /**
     * Returns a map of difficulty levels and their corresponding list of
     * levels.
     *
     * @return the map of difficulty levels and their corresponding list of
     *         levels
     */
    public Map<Difficulty, List<Level>> getLevels()
    {
        return levels.levels;
    }

    /**
     * Retrieves the level for a given difficulty and test.
     *
     * @param difficulty the difficulty of the task
     * @param test       the test index (0 is the first test)
     * @return the level corresponding to the given difficulty and test
     */
    public Level getLevel(Difficulty difficulty, int test)
    {
        return levels.getLevel(difficulty, test);
    }

    /**
     * Returns the level associated with the given difficulty.
     *
     * @param difficulty the difficulty of the task
     * @return the level associated with the difficulty
     */
    public Level getLevel(Difficulty difficulty)
    {
        return getLevel(difficulty, 0);
    }

    /**
     * Retrieves the level based on the given difficulty.
     *
     * @param difficulty the difficulty level as an integer (0 is easy, 1 is
     *                   medium, 2 is hard)
     * @return the corresponding level
     */
    public Level getLevel(int difficulty)
    {
        return getLevel(Difficulty.indexOf(difficulty), 0);
    }

    /**
     * Returns the first version of a training task with the easiest difficulty.
     * /
     *
     * <i>Gibt die erste Version einer Trainingsaufgabe mit dem leichtesten
     * Schwierigkeitsgrad zurück.</i>
     */
    public Level getLevel()
    {
        return getLevel(Difficulty.EASY, 0);
    }

    public Context getMap()
    {
        return getLevel().getContext();
    }

    /**
     * Returns the virtual robot of the first version of a training task with
     * the easiest difficulty. /
     *
     * <i>Gibt einen virtuellen Roboter der erste Version einer Trainingsaufgabe
     * mit dem leichtesten Schwierigkeitsgrad zurück.</i>
     */
    public VirtualRobot getVirtualRobot()
    {
        return getLevel().getContext().getRobot();
    }

    /**
     * Die Anzahl der Kacheln einer Spalte, des Tests (Level) mit der größten
     * horizontalen Ausdehnung.
     */
    public int getMaxCols()
    {
        return levels.maxCols;
    }

    /**
     * Die Anzahl der Kacheln einer Zeile, des Tests (Level) mit der größten
     * vertikalen Ausdehnung.
     */
    public int getMaxRows()
    {
        return levels.maxRows;
    }

    /**
     * Die Anzahl an Schwierigkeitsgraden (In der Regel 3).
     */
    public int getNumberOfDifficulties()
    {
        return levels.levels.size();
    }

    /**
     * Die Anzahl der Tests des Schwierigkeitsgrads mit den meisten Tests.
     */
    public int getMaxLevelsPerDifficulty()
    {
        getLevels().forEach((difficulty, levels) -> {
            if (maxLevelsPerDifficulty < levels.size())
            {
                maxLevelsPerDifficulty = levels.size();
            }
        });
        return maxLevelsPerDifficulty;
    }

    /**
     * Die Anzahl aller Tests (Level).
     */
    public int getNumberOfLevels()
    {
        return levels.numberOfLevels;
    }

    public boolean hasGravity()
    {
        return data.gridInfos.hasGravity;
    }

    public boolean doAutoWithdraw()
    {
        return data.gridInfos.autoWithdraw;
    }
}
