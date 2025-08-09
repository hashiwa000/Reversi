package jp.hashiwa.reversi.player;

import java.util.HashSet;
import java.util.Set;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.util.RManager;
import jp.hashiwa.reversi.util.SelectableCells;

public class MinMaxPlayer extends AbstractPlayer {

  private static int searchDepth;
  static {
    String depthStr = System.getProperty("jp.hashiwa.reversi.minmax.depth", Integer.toString(3));
    try {
      searchDepth = Integer.parseInt(depthStr);
    } catch(NumberFormatException e) {
      throw new Error("jp.hashiwa.reversi.minmax.depth is need to be integer.", e);
    }
  }

  public static final Evaluator DEFAULT_EVALUATOR = new DefaultEvaluator();

  protected final Evaluator evaluator;

  public MinMaxPlayer(RManager manager) {
    this(manager, DEFAULT_EVALUATOR);
  }
  public MinMaxPlayer(RManager manager, Evaluator evaluator) {
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
    // expand() is dummy method
    return expand0(cloneCells(cells), piece, player, depth);
  }
  private double expand0(RCell[][] cells, RCell piece, State player, int depth) {

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
        return expand(cells, null, otherPlayer, depth-1);

      } else {
        // add piece
        double maxPoint = 0;
        if (otherPlayer == getManager().getCurrentState()) {
          // player's initial point.
          maxPoint = Integer.MIN_VALUE;
        } else {
          // enemy's initial point.
          maxPoint = Integer.MAX_VALUE;
        }

        for (RCell c: selectable) {
          double tmpPoint = expand(cells, c, otherPlayer, depth-1);

          if (otherPlayer == getManager().getCurrentState()) {
            // player try to maximize player's point.
            if (maxPoint < tmpPoint) {
              maxPoint = tmpPoint;
            }
          } else {
            // enemy try to minimize player's point.
            if (tmpPoint < maxPoint) {
              maxPoint = tmpPoint;
            }
          }
        }

        return maxPoint;
      }

    } else {
      // not expand, terminate search
      return evaluator.evaluate(cells, getManager().getCurrentState()); // white
    }
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
