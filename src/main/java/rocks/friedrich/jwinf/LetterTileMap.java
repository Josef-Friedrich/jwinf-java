package rocks.friedrich.jwinf;

import java.util.HashMap;
import java.util.HashSet;

import ea.actor.TileContainer;
import ea.actor.TileMap;
import ea.actor.Tile;

/**
 * Ein Kachelsatz (tile map), bei dem die einzelnen Kacheln (tile) durch
 * Buchstaben (letter) repräsentiert sind.
 *
 * Die Größe der Kachel wird auf 1 x 1 Pixelmeter und die linke untere Ecke
 * an die Position -0.5 x -0.5 im Engine-Alpha-Koordinatensystem gesetzt,
 * sodass zum Beispiel (0,0) die Mitte der ersten Kachel (links unten)
 * adressiert.
 */
public class LetterTileMap extends TileContainer {
  char[][] letters;

  /**
   * Die Breite des Kachelsatzes, d. h. die Anzahl der Kacheln in der x-Richtung.
   */
  int width;

  /**
   * Die Höhe des Kachelsatzes, d. h. die Anzahl der Kacheln in der y-Richtung.
   */
  int height;

  HashMap<Character, String> images;
  HashMap<Character, Tile> tiles;
  HashSet<Character> obstacles;

  String pathPrefix;
  String extension;

  public LetterTileMap(int width, int height) {
    this(width, height, "", null);
  }

  public LetterTileMap(int width, int height, String pathPrefix) {
    this(width, height, pathPrefix, null);
  }

  /**
   * @param width      Die Breite des Kachelsatz bzw. die Anzahl an Kacheln in
   *                   x-Richtung.
   * @param height     Die Höhe des Kachelsatz bzw. die Anzahl an Kacheln in
   *                   y-Richtung.
   * @param pathPrefix
   * @param extension  Die Dateiendung der Bild-Dateien, die als Kacheln verwendet
   *                   werden.
   */
  public LetterTileMap(int width, int height, String pathPrefix, String extension) {
    super(width, height, 1, 1);
    this.width = width;
    this.height = height;
    this.pathPrefix = pathPrefix;
    this.extension = extension;
    obstacles = new HashSet<>();
    images = new HashMap<>();
    tiles = new HashMap<>();
    letters = new char[width][height];
    setPosition(-0.5f, -0.5f);
  }

  /**
   * Gib die Breite des Kachelsatzes, d. h. die Anzahl der Kacheln in der
   * x-Richtung, zurück.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gib die Höhe des Kachelsatzes, d. h. die Anzahl der Kacheln in der
   * y-Richtung, zurück.
   */
  public int getHeight() {
    return height;
  }

  private void createTile(char letter, String fileName) {
    String extension;
    if (this.extension != null) {
      extension = "." + this.extension;
    } else {
      extension = "";
    }

    if (pathPrefix.length() > 0) {
      char last = pathPrefix.charAt(pathPrefix.length() - 1);
      if (last != '/') {
        pathPrefix = pathPrefix + "/";
      }
    }
    tiles.put(letter, TileMap.createFromImage(pathPrefix + fileName + extension));
  }

  public void registerImage(char letter, String filePath) {
    if (images.get(letter) != null) {
      throw new IllegalArgumentException(String.format("Eine Kachel mit dem Buchstaben %s existiert bereits.", letter));
    }
    images.put(letter, filePath);
    createTile(letter, filePath);
  }

  /**
   * @param x Die x-Position im Kachelgitter. 0 adressiert die erste,
   *          (ganz am linken Rand gelegene) Spalte.
   * @param y Die y-Position im Kachelgitter. 0 adressiert die erste,
   *          (unterste) Zeile.
   */
  public char getLetter(int x, int y) {
    return letters[x][y];
  }

  /**
   * @param x Die x-Position im Kachelgitter. 0 adressiert die erste,
   *          (ganz am linken Rand gelegene) Spalte.
   * @param y Die y-Position im Kachelgitter. 0 adressiert die erste,
   *          (unterste) Zeile.
   */
  private Tile getTileFromCache(int x, int y) {
    return tiles.get(getLetter(x, y));
  }

