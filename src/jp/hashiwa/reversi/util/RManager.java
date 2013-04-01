package jp.hashiwa.reversi.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.hashiwa.reversi.frame.RBoard;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RFrame;
import jp.hashiwa.reversi.frame.RCell.State;

public class RManager {
  // ----------- static fields -----------
  public static final State INITIAL_STATE = State.Black;
  private final boolean DEBUG = Boolean.getBoolean("jp.hashiwa.reversi.debug");

  // ----------- fields -----------

  private RFrame frame;
//  private Map<State, Set<ReversiCell>> selectable = null;
  private SelectableCells selectable = null;
  private GameState gameState = null;
  private State currentState = INITIAL_STATE;

  // ----------- constructors -----------

  public RManager() {
    try {
      frame = new RFrame();
      appendToConsole("Start", currentState);
    } catch(Exception e) {
      e.printStackTrace();
      appendToConsole("Error is occurred.", currentState);
    }
  }
  public RManager(boolean visible) {
    try {
      frame = new RFrame(visible);
      appendToConsole("Start", currentState);
    } catch(Exception e) {
      e.printStackTrace();
      appendToConsole("Error is occurred.", currentState);
    }
  }

  // ----------- methods -----------

  public RBoard getBoard() {
    return frame.getBoard();
  }

  public RFrame getFrame() {
    return frame;
  }

  /**
   * 駒を置ける場所の集合を取得する。
   * @return
   */
  public SelectableCells getSelectable() {

    if (selectable == null) {
      selectable = new SelectableCells(this);
    }

    return selectable;
  }

  /**
   * 計算しておいた、駒を置ける場所の集合を削除する。
   */
  private void clearSelectable() {
    selectable = null;
  }

  /**
   * 現在手番となっている駒を返す。
   * @return
   */
  public State getCurrentState() {
    return currentState;
  }

  /**
   * 引数の場所が置けるかどうかを判断する。
   * @param cell
   * @return
   */
  public boolean containsInSelectable(RCell cell) {
    return getSelectable().get(currentState).contains(cell);
  }

  /**
   * 駒を置く。
   * 駒がまだ置かれておらず、駒を置くことで他色の駒を裏返せる場合に成功する。
   * @param piece 置く駒
   * @return 駒を置くことに成功した場合、ゲームの状態が返る。
   */
  public synchronized GameState addPiece(RCell piece) {
    // ---- 駒を置く前の確認 ----

    // ゲーム状態の確認
    GameState gs = getGameState();
    if (gs.isOver()) {
      appendToConsole("Game is over! Winner is " + gs.winner());
      setBoardColor(Color.gray);
      return null;
    }

    // 駒が置かれようとしている場所の確認
    if (piece == null) return null;
    if (piece.getState() != State.None) return null;
    if (!containsInSelectable(piece)) return null;

    // ---- 駒を置く処理 ----

    // 駒を置いて、ひっくり返す。
    piece.setState(currentState);
    reverse(piece);
    appendToConsole("Success: add piece " + piece, currentState);
    currentState = currentState.reverse();

    clearSelectable();
    clearGameState();

    // ---- 駒を置いた後の確認 ----
    gs = getGameState();
    if (gs.isOver()) {
      appendToConsole("Game is over! Winner is " + gs.winner());
    }

    if (DEBUG) {
      System.out.println("----- " + piece + " -----");
      for (int i=0 ; i<getBoard().getCellNum() ; i++) {
        for (int j=0 ; j<getBoard().getCellNum() ; j++) {
          State s = getBoard().getCell(j, i).getState();
          switch (s) {
          case None:
            System.out.print(' ');
            break;
          case White:
            System.out.print('o');
            break;
          case Black:
            System.out.print('x');
            break;
          }
        }
        System.out.println();
      }
    }

    return gs;
  }

  public void pass() {
    appendToConsole("Pass.", currentState);
    currentState = currentState.reverse();
  }

  public GameState getGameState() {
    if (gameState == null) {
      gameState = new GameState(this);
    }

    return gameState;
  }

  private void clearGameState() {
    gameState = null;
  }

  private void reverse(RCell piece) {
    for (RCell cell: reversedCells(piece, new HashSet<RCell>())) {
      cell.setState(currentState);
    }

  }

  private void appendToConsole(String line) {
    frame.appendToConsole(line);
  }
  private void appendToConsole(String line, State state) {
    frame.appendToConsole("[" + state + "]: " + line);
  }

