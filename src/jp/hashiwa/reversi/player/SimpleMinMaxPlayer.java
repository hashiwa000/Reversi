package jp.hashiwa.reversi.player;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.util.RManager;
import jp.hashiwa.reversi.util.SelectableCells;

public class SimpleMinMaxPlayer extends AbstractPlayer {

  public static final int SEARCH_DEPTH = 10;

  public static final Evaluator DEFAULT_EVALUATOR = new DefaultEvaluator();
//  public static final Evaluator DEFAULT_EVALUATOR = new BiasedEvaluator();

  protected final Evaluator evaluator;

//  private List<SearchNode>
  public SimpleMinMaxPlayer(RManager manager) {
    this(manager, DEFAULT_EVALUATOR);
  }
  public SimpleMinMaxPlayer(RManager manager, Evaluator evaluator) {
    super(manager);
    this.evaluator = evaluator;
  }

  @Override
  public RCell next() {

    RCell[][] orgCells = getCells();

    LinkedList<SearchNode> nodes = new LinkedList<SearchNode>();

    // 初期リスト
    for (RCell c: new SelectableCells(orgCells).get(getPlayerState())) {
      nodes.add(new SearchNode(c, getPlayerState(), orgCells));
    }

    // pass
    if (nodes.size() == 0) return null;

//    // sort
//    Collections.sort(nodes);

    for (int cnt=0 ; cnt<SEARCH_DEPTH*nodes.size() ; cnt++) {

      // get highest node
      SearchNode target = nodes.remove();

      // 敵プレイヤーの手
      SearchNode otherNode = othersHand(target);

      // 自プレイヤーの手
      SearchNode playerNode = playerHand(otherNode);

      // target -> otherNode -> playerNode

      nodes.add(playerNode);
    }

    // result

    Collections.sort(nodes);
    Collections.reverse(nodes);

    // this object is instance in simulation
    // need to return real instance on RBoard
    RCell resultInSimulation = nodes.remove().firstCellInSearchResult();
    int x = resultInSimulation.getXIndex();
    int y = resultInSimulation.getYIndex();
    RCell result = getCells()[x][y];

    return result;
}

  private SearchNode playerHand(SearchNode node) {
    node.printForDebug();

    SelectableCells sc = new SelectableCells(node.cells);

    SearchNode maxNode = null;

    for (RCell candidate: sc.get(getPlayerState())) {
      SearchNode otherNode = new SearchNode(
          candidate, getPlayerState(), node.cells, node);

      if (maxNode==null || otherNode.compareTo(maxNode)>0) {
        maxNode = otherNode;
      }
    }

    // pass if minNode is null (selectable set is empty)
    if (maxNode == null) {
      maxNode = new SearchNode(
          null, getPlayerState(), node.cells, node);
    }

    return maxNode;
  }

  private SearchNode othersHand(SearchNode node) {
    node.printForDebug();

    SelectableCells sc = new SelectableCells(node.cells);

    SearchNode minNode = null;

    for (RCell candidate: sc.get(getPlayerState().reverse())) {
      SearchNode otherNode = new SearchNode(
          candidate, getPlayerState().reverse(), node.cells, node);

      // min
      // otherNode は相手にとってのポイントを保持しているため、
      // ポイントが最も高い otherNode を minNode とする。
      if (minNode==null || otherNode.compareTo(minNode)>0) {
        minNode = otherNode;
      }
    }

    // pass if minNode is null (selectable set is empty)
    if (minNode == null) {
      minNode = new SearchNode(
          null, getPlayerState().reverse(), node.cells, node);
    }

    return minNode;
  }

  // ------------ Search Node ------------

  private class SearchNode implements Comparable<SearchNode> {
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

    // pass ; if null
    // add piece ; otherwise
    private final RCell hand;

    private final State handState;

    // board
    private final RCell[][] cells;

    // node before this node
    private final SearchNode before;

    // evaluated point of this node
    private final double point;

    /**
     * 根ノード
     * @param hand
     * @param cells
     */
    private SearchNode(RCell hand, State handState, RCell[][] cells) {
      this(hand, handState, cells, null);
    }
    /**
     * 非根ノード
     * @param hand 手 (null ; if pass)
     * @param cells
     * @param before
     */
    private SearchNode(RCell hand, State handState, RCell[][] cells, SearchNode before) {
      this.cells = cloneCells(cells);
      this.hand = hand==null ? null : this.cells[hand.getXIndex()][hand.getYIndex()];
      this.handState = handState;
      this.before = before;

      if (this.hand != null) addPiece();

      this.point = evaluator.evaluate(this.cells, this.handState);
      if (RManager.DEBUG) System.out.println("point=" + this.point);
    }

    private void addPiece() {
      printForDebug();

      if (hand.getState() != State.None) {
        throw new Error("fatal error.");
      }

      hand.setState(handState);

      for (RCell c: RManager.reversedCells(
          cells, hand, new HashSet<RCell>())) {

        if (c.getState() != hand.getState().reverse()) {
          throw new Error("fatal error");
        }
        c.setState(handState);
      }

      printForDebug();
    }

    private RCell firstCellInSearchResult() {
      SearchNode node = this;

      while (node.before != null) {
        node = node.before;
      }

      return node.hand;
    }

    @Override
    public int compareTo(SearchNode o) {
      return (int)(point - o.point);
    }

    @Override
    public boolean equals(Object o) {
      return super.equals(o);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      sb.append(hand).append(',');
      sb.append(handState).append(',');
      sb.append(point).append(']');
      return sb.toString();
    }

    void printForDebug() {
      if (!RManager.DEBUG) return;

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

}
