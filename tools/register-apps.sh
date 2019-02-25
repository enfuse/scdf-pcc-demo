#!/usr/bin/env bash

curl -H "Accept: application/json" -X POST "http://localhost:9393/apps/processor/geode-stream-processor" -i \
    -d "uri=maven://io.enfuse.pipeline.geode:geode-stream-processor:0.0.1-SNAPSHOT" \
    -d "force=true"