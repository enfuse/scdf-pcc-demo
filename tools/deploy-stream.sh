#!/usr/bin/env bash

set -x
set -eu

curl -H "Accept: application/json" -X POST "http://localhost:9393/streams/definitions" -i \
    -d "name=pcc-demo-stream" \
    -d "definition=http|geode-stream-processor|log"