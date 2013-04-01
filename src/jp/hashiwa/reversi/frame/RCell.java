package jp.hashiwa.reversi.frame;

import java.awt.Color;
import java.awt.Graphics;

public class RCell {

  private State state = State.None;
  private final int x, y, length;

  RCell(int x, int y, int length) {
    this.x = x;
    this.y = y;
    this.length = length;
  }

  public void paint(Graphics g) {
    switch (state) {
    case None:
      break;
    case White:
      g.setColor(Color.black);
      g.drawOval(x, y, length, length);
      break;
    case Black:
      g.setColor(Color.black);
      g.fillOval(x, y, length, length);
      break;
    }
  }

  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }
  public int getXIndex() {
    return x/length;
  }
  public int getYIndex() {
    return y/length;
  }
  public int getLength() {
    return length;
  }

  public void setState(State state) {
    this.state = state;
  }
  public State getState() {
    return state;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[').append(getState()).append(',');
    sb.append(getXIndex()).append(',').append(getYIndex());
    sb.append(']');

    return sb.toString();
  }

  public enum State {
    /**
     * 駒が置かれていない状態
     */
    None,
    /**
     * 白駒が置かれた状態
     */
    White,
    /**
     * 黒駒が置かれた状態
     */
    Black;
    public State reverse() {
      switch(this) {
      case White:
        return Black;
      case Black:
        return White;
      }
      return null;
    }
  }
}
