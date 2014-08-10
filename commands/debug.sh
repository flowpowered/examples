#!/bin/sh

java -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y -jar target/cmd-example-*.jar "$@"

