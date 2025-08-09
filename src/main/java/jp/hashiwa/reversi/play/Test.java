package jp.hashiwa.reversi.play;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.hashiwa.reversi.frame.RBoard;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.util.RManager;

public class Test {

  public static void main(String[] args) throws Exception {
    RManager manager = new RManager();

    RBoard board = manager.getBoard();

    board.getCell(4, 3).setState(State.White);
    board.getCell(4, 2).setState(State.White);
    board.getCell(4, 1).setState(State.White);
    board.getCell(4, 0).setState(State.White);

    board.getCell(6, 4).setState(State.Black);
    board.getCell(6, 5).setState(State.White);

    board.getCell(7, 4).setState(State.White);
    board.getCell(7, 5).setState(State.Black);

    Map map = manager.getSelectable();

    test(map, board);

    {
      int xyz = 0;
      xyz++;
    }
  }

  private static void test(Map<State, Set<RCell>> tested, RBoard board) {
    boolean ok = true;
    {
      Set<RCell> collectBlack = new HashSet<RCell>();
      collectBlack.add(board.getCell(5, 6));
      collectBlack.add(board.getCell(6, 6));
      collectBlack.add(board.getCell(7, 3));
      collectBlack.add(board.getCell(3, 4));
      collectBlack.add(board.getCell(8, 4));
      collectBlack.add(board.getCell(3, 2));
      collectBlack.add(board.getCell(7, 6));
      collectBlack.add(board.getCell(4, 6));

      Set<RCell> testedBlack = tested.get(State.Black);

      if (!testedBlack.equals(collectBlack)) {
        System.out.println("Black is not collect.");
        System.out.println("tested : " + testedBlack);
        System.out.println("collect: " + collectBlack);
        ok = false;
      }
    }
    {
      Set<RCell> collectWhite = new HashSet<RCell>();
      collectWhite.add(board.getCell(4, 6));
      collectWhite.add(board.getCell(5, 3));
      collectWhite.add(board.getCell(6, 3));
      collectWhite.add(board.getCell(7, 6));
      collectWhite.add(board.getCell(3, 5));
      collectWhite.add(board.getCell(8, 5));
      collectWhite.add(board.getCell(7, 3));

      Set<RCell> testedWhite = tested.get(State.White);

      if (!testedWhite.equals(collectWhite)) {
        System.out.println("White is not collect.");
        System.out.println("tested : " + testedWhite);
        System.out.println("collect: " + collectWhite);
        ok = false;
      }

      if (ok) {
        System.out.println("OK!");
      }
    }
  }
}
