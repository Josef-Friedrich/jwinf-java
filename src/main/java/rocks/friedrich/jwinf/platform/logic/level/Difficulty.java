package rocks.friedrich.jwinf.platform.logic.level;

import java.util.HashMap;
import java.util.Map;

/**
 * Der Schwierigkeitsgrad
 */
public enum Difficulty
{
    /**
     * Leichter Schwierigkeitsgrad: Zweistern-Version (<code>Version**</code>,
     * <em>easy</em>).
     */
    EASY(0),
    /**
     * Mittlerer Schwierigkeitsgrad: Dreistern-Version (<code>Version***</code>,
     * <em>medium</em>)
     */
    MEDIUM(1),
    /**
     * Schwerer Schwierigkeitsgrad: Vierstern-Version (<code>Version****</code>,
     * <em>hard</em>).
     */
    HARD(2);

    private int index;

    private static Map<Integer, Difficulty> map = new HashMap<>();

    private Difficulty(int index)
    {
        this.index = index;
    }

    static
    {
        for (Difficulty level : Difficulty.values())
        {
            map.put(level.index, level);
        }
    }

    public static Difficulty indexOf(int level)
    {
        return (Difficulty) map.get(level);
    }

    public static Difficulty indexOf(String level)
    {
        switch (level)
        {
        case "easy":
            return Difficulty.EASY;

        case "medium":
            return Difficulty.MEDIUM;

        case "hard":
            return Difficulty.HARD;

        default:
            throw new IllegalArgumentException(
                    "Unknown difficulty level %s".formatted(level));
        }
    }

    public int getIndex()
    {
        return index;
    }
}
