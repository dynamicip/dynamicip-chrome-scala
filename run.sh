#!/usr/bin/env bash

set -e

sbt assembly
docker build -t dynamicip-chrome-scala .
docker run -v /dev/shm:/dev/shm dynamicip-chrome-scala