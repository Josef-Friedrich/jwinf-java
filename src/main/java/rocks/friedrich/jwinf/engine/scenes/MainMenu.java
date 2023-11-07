package rocks.friedrich.jwinf.engine.scenes;

import ea.Game;
import ea.Scene;
import ea.actor.Rectangle;
import ea.actor.Text;
import ea.internal.Bounds;
import ea.internal.io.FontLoader;
import rocks.friedrich.jwinf.engine.Color;
import rocks.friedrich.jwinf.engine.Controller;
import rocks.friedrich.jwinf.engine.State;
import rocks.friedrich.jwinf.engine.WindowScene;

public class MainMenu extends Scene implements WindowScene {

  private final float FONT_SIZE = 0.8f;

  private final Color AREA_COLOR = new Color("#99d422");

  private final float BORDER_RADIUS = 0.3f;

  private final float INITAL_X = 0;

  private final float INITAL_Y = 0;

  private final float RECTANGLE_WIDTH = 20;

  /**
   * aktuelle y-Position.
   */
  private float y = INITAL_X;

  /**
   * aktuelle x-Position.
   */
  private float x = INITAL_Y;

  class ColoredArea {
    private Rectangle rectangle;
    private String main;
    private Text text;

    public ColoredArea(String main, float x, float y) {
      this.main = main;
      rectangle = createRectangle();
      text = createText(main);
      rectangle.setPosition(x - 1, y - FONT_SIZE / 2);
      text.setPosition(x, y);
      add(rectangle, text);
    }

    private Rectangle createRectangle() {
      Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, FONT_SIZE * 2);
      rectangle.setBorderRadius(BORDER_RADIUS);
      rectangle.setColor(AREA_COLOR);
      rectangle.addMouseClickListener((vector, mouseButton) -> {
        if (rectangle.contains(vector)) {
          Controller.launchScene((WindowScene) new SubMenu(main));
        }
      });

      rectangle.addFrameUpdateListener((deltaSeconds) -> {
        if (rectangle.contains(Game.getMousePositionInCurrentScene())) {
          rectangle.setOpacity(0.5f);
        } else {
          rectangle.setOpacity(1f);
        }
      });
      return rectangle;
    }

    private Text createText(String content) {
      Text text = new Text(content, FONT_SIZE);
      text.setFont(FontLoader.loadFromFile("fonts/titilium/TitilliumWeb-Bold.ttf"));
      text.setColor(Color.BLACK);
      return text;
    }

  }

  public MainMenu() {
    State.menu.getMain().forEach((main, submenu) -> {
      new ColoredArea(main, x, y);
      y -= 2.5 * FONT_SIZE;
    });
  }

  public Bounds getWindowBounds() {
    return new Bounds(INITAL_X - 2, y, RECTANGLE_WIDTH + 2, INITAL_Y - y + 2);
  }

  public String getTitle() {
    return "Trainingsaufgaben";
  }

  public static void launch() {
    Controller.launchScene((WindowScene) new MainMenu());
  }

  public static void main(String[] args) {
    launch();
  }

}
