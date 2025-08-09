package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.util.RManager;

public class BiasedABPlayer extends AlphaBetaPlayer {

  public BiasedABPlayer(RManager manager) {
    super(manager, new BiasedEvaluator());
  }

}
