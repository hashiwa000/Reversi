package jp.hashiwa.reversi.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import jp.hashiwa.reversi.event.ReversiEvent;
import jp.hashiwa.reversi.event.ReversiEventListener;
import jp.hashiwa.reversi.frame.RCell.State;

/**
 * リバーシボード
 * @author Masatoshi
 *
 */
public class RBoard extends JPanel {

  private final RCell[][] cells;
  private final int cellNum;
  private final int cellLength;
  private final List<ReversiEventListener> listeners =
    new ArrayList<ReversiEventListener>();

  private RCell lastAdded = null;

  /**
   * ボードを作成する。
   * @param cellLength 一つのセルの縦、横の長さ
   * @param cellNum ボードの縦、横のセル数
   */
  RBoard(final int cellLength, final int cellNum) {
    this.cells = new RCell[cellNum][cellNum];
    this.cellLength = cellLength;
    this.cellNum = cellNum;

    setBackground(Color.white);

    final RBoard board = this;
    addMouseListener(new MouseListener() {

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      private boolean processing = false;

      @Override
      public void mouseClicked(MouseEvent e) {
        if (processing) return;
        processing = true;

        try {
          ReversiEvent re = new ReversiEvent(
              board,
              e.getX()/cellLength,
              e.getY()/cellLength);

          for (ReversiEventListener listener: listeners) {
            listener.handleEvent(re);
            board.repaint();
          }
        } finally {
          processing = false;
        }
      }
    });

    init();
  }

  private void init() {
    for (int i=0 ; i<cellNum ; i++) {
      for (int j=0 ; j<cellNum ; j++) {
        cells[i][j] = new RCell(
            cellLength*i,
            cellLength*j,
            cellLength);
      }
    }

    final int half = cellNum/2;
    cells[half-1][half-1].setState(State.White);
    cells[half  ][half-1].setState(State.Black);
    cells[half-1][half  ].setState(State.Black);
    cells[half  ][half  ].setState(State.White);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    if (lastAdded != null) {
      g.setColor(Color.pink);
      g.fillRect(lastAdded.getX(), lastAdded.getY(), lastAdded.getLength(), lastAdded.getLength());
    }

    for (int i=0 ; i<cells.length ; i++)
      for (int j=0 ; j<cells[i].length ; j++)
        cells[i][j].paint(g);

    g.setColor(Color.black);

    // vertical
    for (int i=1 ; i<=cellNum ; i++) {
      g.drawLine(
          cellLength*i,
          0,
          cellLength*i,
          cellLength*cellNum);
    }

    // horizontal
    for (int i=1 ; i<=cellNum ; i++) {
      g.drawLine(
          0,
          cellLength*i,
          cellLength*cellNum,
          cellLength*i);
    }

  }

  /**
   * ボードの縦、横方向のセル数を取得する。
   * @return
   */
  public int getCellNum() {
    return cellNum;
  }

  public RCell[][] getCells() {
    return cells;
  }

  /**
   * セルを取得する。
   * @param x
   * @param y
   * @return
   */
  public RCell getCell(int x, int y) {
    if (cells.length<=x ||
        x<0 ||
        cells[x].length<=y ||
        y<0) {
      return null;
    }
    return cells[x][y];
  }

  public void setLastAddedCell(RCell cell) {
    this.lastAdded = cell;
  }

  /**
   * リバーシ用のイベントリスナーを登録する。
   * @param l
   */
  public void addReversiEventListener(ReversiEventListener l) {
    listeners.add(l);
  }


}
