package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public interface Evaluator {
  public double evaluate(RCell[][] cells, State state);
}
