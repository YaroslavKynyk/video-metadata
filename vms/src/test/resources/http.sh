#!/bin/bash

# Health check using curl
URL="http://localhost:8090/api/v1/videos/health"

response=$(curl -s -o /dev/null -w "%{http_code}" "$URL")

if [ "$response" -eq 200 ]; then
    echo "Health check passed."
    exit 0
else
    echo "Health check failed with status code: $response"
    exit 1
fi