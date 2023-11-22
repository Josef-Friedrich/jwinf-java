package rocks.friedrich.jwinf.platform.gui.scenes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import ea.Scene;
import ea.event.KeyListener;
import ea.internal.Bounds;
import rocks.friedrich.jwinf.platform.gui.Controller;
import rocks.friedrich.jwinf.platform.gui.level.AssembledLevel;
import rocks.friedrich.jwinf.platform.gui.level.LevelAssembler;
import rocks.friedrich.jwinf.platform.gui.robot.Robot;
import rocks.friedrich.jwinf.platform.gui.robot.RobotAction;
import rocks.friedrich.jwinf.platform.logic.Task;
import rocks.friedrich.jwinf.platform.logic.level.Difficulty;
import rocks.friedrich.jwinf.platform.logic.level.Level;

public class LevelScene extends Scene implements WindowScene, KeyListener, AssembledLevelScene {
  private final ArrayList<AssembledLevel> assembledLevels = new ArrayList<>();

  public Task task;
  public Level level;

  private final Robot robot;

  private RobotAction action;

  public LevelScene(String taskId, Difficulty difficulty) {
    this(taskId, difficulty, 0);
  }

  public LevelScene(String taskId, Difficulty difficulty, int test) {
    this(Task.loadById(taskId), difficulty, test);
  }

  public LevelScene(Task task, Difficulty difficulty, int test) {
    this.task = task;
    level = task.getLevel(difficulty, test);
    var assembler = new LevelAssembler(level);
    var assembledLevel = assembler.placeActorsInScene(this, 0, 0);
    robot = assembledLevel.robot.actor;
    assembledLevels.add(assembledLevel);
  }

  public List<AssembledLevel> getAssembledLevels() {
    return assembledLevels;
  }

  public Bounds getWindowBounds() {
    return new Bounds(-0.5f, -0.5f, level.cols, level.rows);
  }

  public String getTitle() {
    return level.task.title;
  }

  public void setAction(RobotAction action) {
    this.action = action;
  }

  public void act() {
    if (action != null) {
      action.act(robot, level);
    }
  }

  public static void launch(String taskId, Difficulty difficulty) {
    launch(taskId, difficulty, 0);
  }

  public static void launch(String taskId, Difficulty difficulty, int test) {
    launch(taskId, difficulty, test, null);
  }

  public static void launch(String taskId, RobotAction action) {
    launch(taskId, Difficulty.EASY, 0, action);
  }

  public static void launch(String taskId, Difficulty difficulty, int test, RobotAction action) {
    LevelScene scene = new LevelScene(taskId, difficulty, test);
    if (action != null) {
      scene.setAction(action);
    }
    Controller.launchScene((WindowScene) scene);
  }

  @Override
  public void onKeyDown(KeyEvent e) {
    switch (e.getKeyCode()) {
      // n = next
      case KeyEvent.VK_N:
        break;
      // p = previous
      case KeyEvent.VK_P:
        break;

      case KeyEvent.VK_A:
        new Thread(this::act).start();
        break;

      case KeyEvent.VK_RIGHT:
        robot.goRightNonBlocking();
        break;

      case KeyEvent.VK_UP:
        robot.goUpNonBlocking();
        break;

      case KeyEvent.VK_LEFT:
        robot.goLeftNonBlocking();
        break;

      case KeyEvent.VK_DOWN:
        robot.goDownNonBlocking();
        break;
    }
  }

  public static void main(String[] args) {
    launch("19-DE-12-stay-on-the-road", Difficulty.HARD);
    // launch("20-DE-13-Kerzen-einfach", Difficulty.HARD);
  }

}