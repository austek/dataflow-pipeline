#!/usr/bin/env sh

SN=${1:-1}
TN=${2:-1}

{ for i in $(seq 1 $SN); do
  for j in $(seq 1 $TN); do
    gecho -ne 'curl -s -X POST -d '\''{"name": "Sensor-'$i'", "value": '$(gshuf -i 20-49 -n 1)'}'\'' -H "Content-Type: application/x-ndjson" localhost:8081/sensors''\0'; done; done; } \
    | gxargs -0 -P 2 -n 100 parallel -j 25 --
