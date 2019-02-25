#!/usr/bin/env bash

# ##################################################
# generateSampleEntities.sh
# Generates & executes curl commands in order to PUT data
# into PCC via REST API in order to update or insert sample
# entries in region 'exampleRegion'.
#
# Usage:
#   $ ./generateSampleEntities.sh [START_INDEX] [NUMBER_TO_GENERATE] [DATA_REGION]
#
#   * START_INDEX (optional): Start index to increment from (default 1)
#   * NUMBER_TO_GENERATE (optional): number of entries to be generated. Default 10
#   * DATA_REGION (optional): Data region to run curl PUT against
# ##################################################

# $1 Start index (default 1)
START_INDEX=${1:-1}

# $2 Number of entity PUTS to generate (default 10)
NUMBER_TO_GENERATE=${2:-10}

# $3 Data Region name
REGION_NAME=${3:-"exampleRegion"}

# base curl command to build on
CURL_STRING="curl -i -H 'Content-Type: application/json' -X PUT localhost:7071/gemfire-api/v1/${REGION_NAME}/"

# createCurlCommand()
#
# Builds & executes a curl PUT command against Gemfire REST API
#
# -----------------------------------
# $1    Unique id used to generate key and value
# -----------------------------------
createCurlCommand(){
    ID=$1

    # use id to create a key
    ID_STRING=${ID}-$((ID+100))

    # use id to create simple json payload
    PAYLOAD="{\\\"id\\\":\\\"$ID_STRING\\\",\\\"value\\\":\\\"value$ID\\\"}"

    # append generated ID_STRING to base curl command
    CURL_COMMAND=${CURL_STRING}${ID_STRING}

    # evaluate to execute generated CURL_COMMAND with PAYLOAD
    eval ${CURL_COMMAND} -d \"${PAYLOAD}\"

     echo ${CURL_COMMAND} -d \"${PAYLOAD}\"
}

# calculate end of range
END=$((START_INDEX + NUMBER_TO_GENERATE -1))

# generate NUMBER_OF_ENTITIES_TO_GENERATE number of curl commands
for index in $(seq ${START_INDEX} ${END}); do
 createCurlCommand ${index}
done