  /**
   * Überprüfe, ob ein Buchstabe, der eine Kachel repräsentiert, bereits
   * registiert ist.
   *
   * @param tile Der Buchstabe, der für ein bestimmtes Kachelbild registiert
   *             wurde.
   */
  private boolean existsTile(char tile) {
    if (tile == ' ') {
      return true;
    }
    return tiles.get(tile) != null;
  }

  /**
   * @throws IllegalArgumentException
   */
  private void checkWidth(String row) {
    if (row.length() > width) {
      throw new IllegalArgumentException(
          String.format("Anzahl der Zeichen in einer Reihe (%s) muss kleiner gleich numX (%s) sein!", row,
              width));
    }
  }

  /**
   * @throws IllegalArgumentException
   */
  private void checkHeight(String[] rows) {
    if (rows.length > height) {
      throw new IllegalArgumentException(
          String.format("Anzahl der Reihen (%s) muss kleiner gleich numY (%s) sein!", rows.length, height));
    }
  }

  /**
   * @throws IllegalArgumentException
   */
  private void checkLetter(char letter) {
    if (!existsTile(letter)) {
      throw new IllegalArgumentException(String.format("Unbekannte Kachel mit dem Buchstaben “%s”!", letter));
    }
  }

  public void parseMap(String[] rows) {
    checkHeight(rows);
    int currentRow = getHeight() - 1;
    for (String row : rows) {
      setRow(currentRow, row);
      currentRow--;
    }
  }

  /**
   * Setze ein und dieselbe Kachel auf alle Positionen im Kachelgitter.
   *
   * @param tile Der Buchstabe, der für ein bestimmtes Kachelbild registiert
   *             wurde.
   */
  public void fill(char tile) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        setTile(x, y, tile);
      }
    }
  }

  /**
   * @param x    Die x-Position im Kachelgitter. 0 adressiert die erste,
   *             (ganz am linken Rand gelegene) Spalte.
   * @param y    Die y-Position im Kachelgitter. 0 adressiert die erste,
   *             (unterste) Zeile.
   * @param tile Der Buchstabe, der für ein bestimmtes Kachelbild registiert
   *             wurde.
   */
  public void setTile(int x, int y, char tile) {
    checkLetter(tile);
    letters[x][y] = tile;
    setTile(x, height - y - 1, getTileFromCache(x, y));
  }

  /**
   * Setzt die Kacheln in einer Zeile von links nach rechts.
   *
   * @param y Die y-Position im Kachelgitter. 0 adressiert die erste,
   *          (unterste) Zeile.
   */
  public void setRow(int y, String row) {
    checkWidth(row);
    for (int x = 0; x < row.length(); x++) {
      setTile(x, y, row.charAt(x));
    }
  }

  /**
   * Setzt die Kacheln in einer Spalte von unten nach oben.
   *
   * @param x Die x-Position im Kachelgitter. 0 adressiert die erste,
   *          (ganz am linken Rand gelegene) Spalte.
   */
  public void setColumn(int x, String column) {
    for (int y = 0; y < column.length(); y++) {
      setTile(x, y, column.charAt(y));
    }
  }

  /**
   * Definiert welche Kacheln als Hindernisse gelten.
   *
   * @param obstacles Die Buchstaben der Kacheln, die als Hindernis gelten.
   */
  public void setObstacles(char... obstacles) {
    for (char c : obstacles) {
      this.obstacles.add(c);
    }
  }

  /**
   * Gibt wahr zurück, wenn die Kachel auf der angegeben Gitterposition ein
   * Hindernis darstellt.
   *
   * @param x Die x-Position im Kachelgitter. 0 adressiert die erste,
   *          (ganz am linken Rand gelegene) Spalte.
   * @param y Die y-Position im Kachelgitter. 0 adressiert die erste,
   *          (unterste) Zeile.
   */
  public boolean isObstacle(int x, int y) {
    return obstacles.contains(getLetter(x, y));
  }

}
