#!/usr/bin/env sh

apk add curl

GEODE_SERVER_URL=${GEODE_SERVER_URL:-http://server.localhost:7070}

while ! nc -zw2 server.localhost 7070 &>/dev/null; do
    true
done

curl -i -H 'Content-Type: application/json' -X PUT ${GEODE_SERVER_URL}/gemfire-api/v1/exampleRegion/1 \
    -d "{\"keyOne\":1,\"keyTwo\":2,\"value\":100}"

curl -i -H 'Content-Type: application/json' -X PUT ${GEODE_SERVER_URL}/gemfire-api/v1/exampleRegion/2 \
    -d "{\"keyOne\":3,\"keyTwo\":4,\"value\":101}"

curl -i -H 'Content-Type: application/json' -X PUT ${GEODE_SERVER_URL}/gemfire-api/v1/exampleRegion/3 \
    -d "{\"keyOne\":5,\"keyTwo\":6,\"value\":102}"

curl -i -H 'Content-Type: application/json' -X PUT ${GEODE_SERVER_URL}/gemfire-api/v1/exampleRegion/4 \
    -d "{\"keyOne\":7,\"keyTwo\":8,\"value\":103}"

curl -i -H 'Content-Type: application/json' -X PUT ${GEODE_SERVER_URL}/gemfire-api/v1/exampleRegion/5 \
    -d "{\"keyOne\":9,\"keyTwo\":10,\"value\":104}"