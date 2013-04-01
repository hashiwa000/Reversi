package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.util.GameState;
import jp.hashiwa.reversi.util.RManager;
import jp.hashiwa.reversi.util.SelectableCells;

public abstract class AbstractPlayer implements Runnable {

  private static int intervalMsec = 500; // default interval
  static {
    try {
      setInterval(Integer.getInteger("jp.hashiwa.reversi.computerinterval"));
    } catch(NullPointerException e) {}
  }
  public static void setInterval(int newIntervalMsec) {
    intervalMsec = newIntervalMsec;
  }

  private final RManager manager;

  public AbstractPlayer(RManager manager) {
    this.manager = manager;
  }

  protected RCell[][] getCells() {
    return manager.getBoard().getCells();
  }

  protected State getPlayerState() {
    return manager.getCurrentState();
  }

  protected SelectableCells getSelectable() {
    return manager.getSelectable();
  }

  protected RManager getManager() {
    return manager;
  }

  public GameState play() {
    run();
    return manager.getGameState();
  }

  public abstract RCell next();

  @Override
  public void run() {

    try {
      Thread.sleep(intervalMsec);
    } catch(Exception e) {
      e.printStackTrace();
    }

    synchronized (manager) {

      RCell c = next();
      if (c == null) {
        // pass
        manager.pass();
        return;
      }

      GameState gs = manager.addPiece(c);
      if (gs == null) {
        String errMsg = "Error is occurred about Computer: piece is " + c;
        manager.getFrame().appendToConsole(errMsg);
        new Exception(errMsg).printStackTrace();
        return;
      }
      if (gs.isOver()) return;

      manager.getBoard().repaint();

    }
  }
}
