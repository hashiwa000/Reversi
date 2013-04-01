package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.util.RManager;

public class BiasedMinMaxPlayer extends MinMaxPlayer {

  public BiasedMinMaxPlayer(RManager manager) {
    super(manager, new BiasedEvaluator());
  }

}
