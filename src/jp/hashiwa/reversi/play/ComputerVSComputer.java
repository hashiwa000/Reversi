package jp.hashiwa.reversi.play;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.EdgePreferringPlayerType0;
import jp.hashiwa.reversi.player.RandomPlayer;
import jp.hashiwa.reversi.util.RManager;

public class ComputerVSComputer {

  public static void main(String[] args) throws Exception {
    final RManager manager = new RManager();

    ExecutorService pool = Executors.newSingleThreadExecutor();

    // player 1
    AbstractPlayer player1 = new EdgePreferringPlayerType0(manager);
    // player 2
    AbstractPlayer player2 = new RandomPlayer(manager);

    for (;;) {
      if (player1.play(pool).isOver()) break;
      if (player2.play(pool).isOver()) break;
    }

    pool.shutdownNow();

  }

}
