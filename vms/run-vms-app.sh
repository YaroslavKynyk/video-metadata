#!/bin/bash

# Variables
IMAGE_NAME="vms-app"
CONTAINER_NAME="vms-app-container"
SWAGGER_URL="http://localhost:8090/swagger-ui/index.html"

# Clean docker
docker rm -f $CONTAINER_NAME
docker rmi -f $IMAGE_NAME

# Build Docker image
docker build -t $IMAGE_NAME .

# Run Docker container
docker run -d --name $CONTAINER_NAME -p 8090:8090 $IMAGE_NAME

# Wait for the app to start (adjust sleep as needed)
echo "Waiting for the application to start..."
sleep 10

# Open Swagger UI in default browser
if command -v xdg-open &> /dev/null; then
    xdg-open $SWAGGER_URL
elif command -v open &> /dev/null; then
    open $SWAGGER_URL
else
    echo "Swagger UI available at: $SWAGGER_URL"
fi
