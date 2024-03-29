package jp.hashiwa.reversi.play;

import jp.hashiwa.reversi.event.ReversiEvent;
import jp.hashiwa.reversi.event.ReversiEventListener;
import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.AlphaBetaPlayer;
import jp.hashiwa.reversi.player.PlayerProvider;
import jp.hashiwa.reversi.util.GameState;
import jp.hashiwa.reversi.util.RManager;

public class PlayerVSComputer {

  private static final Class<? extends AbstractPlayer> DEFAULT_PLAYER_CLASS =
    AlphaBetaPlayer.class;
  private static final String COMPUTER_THREAD_NAME = "COMPUTER_NOW_PLAYING";

  private static AbstractPlayer computer;

  private static boolean clickable = true;

  public static void main(String[] args) throws Exception {
    final RManager manager = new RManager();

    if (!parseArgumentsAndSetPlayer(args, manager)) System.exit(1);
    manager.getFrame().appendToConsole("Player VS " + computer.getClass().getSimpleName());

    manager.getBoard().addReversiEventListener(new ReversiEventListener() {

      @Override
      public void handleEvent(ReversiEvent e) {

        if (!clickable) return;

        RCell cell = e.getCell();

        // game is over
        if (manager.getGameState().isOver()) return;

        // if game is not over and
        // cell cannot be added,
        // terminate click operation.
        //
        // current player should be able to
        // click any cell.
        // (pass should not be occurred.)
        if (!manager.canAdd()) return;

        // can add piece to the cell
        GameState gs = manager.addPiece(cell);

        if (gs == null) return;
        if (gs.isOver()) return;

        createNewThreadForComputerPlay(manager);

      }
    });
  }

  private static synchronized void createNewThreadForComputerPlay(final RManager manager) {
    new Thread(COMPUTER_THREAD_NAME){

      @Override
      public void run() {
        clickable = false;
        doIt();
        clickable = true;
      }

      private void doIt() {
        while (true) {

//          if (manager.getGameState().isOver()) return;

          // add piece (computer)
          if (computer.play().isOver()) return;

          // if can add piece, turn over (player)
          if (manager.canAdd()) return;

          // if cannot add piece, repeat (next, computer turn)
          manager.pass();
        }
      }

    }.start();
  }

  private static boolean parseArgumentsAndSetPlayer(String[] args, RManager manager) throws Exception {
    PlayerProvider provider = new PlayerProvider(manager);

    if (args.length == 1) {
      computer = provider.getPlayer(args[0]);
      if (computer != null) return true;
    }

    computer = provider.getPlayer(DEFAULT_PLAYER_CLASS);
    if (computer != null) return true;
    return false;
  }

}
