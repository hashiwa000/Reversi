package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.util.RManager;

public class BiasedMMPlayer extends MinMaxPlayer {

  public BiasedMMPlayer(RManager manager) {
    super(manager, new BiasedEvaluator());
  }

}
