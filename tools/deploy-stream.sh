#!/usr/bin/env bash

set -x
set -eu

curl -H "Accept: application/json" -X POST "http://localhost:9393/streams/definitions" -i \
    -d "name=pcc-demo-stream" \
    -d "definition=http --server.port=9998|geode-stream-processor --server.port=9997|log"

curl -H "Content-type: application/json" -X POST "http://localhost:9393/streams/deployments/pcc-demo-stream" -i \
    -d "{\"app.*.--spring.profiles.active\":\"docker\"}"
