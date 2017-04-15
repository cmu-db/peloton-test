#!/usr/bin/env bash
input="$@"
args=$(printf ",%s" ""${input[@]})
args=${args:1}
gradle run -Pargs=${args}