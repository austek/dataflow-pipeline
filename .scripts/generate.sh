#!/usr/bin/env sh

SN=${1:-1}
TN=${2:-1}


{ for i in $(seq 1 $SN); do
  for j in $(seq 1 $TN); do
    echo '{"name": "Sensor-'$i'", "value": '$(gshuf -i 20-49 -n 1)'}'; done; done; } \
    | curl -X POST -T - -H "Content-Type: application/x-ndjson" localhost:8081/sensors