#!/bin/sh

set -e


echo "Waiting for Elasticsearch to be ready..."

until curl -s http://elasticsearch:9200/_cluster/health | grep -q '"status":"green"\|"status":"yellow"'; do
  sleep 2
done

echo "Elasticsearch is up. Creating index..."

curl -X PUT "http://elasticsearch:9200/music" \
  -H 'Content-Type: application/json' \
  -d @/mnt/init/settings.json


echo "Index created."
