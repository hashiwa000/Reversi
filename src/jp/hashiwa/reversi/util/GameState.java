package jp.hashiwa.reversi.util;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public class GameState {

  /**
   * ゲームの勝利者
   * ゲームが引き分けの場合は null
   */
  private final State winner;

  /**
   * ゲームが終わっているかどうか
   */
  private final boolean over;

  public GameState(RManager manager) {
    this(manager.getBoard().getCells(), manager.getSelectable());
  }

  public GameState(RCell[][] cells, SelectableCells selectable) {

    State tmpWinner = null;
    boolean tmpOver = false;

    if (selectable.get(State.Black).isEmpty() &&
        selectable.get(State.White).isEmpty()) {

      // ゲームの勝敗を計算する。
      tmpOver = true;

      int cellNum = cells.length;
      int blackNum = 0;

      for (int i=0 ; i<cellNum ; i++) {
        for (int j=0 ; j<cellNum ; j++) {
          State s = cells[i][j].getState();

          if      (s == State.Black) blackNum++;
          else if (s == State.White) blackNum--;

        }
      }

      if (0 < blackNum) {
        tmpWinner = State.Black;
      } else if (blackNum < 0) {
        tmpWinner = State.White;
      }
    }

    winner = tmpWinner;
    over = tmpOver;

  }

  public boolean isOver() {
    return over;
  }
  public State winner() {
    return winner;
  }
}
