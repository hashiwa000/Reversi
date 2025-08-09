package jp.hashiwa.reversi.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.hashiwa.reversi.frame.RBoard;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.util.RManager;

public class EdgePreferringPlayerType1 extends EdgePrefferingPlayer {
  public EdgePreferringPlayerType1(RManager manager) {
    super(manager);
  }

  @Override
  protected List<Set<RCell>> getPrefferedSet() {
    List<Set<RCell>> preferred = new ArrayList<Set<RCell>>();

    RBoard b = getManager().getBoard();

    int last = b.getCellNum()-1;

    // 1st
    Set<RCell> set = new HashSet<RCell>();
    set.add(b.getCell(0, 0));
    set.add(b.getCell(0, last));
    set.add(b.getCell(last, 0));
    set.add(b.getCell(last, last));

    preferred.add(set);

    // 2nd
    set = new HashSet<RCell>();
    for (RCell c: getManager().downCells(0, 0, new ArrayList<RCell>())) {
      set.add(c);
    }
    for (RCell c: getManager().downCells(last, 0, new ArrayList<RCell>())) {
      set.add(c);
    }
    for (RCell c: getManager().rightCells(0, 0, new ArrayList<RCell>())) {
      set.add(c);
    }
    for (RCell c: getManager().rightCells(0, last, new ArrayList<RCell>())) {
      set.add(c);
    }

    preferred.add(set);

    return preferred;
  }
}
