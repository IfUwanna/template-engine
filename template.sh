#!/bin/sh
echo "================ Jihun template-engine start :) ================"
java -jar template-engine.jar "$1" "$2" | tee
