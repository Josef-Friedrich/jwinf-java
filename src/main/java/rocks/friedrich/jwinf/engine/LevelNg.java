package rocks.friedrich.jwinf.engine;

import java.awt.event.KeyEvent;

import ea.event.KeyListener;
import rocks.friedrich.jwinf.engine.data.model.LevelData;
import rocks.friedrich.jwinf.engine.map.TileMap;
import ea.Scene;

/**
 * Ein Schwierigkeitsgrad bzw. eine Version einer Trainingsaufgabe.
 *
 * Eine Trainingsaufgabe kann mehrere Versionen unterschiedlicher
 * Schwierigkeitsgrade haben, z. B. eine Zweistern- (<code>Version**</code>,
 * <em>easy</em>), Dreistern-(<code>Version***</code>, <em>medium</em>), und
 * eine Vierstern-Version (<code>Version****</code>, <em>hard</em>).
 */
public class LevelNg extends Scene implements KeyListener {

  LevelData data;

  DifficultyLevel difficulty;

  int testNo;

  /**
   * Zum Beispiel „Der Roboter soll den Edelstein einsammeln. Sobald er das Feld
   * mit dem
   * Edelstein erreicht, wird dieser automatisch eingesammelt.“
   */
  String intro;

  public int width;

  public int height;

  protected Grid grid;

  /**
   * Der Haupt-Kachelsatz. Die Figur muss auf diesen Kachelsatz Zugriff haben,
   * um entscheiden zu können, ob sie sich vor einem Hindernis befindet oder
   * nicht.
   */
  protected TileMap map;

  Actor actor;

  public LevelNg(LevelData data) {
    this.data = data;

    width = data.getWidth();
    height = data.getHeight();
    difficulty = data.difficulty;
    testNo = data.testNo;

  }

  public void setGrid(String gridColor, String backgroundColor) {
    grid = new Grid(width, height);
    grid.setColor(new Color(gridColor));
    grid.setBackground(new Color(backgroundColor));
    // Damit (0,0) in der Mitte einer Kachel liegt.
    grid.setPosition(-0.5f, -height + 0.5f);
    add(grid);
  }

  public Grid getGrid() {
    return grid;
  }

  public void setMap(TileMap map) {
    this.map = map;
    add(this.map.container);
  }

  public void setMap(String pathPrefix, String extension) {
    map = new TileMap(width, height, pathPrefix, extension);
    add(map.container);
  }

  public TileMap getMap() {
    return map;
  }

  public void addActor(Actor actor) {
    this.actor = actor;
    add(actor);
  }

  public Actor getActor() {
    return actor;
  }

  // public void controlActor(ActorAction action) {
  // action.act(actor, this);
  // }

  public void focus() {
    getCamera().setFocus(getGrid());
    getCamera().setZoom(State.pixelPerMeter);
  }

  @Override
  public void onKeyDown(KeyEvent keyEvent) {
    switch (keyEvent.getKeyCode()) {

      case KeyEvent.VK_M:
        Controller.toggleInterpolator();
        break;
    }
  }
}
