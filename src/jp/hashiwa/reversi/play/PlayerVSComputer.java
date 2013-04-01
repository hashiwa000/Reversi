package jp.hashiwa.reversi.play;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.hashiwa.reversi.event.ReversiEvent;
import jp.hashiwa.reversi.event.ReversiEventListener;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.MinMaxPlayer;
import jp.hashiwa.reversi.util.GameState;
import jp.hashiwa.reversi.util.RManager;

public class PlayerVSComputer {

  private static final String PLAYER_CLASS_PREFIX = "jp.hashiwa.reversi.player.";
  private static final Class<? extends AbstractPlayer> DEFAULT_PLAYER_CLASS =
    MinMaxPlayer.class;

  private static AbstractPlayer computer;

  static {
    try {
      Class.forName(DEFAULT_PLAYER_CLASS.getName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    final RManager manager = new RManager();

    if (!parseArgumentsAndSetPlayer(args, manager)) System.exit(1);
    manager.getFrame().appendToConsole("Player VS " + computer.getClass().getSimpleName());

    final ExecutorService pool = Executors.newSingleThreadExecutor();

    manager.getBoard().addReversiEventListener(new ReversiEventListener() {

      @Override
      public void handleEvent(ReversiEvent e) {

        RCell cell = e.getCell();

        if (manager.getGameState().isOver()) {
          // game is over
          return;

        } else if (manager.canAdd(cell)) {

          // can add piece to the cell
          GameState gs = manager.addPiece(cell);

          if (gs == null) return;
          if (gs.isOver()) return;

        } else {

          // cannot add piece, so pass
          manager.pass();
        }

//        if (computer.play(pool).isOver()) return;
        while (true) {
          // add piece (computer)
          if (computer.play(pool).isOver()) return;

          // if can add piece, turn over (player)
          if (manager.canAdd()) return;

          // if cannot add piece, repeat (next, computer turn)
          manager.pass();
        }
      }
    });

    // cannot shutdown here.
//    pool.shutdownNow();
  }

  private static boolean parseArgumentsAndSetPlayer(String[] args, RManager manager) throws Exception {
    if (args.length != 1) {
      Constructor con = DEFAULT_PLAYER_CLASS.getConstructor(new Class[]{RManager.class});
      computer = (AbstractPlayer)con.newInstance(manager);
      return true;
    }

    String p1ClassName = PLAYER_CLASS_PREFIX + args[0];

    try {
      Class<? extends AbstractPlayer> p1Class = (Class<? extends AbstractPlayer>) Class.forName(p1ClassName);
      Constructor con1 = p1Class.getConstructor(new Class[]{RManager.class});
      computer = (AbstractPlayer)con1.newInstance(manager);

      return true;
    } catch (ClassNotFoundException e) {
      // use default player
      Constructor con = DEFAULT_PLAYER_CLASS.getConstructor(new Class[]{RManager.class});
      computer = (AbstractPlayer)con.newInstance(manager);


      manager.getFrame().appendToConsole("Class is not found. Use default player.");

      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

}
