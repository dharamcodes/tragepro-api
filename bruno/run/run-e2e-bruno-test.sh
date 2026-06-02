#!/bin/bash
# Consolidated Test Runner for tragepro-api and Bruno Integration Tests

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$ROOT_DIR" || exit 1

# Setup build directory if not exists
mkdir -p build

echo -e "${BLUE}=============================================${NC}"
echo -e "${BLUE}       Preparing Test Environment            ${NC}"
echo -e "${BLUE}=============================================${NC}"

# 1. Clean up existing processes on port 8082
EXISTING_PID=$(lsof -t -i :8082)
if [ ! -z "$EXISTING_PID" ]; then
    echo -e "${YELLOW}Killing existing process on port 8082 (PID: $EXISTING_PID)...${NC}"
    kill -9 "$EXISTING_PID" >/dev/null 2>&1
    sleep 1
fi

# 2. Stop database and clean volumes to avoid stale credentials/data
echo -e "${YELLOW}Stopping database container and pruning stale volumes...${NC}"
docker compose down -v >/dev/null 2>&1
docker volume prune -f >/dev/null 2>&1

# 3. Start clean MongoDB container
echo -e "${YELLOW}Starting MongoDB container...${NC}"
docker compose up -d
if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Failed to start MongoDB container.${NC}"
    exit 1
fi

# 4. Boot Spring Boot Application in the background
echo -e "${YELLOW}Starting Spring Boot Application via bootRun...${NC}"
./gradlew bootRun --no-daemon > build/bootRun.log 2>&1 &
BOOT_PID=$!

# Register cleanup function to run on script exit/interruption
cleanup() {
    echo -e "\n${BLUE}=============================================${NC}"
    echo -e "${BLUE}       Cleaning Up Test Environment          ${NC}"
    echo -e "${BLUE}=============================================${NC}"
    
    if ps -p "$BOOT_PID" > /dev/null; then
        echo -e "${YELLOW}Stopping Spring Boot Application (PID: $BOOT_PID)...${NC}"
        kill "$BOOT_PID" >/dev/null 2>&1
        # Wait a bit for it to shutdown cleanly
        sleep 2
    fi
    
    echo -e "${YELLOW}Stopping MongoDB container and removing volumes...${NC}"
    docker compose down -v >/dev/null 2>&1
    echo -e "${GREEN}Cleanup complete.${NC}"
}
trap cleanup EXIT

# 5. Wait for Spring Boot to be fully ready
echo -e "${YELLOW}Waiting for application to be ready on port 8082...${NC}"
TIMEOUT=60
ELAPSED=0
STARTED=false

while [ $ELAPSED -lt $TIMEOUT ]; do
    if ! ps -p "$BOOT_PID" > /dev/null; then
        echo -e "${RED}Error: Spring Boot application terminated unexpectedly.${NC}"
        echo "Check build/bootRun.log for details."
        exit 1
    fi
    
    # Poll the permitted openapi api-docs endpoint
    STATUS_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8082/api-docs)
    if [ "$STATUS_CODE" = "200" ]; then
        STARTED=true
        break
    fi
    
    sleep 2
    ELAPSED=$((ELAPSED + 2))
done

if [ "$STARTED" = false ]; then
    echo -e "${RED}Error: Application failed to start within $TIMEOUT seconds.${NC}"
    echo "Check build/bootRun.log for details."
    exit 1
fi

echo -e "${GREEN}✓ Application is up and running!${NC}"

# 6. Execute Bruno Integration Tests
echo -e "${BLUE}=============================================${NC}"
echo -e "${BLUE}       Running Bruno Integration Tests       ${NC}"
echo -e "${BLUE}=============================================${NC}"

./bruno/run/run-bruno-tests-only.sh "$@"
TEST_EXIT_CODE=$?

# Cleanup will be automatically executed by the trap on exit
exit $TEST_EXIT_CODE