  private void setBoardColor(Color c) {
    frame.getBoard().setBackground(c);
  }

  // -------- utility methods --------

  /**
   * 右方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> rightCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    for (int xi=x ; xi<cells.length ; xi++) {
      RCell c = cells[xi][y];
      list.add(c);
    }
    return list;
  }

  /**
   * 左方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> leftCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    for (int xi=x ; 0<=xi ; xi--) {
      RCell c = cells[xi][y];
      list.add(c);
    }
    return list;
  }

  /**
   * 上方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> upCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    for (int yi=y ; 0<=yi ; yi--) {
      RCell c = cells[x][yi];
      list.add(c);
    }
    return list;
  }

  /**
   * 下方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> downCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    for (int yi=y ; yi<cells.length ; yi++) {
      RCell c = cells[x][yi];
      list.add(c);
    }
    return list;
  }

  /**
   * 右下方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> rightDownCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    int xi=x; int yi=y;
    while (xi<cellNum && yi<cellNum) {
      RCell c = cells[xi][yi];
      list.add(c);
      xi++; yi++;
    }
    return list;
  }

  /**
   * 右上方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> rightUpCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    int xi=x; int yi=y;
    while (xi<cellNum && 0<=yi) {
      RCell c = cells[xi][yi];
      list.add(c);
      xi++; yi--;
    }
    return list;
  }

  /**
   * 左下方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> leftDownCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int cellNum = cells.length;
    if (cellNum<=x || x<0 ||
        cellNum<=y || y<0) {
      return list;
    }

    int xi=x; int yi=y;
    while (0<=xi && yi<cellNum) {
      RCell c = cells[xi][yi];
      list.add(c);
      xi--; yi++;
    }
    return list;
  }

  /**
   * 左下方向の駒リストをリストに追加する。
   * @param cells ボード
   * @param x
   * @param y
   * @param list
   * @return
   */
  public synchronized List<RCell> leftUpCells(int x, int y, List<RCell> list) {
    RCell[][] cells = getBoard().getCells();

    int xi=x; int yi=y;
    while (0<=xi && 0<=yi) {
      RCell c = cells[xi][yi];
      list.add(c);
      xi--; yi--;
    }
    return list;
  }

  private List<RCell> candidates = new ArrayList<RCell>();
  private List<RCell> tmps = new ArrayList<RCell>();

  public synchronized List<RCell> reversedRightCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: rightCells(x+1, y, tmps)) {
      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedLeftCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: leftCells(x-1, y, tmps)) {

      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedUpCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: upCells(x, y-1, tmps)) {

      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedDownCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: downCells(x, y+1, tmps)) {
      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedRightUpCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: rightUpCells(x+1, y-1, tmps)) {
      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedRightDownCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: rightDownCells(x+1, y+1, tmps)) {
      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedLeftUpCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: leftUpCells(x-1, y-1, tmps)) {
      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  public synchronized List<RCell> reversedLeftDownCells(
      int x, int y, State state, List<RCell> reversed) {

    RCell[][] cells = getBoard().getCells();

    candidates.clear();
    tmps.clear();
    for (RCell candidate: leftDownCells(x-1, y+1, tmps)) {
      if (candidate == null) break;

      if (candidate.getState() == state.reverse()) {
        candidates.add(candidate);
      } else {
        if (candidate.getState() == state) {
          reversed.addAll(candidates);
        }
        break;
      }
    }
    return reversed;
  }

  /**
   * 駒を置くことで裏返る駒の集合を取得する。
   * @param cells ボード
   * @param piece 置く駒
   * @param reversed
   * @return 裏返る駒の集合
   */
  public synchronized Set<RCell> reversedCells(RCell piece, Set<RCell> reversed) {

    State state = piece.getState();
    int x = piece.getXIndex();
    int y = piece.getYIndex();

    List<RCell> reversedList = new ArrayList<RCell>();

    reversedRightCells(x, y, state, reversedList);
    reversedLeftCells(x, y, state, reversedList);
    reversedUpCells(x, y, state, reversedList);
    reversedDownCells(x, y, state, reversedList);

    reversedRightUpCells(x, y, state, reversedList);
    reversedRightDownCells(x, y, state, reversedList);
    reversedLeftUpCells(x, y, state, reversedList);
    reversedLeftDownCells(x, y, state, reversedList);

    for (RCell c: reversedList) reversed.add(c);

    return reversed;
  }
}
