package jp.hashiwa.reversi.play;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.EdgePreferringPlayerType0;
import jp.hashiwa.reversi.player.PlayerProvider;
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
    if (args.length == 2) {
      PlayerProvider provider = new PlayerProvider(manager);

      player1 = provider.getPlayer(args[0]);
      if (player1 == null) {
        player1 = new EdgePreferringPlayerType0(manager);
      }

      player2 = provider.getPlayer(args[1]);
      if (player2 == null) {
        player2 = new RandomPlayer(manager);
      }
    } else {
      player1 = new EdgePreferringPlayerType0(manager);
      player2 = new RandomPlayer(manager);
    }

    return true;
  }

}
