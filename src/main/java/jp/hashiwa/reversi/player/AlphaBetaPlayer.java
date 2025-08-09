package jp.hashiwa.reversi.player;

import java.util.HashSet;
import java.util.Set;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.util.RManager;
import jp.hashiwa.reversi.util.SelectableCells;

/**
 *
 * 参考: http://ja.wikipedia.org/wiki/%E3%82%A2%E3%83%AB%E3%83%95%E3%82%A1%E3%83%BB%E3%83%99%E3%83%BC%E3%82%BF%E6%B3%95
 *
 * @author Masatoshi
 *
 */
public class AlphaBetaPlayer extends AbstractPlayer {
  private static int searchDepth;
  static {
    String depthStr = System.getProperty("jp.hashiwa.reversi.alphabeta.depth", Integer.toString(5));
    try {
      searchDepth = Integer.parseInt(depthStr);
    } catch(NumberFormatException e) {
      throw new Error("jp.hashiwa.reversi.alphabeta.depth is need to be integer.", e);
    }
  }

  public static final Evaluator DEFAULT_EVALUATOR = new DefaultEvaluator();

  protected final Evaluator evaluator;

  public AlphaBetaPlayer(RManager manager) {
    this(manager, DEFAULT_EVALUATOR);
  }
  public AlphaBetaPlayer(RManager manager, Evaluator evaluator) {
    super(manager);
    this.evaluator = evaluator;
  }

  @Override
  public RCell next() {
    return search(getManager().getBoard().getCells());
  }

  private RCell search(RCell[][] _cells) {
    RCell[][] cells = cloneCells(_cells);
    Set<RCell> selectable = new SelectableCells(cells).get(getManager().getCurrentState());

    RCell maxCell = null;
    double maxPoint = Integer.MIN_VALUE;

    for (RCell c: selectable) {
      double tmpPoint = expand(cells, c, getManager().getCurrentState(), searchDepth);
      if (maxPoint < tmpPoint) {
        maxCell = c;
        maxPoint = tmpPoint;
      }
    }

    if (maxCell == null) return null;

    int x = maxCell.getXIndex();
    int y = maxCell.getYIndex();
    RCell resultCell = _cells[x][y];
    return resultCell;
  }

  /**
   * ボードに駒をおいて、さらに探索を進める。
   * @param cells ボード
   * @param piece 置く駒
   * @param player 駒を置くプレイヤー
   * @param depth 探索している深さ
   * @return 現在手番となっているプレイヤーにとっての評価値
   */
  private double expand(RCell[][] cells, RCell piece, State player, int depth) {
    return expand(cells, piece, player, -Integer.MAX_VALUE, Integer.MAX_VALUE, depth);
  }
  private double expand(RCell[][] cells, RCell piece, State player, double alpha, double beta, int depth) {
    cells = cloneCells(cells);

    if (piece != null) {
      int x = piece.getXIndex();
      int y = piece.getYIndex();
      addPiece(cells, x, y, player);
    }

    if (0 < depth) {
      // expand

      State otherPlayer = player.reverse();

      Set<RCell> selectable = new SelectableCells(cells).get(otherPlayer); // TODO if selectable is empty, terminate search

      if (selectable.isEmpty()) {
        // pass
        return expand(cells, null, otherPlayer, alpha, beta, depth-1);

      } else {
        // adding success
        if (otherPlayer == getManager().getCurrentState()){
          return calcPointOnPlayerTurn(cells, selectable, alpha, beta, depth);
        } else {
          return calcPointOnEnemyTurn(cells, selectable, alpha, beta, depth);
        }
      }

    } else {
      // not expand, terminate search
      return evaluator.evaluate(cells, getManager().getCurrentState()); // white
    }
  }

  private double calcPointOnEnemyTurn(RCell[][] cells, Set<RCell> selectable, double alpha, double beta, int depth) {
    State otherPlayer = getManager().getCurrentState().reverse();

    for (RCell c: selectable) {

      double tmpPoint = expand(cells, c, otherPlayer, alpha, beta, depth-1);

      beta = Math.min(beta, tmpPoint);
      if (beta <= alpha) return alpha;
    }

    return beta;
  }

  private double calcPointOnPlayerTurn(RCell[][] cells, Set<RCell> selectable, double alpha, double beta, int depth) {
    State otherPlayer = getManager().getCurrentState();

    for (RCell c: selectable) {

      double tmpPoint = expand(cells, c, otherPlayer, alpha, beta, depth-1);

      alpha = Math.max(alpha, tmpPoint);
      if (beta <= alpha) return beta;
    }

    return alpha;
  }

  /**
   * cells と同じ内容のボードを生成する。
   * @param cells
   * @return
   */
  private RCell[][] cloneCells(RCell[][] cells) {
    int num = cells.length;
    RCell[][] newCells = new RCell[num][];
    for (int i=0 ; i<num ; i++) newCells[i] = new RCell[num];

    for (int i=0 ; i<num ; i++) {
      for (int j=0 ; j<num ; j++) {
        newCells[i][j] = cells[i][j].clone();
      }
    }

    return newCells;
  }

  private final Set<RCell> reversedCellSet = new HashSet<RCell>();
  private void addPiece(RCell[][] cells, int x, int y, State player) {
    RCell cell = cells[x][y];

    if (cell.getState() != State.None) {
      throw new Error("fatal error.");
    }

    cell.setState(player);

    reversedCellSet.clear();
    for (RCell c: RManager.reversedCells(
        cells, cell, reversedCellSet)) {

      if (c.getState() != player.reverse()) {
        throw new Error("fatal error");
      }
      c.setState(player);
    }

    printForDebug(cells, "added: " + x + ", " + y);
  }

  void printForDebug(RCell[][] cells, String message) {
    if (!RManager.DEBUG) return;

    System.out.println(message);

    StringBuilder sb = new StringBuilder();

    sb.append("-------------------\n");

    for (int i=0 ; i<cells.length ; i++) {
      for (int j=0 ; j<cells[i].length ; j++) {
        switch(cells[j][i].getState()) {
        case None:
          sb.append(' '); break;
        case White:
          sb.append('o'); break;
        case Black:
          sb.append('x'); break;
        }
      }
      sb.append('\n');
    }

    sb.append("-------------------\n");

    System.out.println(sb.toString());
  }

}
