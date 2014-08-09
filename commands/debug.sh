#!/bin/sh

PATH=$PATH:scripts
mvn-run -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y com.flowpowered.examples.cmd_example.CommandExample "$@"

