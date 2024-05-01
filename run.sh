#!/bin/sh

HOME_DIR=$(cd $(dirname $0);pwd)

PROPS=""
#PROPS="\
#  -Djp.hashiwa.reversi.alphabeta.depth=5 \
#  -Djp.hashiwa.reversi.minmax.depth=3 \
#"

java \
  -classpath ${HOME_DIR}/classes \
  -server \
  $PROPS \
  jp.hashiwa.reversi.play.PlayerVSComputer $1 $2
