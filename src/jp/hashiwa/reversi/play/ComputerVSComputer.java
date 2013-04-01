package jp.hashiwa.reversi.play;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.EdgePreferringPlayerType0;
import jp.hashiwa.reversi.player.RandomPlayer;
import jp.hashiwa.reversi.util.RManager;

public class ComputerVSComputer {

  private static final String PLAYER_CLASS_PREFIX = "jp.hashiwa.reversi.player.";

  private static AbstractPlayer player1, player2;

  public static void main(String[] args) throws Exception {
    final RManager manager = new RManager();

    if (!parseArgumentsAndSetPlayer(args, manager)) {
      System.exit(1);
    }

    manager.getFrame().appendToConsole(player1.getClass().getSimpleName() + " VS " + player2.getClass().getSimpleName());

//    ExecutorService pool = Executors.newSingleThreadExecutor();

    for (;;) {
      if (player1.play().isOver()) break;
      if (player2.play().isOver()) break;
    }

//    pool.shutdownNow();

  }

  private static boolean parseArgumentsAndSetPlayer(String[] args, RManager manager) {
    if (args.length != 2) {
      player1 = new EdgePreferringPlayerType0(manager);
      player2 = new RandomPlayer(manager);
      return true;
    }

    String p1ClassName = PLAYER_CLASS_PREFIX + args[0];
    String p2ClassName = PLAYER_CLASS_PREFIX + args[1];

    try {
      Class<? extends AbstractPlayer> p1Class = (Class<? extends AbstractPlayer>) Class.forName(p1ClassName);
      Class<? extends AbstractPlayer> p2Class = (Class<? extends AbstractPlayer>) Class.forName(p2ClassName);

      Constructor con1 = p1Class.getConstructor(new Class[]{RManager.class});
      Constructor con2 = p2Class.getConstructor(new Class[]{RManager.class});

      player1 = (AbstractPlayer)con1.newInstance(manager);
      player2 = (AbstractPlayer)con2.newInstance(manager);

      return true;
    } catch (ClassNotFoundException e) {
      // use default players
      player1 = new EdgePreferringPlayerType0(manager);
      player2 = new RandomPlayer(manager);

      manager.getFrame().appendToConsole("Class is not found. Use default player.");

      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

}
