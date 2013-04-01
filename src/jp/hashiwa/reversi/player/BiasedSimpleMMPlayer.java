package jp.hashiwa.reversi.player;

import jp.hashiwa.reversi.util.RManager;

public class BiasedSimpleMMPlayer extends SimpleMinMaxPlayer {

  public BiasedSimpleMMPlayer(RManager manager) {
    super(manager, new BiasedEvaluator());
  }

}
