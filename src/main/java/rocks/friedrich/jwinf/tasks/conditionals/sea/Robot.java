package rocks.friedrich.jwinf.tasks.conditionals.sea;

import rocks.friedrich.jwinf.engine.task.LevelMap;

public class Robot extends rocks.friedrich.jwinf.engine.Robot {

  public Robot(String filePath, LevelMap map) {
    super(filePath, map);
  }

  public void rotateLeft() {
    rotateByAnimated(-90);
  }

  public void rotateRight() {
    rotateByAnimated(90);
  }

  public boolean isBeforeObstacle() {
    return false;
  }

}
