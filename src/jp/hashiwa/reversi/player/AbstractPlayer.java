package jp.hashiwa.reversi.player;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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
  private CountDownLatch signal;

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

  public GameState play(ExecutorService pool) {
    signal = new CountDownLatch(1);
    pool.execute(this);
    try {
      signal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
      pool.shutdownNow();
    }

    return manager.getGameState();
  }

  //  public RCell next() {
  //    synchronized (manager) {
  //      return next0();
  //    }
  //  }
  //  public abstract RCell next0();
  public abstract RCell next();

  @Override
  public void run() {

    try {
      Thread.sleep(intervalMsec);
    } catch(Exception e) {
      e.printStackTrace();
    }

    synchronized (manager) {

      try {

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

      } finally {
        if (signal != null) signal.countDown();
      }

    }
  }
}
