#!/bin/bash
# Consolidated Test Runner for tragepro-api and Bruno Integration Tests

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
BOLD='\033[1m'
NC='\033[0m' # No Color

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$ROOT_DIR" || exit 1

mkdir -p build

echo -e "\n${BOLD}${BLUE}🚀 Starting Bruno E2E Test Suite${NC}\n"

# 1. Clean up existing processes
echo -ne "${YELLOW}[1/6] 🧹 Terminating existing processes on port 8082\t${NC}"
EXISTING_PID=$(lsof -t -i :8082 2>/dev/null)
if [ ! -z "$EXISTING_PID" ]; then
    kill -9 "$EXISTING_PID" >/dev/null 2>&1
    sleep 1
fi
echo -e "${GREEN}Done${NC}"

# 2. Stop and prune Docker containers
echo -ne "${YELLOW}[2/6] 🐳 Stopping existing MongoDB containers and pruning volumes\t${NC}"
docker compose down -v >/dev/null 2>&1
docker volume prune -f >/dev/null 2>&1
echo -e "${GREEN}Done${NC}"

# 3. Start fresh MongoDB container
echo -ne "${YELLOW}[3/6] 🐳 Starting fresh MongoDB container\t\t\t${NC}"
docker compose up -d >/dev/null 2>&1
if [ $? -ne 0 ]; then
    echo -e "\n${RED}❌ Error: Failed to start MongoDB container${NC}"
    exit 1
fi
echo -e "${GREEN}Done${NC}"

# 4. Boot Spring Boot Application in the background
echo -ne "${YELLOW}[4/6] ☕ Booting Spring Boot Application via bootRun\t\t${NC}"
./gradlew bootRun -Dspring.devtools.restart.enabled=false --no-daemon > build/bootRun.log 2>&1 &
BOOT_PID=$!
echo -e "${GREEN}Started (PID: $BOOT_PID)${NC}"

# Register cleanup function to run on script exit/interruption
cleanup() {
    echo -e "\n${YELLOW}🧹 Commencing Teardown of Test Environment${NC}"
    if ps -p "$BOOT_PID" > /dev/null; then
        echo -e "${YELLOW}   Stopping Spring Boot Application${NC}"
        kill "$BOOT_PID" >/dev/null 2>&1
        sleep 2
    fi
    echo -e "${YELLOW}   Shutting down MongoDB and removing volumes${NC}"
    docker compose down -v >/dev/null 2>&1
    echo -e "${GREEN}✨ Teardown complete. Goodbye!${NC}\n"
}
trap cleanup EXIT

# 5. Wait for Spring Boot to be fully ready
echo -e "${YELLOW}[5/6] ⏳ Polling health check endpoint until API is online${NC}"
TIMEOUT=60
ELAPSED=0
STARTED=false

while [ $ELAPSED -lt $TIMEOUT ]; do
    if ! ps -p "$BOOT_PID" > /dev/null; then
        echo -e "\n${RED}❌ Error: Spring Boot application terminated unexpectedly${NC}"
        echo -e "${RED}Check build/bootRun.log for details${NC}"
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
    echo -e "\n${RED}❌ Error: Application failed to start within $TIMEOUT seconds${NC}"
    echo -e "${RED}Check build/bootRun.log for details${NC}"
    exit 1
fi

echo -e "      ${GREEN}✓ API is Online and accepting requests!${NC}\n"

# Execute Bruno Integration Tests
./bruno/run/run-bruno-tests-only.sh "$@"
TEST_EXIT_CODE=$?

exit $TEST_EXIT_CODE
