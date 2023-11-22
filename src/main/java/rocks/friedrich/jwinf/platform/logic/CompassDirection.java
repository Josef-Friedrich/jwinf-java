package rocks.friedrich.jwinf.platform.logic;

/**
 * Grobe Himmelsrichtung der vier Haupthimmelsrichtungen
 */
public enum CompassDirection {
  /**
   * 0 in <a href=
   * "https://github.com/France-ioi/bebras-modules/blob/ec1baf055c7f1c383ce8dfa5d27998463ef5be59/pemFioi/blocklyRobot_lib-1.1.js#L3355">blocklyRobot_lib-1.1.js</a>
   */
  EAST,

  /**
   * 1 in <a href=
   * "https://github.com/France-ioi/bebras-modules/blob/ec1baf055c7f1c383ce8dfa5d27998463ef5be59/pemFioi/blocklyRobot_lib-1.1.js#L3341">blocklyRobot_lib-1.1.js</a>
   */
  SOUTH,

  /**
   * 2 in <a href=
   * "https://github.com/France-ioi/bebras-modules/blob/ec1baf055c7f1c383ce8dfa5d27998463ef5be59/pemFioi/blocklyRobot_lib-1.1.js#L3369">blocklyRobot_lib-1.1.js</a>
   */
  WEST,

  /**
   * 3 in <a href=
   * "https://github.com/France-ioi/bebras-modules/blob/ec1baf055c7f1c383ce8dfa5d27998463ef5be59/pemFioi/blocklyRobot_lib-1.1.js#L3327">blocklyRobot_lib-1.1.js</a>
   */
  NORTH;

  /**
   * Konvertiere eine Himmelrichtungsnummer in den Aufzählungstyp.
   */
  public static CompassDirection fromNo(int directionNumber) {
    switch (directionNumber) {
      case 1:
        return SOUTH;

      case 2:
        return WEST;

      case 3:
        return NORTH;

      case 0:
      default:
        return EAST;
    }
  }
}