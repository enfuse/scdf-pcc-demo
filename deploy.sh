#!/usr/bin/env bash

(cd SCDF/geode-stream-processor
./gradlew clean build install)

./tools/destroy-stream.sh

./tools/register-apps.sh

./tools/deploy-stream.sh