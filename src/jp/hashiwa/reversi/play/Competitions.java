package jp.hashiwa.reversi.play;

import jp.hashiwa.reversi.frame.RCell.State;
import jp.hashiwa.reversi.player.AbstractPlayer;
import jp.hashiwa.reversi.player.BiasedABPlayer;
import jp.hashiwa.reversi.player.BiasedMMPlayer;
import jp.hashiwa.reversi.player.PlayerProvider;
import jp.hashiwa.reversi.util.RManager;

public class Competitions {

  static Class<? extends AbstractPlayer> p1Class = BiasedMMPlayer.class;
  static Class<? extends AbstractPlayer> p2Class = BiasedABPlayer.class;
//  static Class<? extends AbstractPlayer> p2Class = BiasedMinMaxPlayer.class;

  private static final boolean SHOW_PROGRESS = true;

  /**
   *
   * @param args the number of games (if needed)
   */
  public static void main(String[] args) {
    // interval <- 0 msec
    AbstractPlayer.setInterval(0);

    int gameNum = 10;
    int p1WinNum = 0;
    int p2WinNum = 0;

    try {
      if (args.length == 1) gameNum = Integer.parseInt(args[0]);
    } catch(NumberFormatException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (SHOW_PROGRESS) {
      System.out.println(p1Class.getSimpleName() + " VS " + p2Class.getSimpleName());
      System.out.println("start[          ]finish");
      System.out.print  ("     [");
    }

    for (int i=0 ; i<gameNum ; i++) {
      final RManager manager = new RManager(false);

      State winner = play(manager, i);

      if (i%2==0) {
        if (winner == State.Black) {
          p1WinNum++;
        } else if (winner == State.White) {
          p2WinNum++;
        }
      } else {
        if (winner == State.Black) {
          p2WinNum++;
        } else if (winner == State.White) {
          p1WinNum++;
        }
      }

      if (SHOW_PROGRESS) {
        if (i%(gameNum/10)==0) {
          System.out.print('#');
        }
      }
    }

    if (SHOW_PROGRESS) {
      System.out.println("] --> finish!");
    }

    System.out.println(p1Class.getSimpleName() + ": " + p1WinNum + "(" + (100*p1WinNum/gameNum) + "%)");
    System.out.println(p2Class.getSimpleName() + ": " + p2WinNum + "(" + (100*p2WinNum/gameNum) + "%)");
    int drawNum = gameNum - p1WinNum - p2WinNum;
    System.out.println("Draw: " + drawNum + "(" + (100*drawNum/gameNum) + "%)");
  }

  static State play(RManager manager, int gameIndex) {
    PlayerProvider provider = new PlayerProvider(manager);

    // player setting
    AbstractPlayer player1=null, player2=null;

    if (gameIndex%2==0) {
      player1 = provider.getPlayer(p1Class);
      player2 = provider.getPlayer(p2Class);
    } else {
      player1 = provider.getPlayer(p2Class);
      player2 = provider.getPlayer(p1Class);
    }

    // play!
    for (;;) {
      if (player1.play().isOver()) break;
      if (player2.play().isOver()) break;
    }

    return manager.getGameState().winner();
  }

}
