#!/bin/sh

HOME_DIR=$(cd $(dirname $0);pwd)
java -classpath ${HOME_DIR}/classes jp.hashiwa.reversi.play.PlayerVSComputer $1 $2
