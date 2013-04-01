package jp.hashiwa.reversi.play;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.hashiwa.reversi.event.ReversiEvent;
import jp.hashiwa.reversi.event.ReversiEventListener;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.RationalPlayer;
import jp.hashiwa.reversi.util.GameState;
import jp.hashiwa.reversi.util.RManager;

public class PlayerVSComputer {

  public static void main(String[] args) throws Exception {
    final RManager manager = new RManager();
//    Map map = manager.getSelectable();

//    final ComputerThread computerThread = new ComputerThread(
//        manager, new RationalPlayer(manager));

    final ExecutorService pool = Executors.newSingleThreadExecutor();
    final AbstractPlayer computer = new RationalPlayer(manager);

    manager.getBoard().addReversiEventListener(new ReversiEventListener() {

      @Override
      public void handleEvent(ReversiEvent e) {

        RCell cell = e.getCell();
        GameState gs = manager.addPiece(cell);

        if (gs == null) return;
        if (gs.isOver()) return;

        if (computer.play(pool).isOver()) return;
      }
    });

    pool.shutdownNow();
  }

}
