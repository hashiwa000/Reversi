package jp.hashiwa.reversi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public class SelectableCells extends HashMap<State, Set<RCell>> {

  public SelectableCells(RManager manager) {
    super(2, 1);
    put(State.Black, new HashSet<RCell>());
    put(State.White, new HashSet<RCell>());

    List<RCell> list = new ArrayList<RCell>();

    int cellNum = manager.getBoard().getCellNum();

    for (int i=0 ; i<cellNum ; i++) {
      // 縦方向
      list.clear();
      update(manager.downCells(i, 0, list));
      // 横方向
      list.clear();
      update(manager.rightCells(0, i, list));
      // 右下斜め方向
      list.clear();
      update(manager.rightDownCells(i, 0, list));
      list.clear();
      update(manager.rightDownCells(0, i, list));
      // 左下斜め方向
      list.clear();
      update(manager.leftDownCells(i, 0, list));
      list.clear();
      update(manager.leftDownCells(cellNum-1, i, list));
    }
  }

  private void update(List<RCell> cells) {

    // x: current piece (e.g. Black)
    //
    // (1)
    //   +----------------- term == None (selectable!)
    //   |   +------------- cell == Black
    //   v   v
    // [  oooxxxoooxxxoo  ]
    //
    // (2)
    //            +-------- term == Black
    //            |  +----- cell == None (selectable!)
    //            v  v
    // [  oooxxooxxoo     ]
    //
    // (3)
    //                      term == null
    //    +---------------- cell == None
    //    v
    // [oo                ]
    //
    // (4)
    //  +------------------ term == Black
    //  | +---------------- cell == None (selectable!)
    //  v v
    // [xo                ]

    RCell term = null;
    RCell before = null;

    for (RCell cell: cells) {

      State cellState = cell.getState();

      if (term == null) {
        // (3)
        // empty
      } else if (term.getState() == State.None) {
        if (cellState != State.None &&
            before.getState() != cellState) {
          // (1)
          get(cellState).add(term);
        }
      } else {
        // (2), (4)
        if (cellState == State.None &&
            before.getState() != State.None &&
            term.getState() != State.None
            ) {
          get(term.getState()).add(cell);
        }
      }

      // move "term"
      if (before != null &&
          before.getState() != cellState) {
        term = before;
      }

      // move "before"
      before = cell;
    }
  }


}
