package jp.hashiwa.reversi.player;

import java.util.ArrayList;
import java.util.List;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.util.RManager;

public class RationalPlayer extends AbstractPlayer {

  public RationalPlayer(RManager manager) {
    super(manager);
  }

  @Override
  public RCell next() {
    RCell candidateCell = null;
    int candidatePoint = -1;
    for (RCell c: getSelectable().get(getPlayerState())) {
      int point = evaluate(c);

      if (candidatePoint < point) {
        candidateCell = c;
        candidatePoint = point;
      }
    }

    return candidateCell;
  }

  public int evaluate(RCell c) {
    int cnt = 0;

    RCell[][] cells = getCells();
    int x = c.getXIndex();
    int y = c.getYIndex();
    State state = getPlayerState();

    List<RCell> list = new ArrayList<RCell>();

    list.clear();
    cnt += getManager().reversedRightCells(x, y, state, list).size();
    list.clear();
    cnt += getManager().reversedLeftCells(x, y, state, list).size();
    list.clear();
    cnt += getManager().reversedUpCells(x, y, state, list).size();
    list.clear();
    cnt += getManager().reversedDownCells(x, y, state, list).size();

    list.clear();
    cnt += getManager().reversedRightUpCells(x, y, state, list).size();
    list.clear();
    cnt += getManager().reversedRightDownCells(x, y, state, list).size();
    list.clear();
    cnt += getManager().reversedLeftUpCells(x, y, state, list).size();
    list.clear();
    cnt += getManager().reversedLeftDownCells(x, y, state, list).size();

    return cnt;
  }

}
