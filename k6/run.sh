#!/bin/sh

echo "1) streaming"
echo "2) batch"
read choice

if [ "$choice" = "1" ]; then
  k6 run /script/streaming.js
elif [ "$choice" = "2" ]; then
  k6 run /script/batch.js
else
  echo "invalid option"
fi