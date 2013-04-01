package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public class DefaultEvaluator implements Evaluator {

  @Override
  public double evaluate(RCell[][] cells, State state) {
    int num = cells.length;
    int point = 0;

    State reversedState = state.reverse();

    for (int i=0 ; i<num ; i++) {
      for (int j=0 ; j<num ; j++) {
        if (cells[i][j].getState() == state) {
          point += weight(cells, i, j);
        } else if (cells[i][j].getState() == reversedState) {
          point -= weight(cells, i, j);
        }
      }
    }

    return point;
  }

  protected double weight(RCell[][] cells, int x, int y) {
    return 1;
  }

}
