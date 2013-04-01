package jp.hashiwa.reversi.play;

import java.util.Map;

import jp.hashiwa.reversi.event.ReversiEvent;
import jp.hashiwa.reversi.event.ReversiEventListener;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.util.RManager;

public class PlayerVSPlayer {

  public static void main(String[] args) throws Exception {
    final RManager manager = new RManager();
    Map map = manager.getSelectable();

    manager.getBoard().addReversiEventListener(new ReversiEventListener() {

      @Override
      public synchronized void handleEvent(ReversiEvent e) {
        RCell cell = e.getCell();
//        if (manager.getGameState().isOver()) {
//          // game is over
//          return;
//        } else if () {
//
//        }
//        manager.addPiece(cell);
//      }
        // not implemented yet
        manager.addPiece(cell);
      }
    });
  }
}
