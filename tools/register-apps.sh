#!/usr/bin/env bash

curl -H "Accept: application/json" -X POST "http://localhost:9393/apps/source/http" -i \
    -d "uri=maven://org.springframework.cloud.stream.app:http-source-rabbit:2.0.3.RELEASE" \
    -d "force=true"

curl -H "Accept: application/json" -X POST "http://localhost:9393/apps/processor/geode-stream-processor" -i \
    -d "uri=maven://io.enfuse.pipeline.geode:geode-stream-processor:0.0.1-SNAPSHOT" \
    -d "force=true"

curl -H "Accept: application/json" -X POST "http://localhost:9393/apps/sink/log" -i \
    -d "uri=maven://org.springframework.cloud.stream.app:log-sink-rabbit:2.0.2.RELEASE" \
    -d "force=true"