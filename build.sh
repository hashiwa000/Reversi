#!/bin/sh

HOME_DIR=$(cd $(dirname $0);pwd)
if [ ! -d ${HOME_DIR}/classes ] ; then
    mkdir ${HOME_DIR}/classes
fi
find ${HOME_DIR}/src/ -type f | xargs javac -d ${HOME_DIR}/classes/
