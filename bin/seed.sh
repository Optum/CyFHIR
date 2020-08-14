#!/usr/bin/env bash

set -e

(
cd synthea-bundles
start=`ruby -e 'puts Time.now.to_f.round(3)*1000' | cut -d'.' -f1`
resourceCount="0"
bundleCount="0"
for file in ./*; do
  echo "Seeding: ${file##*/}"
  resourceCount=$((resourceCount+`tr ' ' '\n' < "${file##*/}" | grep resourceType | wc -l| awk '{$1=$1;print}'`))
  bundleCount=$((bundleCount + 1))
  curl -s -X POST -H "Content-Type: application/json" -d @"${file##*/}" http://localhost:3000/api/Bundle > /dev/null
done
echo "Seed Time: $(($(ruby -e 'puts Time.now.to_f.round(3)*1000'| cut -d'.' -f1)-$start)) milliseconds"
echo "${bundleCount} Bundles with ${resourceCount} total resources"
)
