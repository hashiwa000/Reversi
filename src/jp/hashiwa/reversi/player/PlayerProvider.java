package jp.hashiwa.reversi.player;

import java.lang.reflect.Constructor;

import jp.hashiwa.reversi.util.RManager;

public class PlayerProvider {
  public final Class<? extends AbstractPlayer> RANDOM_PLAYER = RandomPlayer.class;
  public final Class<? extends AbstractPlayer> RATIONAL_PLAYER = RationalPlayer.class;
  public final Class<? extends AbstractPlayer> SIMPLE_MINMAX_PLAYER = SimpleMinMaxPlayer.class;
  public final Class<? extends AbstractPlayer> BIASED_MINMAX_PLAYER = BiasedMMPlayer.class;
  public final Class<? extends AbstractPlayer> EDGEPREFERRING_PLAYER_TYPE0 = EdgePreferringPlayerType0.class;
  public final Class<? extends AbstractPlayer> EDGEPREFERRING_PLAYER_TYPE1 = EdgePreferringPlayerType1.class;
  public final Class<? extends AbstractPlayer> BIASED_ALPHABETA_PLAYER = BiasedABPlayer.class;

  private final RManager manager;

  public PlayerProvider(RManager manager) {
    this.manager = manager;
  }

  /**
   * Resolve player class and return player object.
   * priority:<br>
   *   1. Resolve as raw class name "playerClassName"<br>
   *   2. Resolve as jp.hashiwa.reversi.player. + "playerClassName"<br>
   * @param playerClassName
   * @return null if construction of player object is failed,
   *         player object otherwise.
   */
  public AbstractPlayer getPlayer(String playerClassName) {
    // resolve raw class name
    try {
      Class clazz = Class.forName(playerClassName);
      return getPlayer(clazz);
    } catch(ClassNotFoundException e) {}

    // resolve simple class name
    playerClassName = "jp.hashiwa.reversi.player." + playerClassName;
    try {
      Class clazz = Class.forName(playerClassName);
      return getPlayer(clazz);
    } catch(ClassNotFoundException e) {}

    return null;
  }

  /**
   * Return player object.
   * @param player
   * @return null if construction of player object is failed,
   *         player object otherwise.
   */
  public AbstractPlayer getPlayer(Class<? extends AbstractPlayer> player) {
    try {
      Constructor<?> con = player.getConstructor(new Class[]{RManager.class});
      AbstractPlayer obj = (AbstractPlayer)con.newInstance(manager);

      return obj;
    } catch(Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
