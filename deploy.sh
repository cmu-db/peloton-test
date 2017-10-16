#!/usr/bin/env bash
./gradlew build
cp build/distributions/peloton-test.tar deploy/
