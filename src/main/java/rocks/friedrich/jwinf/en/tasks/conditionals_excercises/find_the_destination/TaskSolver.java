package rocks.friedrich.jwinf.en.tasks.conditionals_excercises.find_the_destination;

import rocks.friedrich.jwinf.platform.Solver;

public class TaskSolver extends Solver<Robot> {

  public TaskSolver() {
    super("17-FR-07-platforms-marbles");
  }

  @Override
  public void easy(Robot robot) {
    while (!robot.reachedRedFlag()) {
      robot.turnLeft();
      if (robot.obstacleInFront()) {
        robot.turnRight();
      }
      robot.forward();
    }
  }

  @Override
  public void medium(Robot robot) {
    while (!robot.reachedRedFlag()) {
      robot.turnLeft();
      if (robot.obstacleInFront()) {
        robot.turnRight();
        robot.turnRight();
        if (robot.obstacleInFront()) {
          robot.turnLeft();
        }
      }
      robot.forward();
    }
  }

  @Override
  public void hard(Robot robot) {
    while (!robot.reachedRedFlag()) {
      robot.turnRight();
      if (robot.obstacleInFront()) {
        robot.turnLeft();
        if (!robot.obstacleInFront()) {
          robot.forward();
        } else {
          robot.turnLeft();
        }
      } else {
        robot.forward();
      }
    }
  }

  @Override
  public void all(Robot robot) {

  }

  public static void main(String[] args) {
    new TaskSolver().solve("hard", 0);
  }
}