package jp.hashiwa.reversi.util;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public class GameState {

  /**
   * ゲームが終わっているかどうか
   */
  private final boolean over;

  private int blackNum, whiteNum;

  public GameState(RManager manager) {
    this(manager.getBoard().getCells(), manager.getSelectable());
  }

  public GameState(RCell[][] cells, SelectableCells selectable) {

    boolean tmpOver = false;

    if (selectable.get(State.Black).isEmpty() &&
        selectable.get(State.White).isEmpty()) {

      // ゲームの勝敗を計算する。
      tmpOver = true;

      int cellNum = cells.length;
//      blackNum = 0; // not necessary.
//      whiteNum = 0; // not necessary.

      for (int i=0 ; i<cellNum ; i++) {
        for (int j=0 ; j<cellNum ; j++) {
          State s = cells[i][j].getState();

          if      (s == State.Black) blackNum++;
          else if (s == State.White) whiteNum++;

        }
      }
    }

    over = tmpOver;
  }

  public boolean isOver() {
    return over;
  }
  public State winner() {
    if (blackNum == whiteNum) return null;
    if (blackNum < whiteNum)  return State.White;
    return State.Black;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (isOver()) {
      sb.append("Game is over! Winner is ");
      sb.append(winner()).append(':');
    } else {
      sb.append("Now playing.:");
    }

    sb.append(blackNum).append("(Black)").append('-');
    sb.append(whiteNum).append("(White)");

    return sb.toString();
  }
}
