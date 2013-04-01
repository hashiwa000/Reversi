package jp.hashiwa.reversi.event;

import jp.hashiwa.reversi.frame.RBoard;
import jp.hashiwa.reversi.frame.RCell;

public class ReversiEvent {
  private RBoard board;
  private int x, y;
  public ReversiEvent(RBoard board, int x, int y) {
    this.board = board;
    this.x = x;
    this.y = y;
  }

  public RBoard getBoard() {
    return board;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public RCell getCell() {
    return board.getCell(x, y);
  }
}
