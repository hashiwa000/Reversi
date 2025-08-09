package jp.hashiwa.reversi.test;

import static org.junit.Assert.*;
import org.junit.Test;

import jp.hashiwa.reversi.frame.RBoard;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public class RBoardTest {

  @Test
  public void testInitialBoard() {
    RBoard board = new RBoard(9, 9);
    
    // 中央4マスの状態を確認
    assertEquals(State.Black, board.getCell(3, 3).getState());
    assertEquals(State.Black, board.getCell(4, 4).getState());
    assertEquals(State.White, board.getCell(3, 4).getState());
    assertEquals(State.White, board.getCell(4, 3).getState());
    
    // その他のマスはNoneであることを確認
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        if ((x == 3 && y == 3) || (x == 4 && y == 4) || 
            (x == 3 && y == 4) || (x == 4 && y == 3)) {
          continue;
        }
        assertEquals(State.None, board.getCell(x, y).getState());
      }
    }
  }

  @Test
  public void testSetCellState() {
    //RBoard board = RBoard.createInitialBoard();
    RBoard board = new RBoard(9, 9);
    
    // セルの状態を変更して確認
    board.getCell(0, 0).setState(State.Black);
    assertEquals(State.Black, board.getCell(0, 0).getState());
    
    board.getCell(7, 7).setState(State.White);
    assertEquals(State.White, board.getCell(7, 7).getState());
  }
}
