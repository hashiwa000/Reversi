package jp.hashiwa.reversi.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.util.RManager;

public class RandomPlayer extends AbstractPlayer {

  private List<RCell> tmpList = new ArrayList<RCell>();

  public RandomPlayer(RManager manager) {
    super(manager);
  }

  @Override
  public RCell next() {
    Set<RCell> selectable = getSelectable().get(getPlayerState());

    if (selectable.isEmpty()) return null;

    tmpList.clear();
    for (RCell c: selectable) tmpList.add(c);

    int index = (int)(Math.random()*tmpList.size());
    return tmpList.get(index);
  }

}
