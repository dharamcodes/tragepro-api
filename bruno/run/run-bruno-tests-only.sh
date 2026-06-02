#!/bin/bash
# Bruno CLI Executor for tragepro-api

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Ensure PATH has Brew/Node binaries
export PATH="/opt/homebrew/bin:/usr/local/bin:$PATH"

if ! command -v npx &> /dev/null; then
    echo -e "${RED}Error: npx/node is not installed or not in PATH.${NC}"
    echo "Please install Node.js to run Bruno CLI tests."
    exit 1
fi

# Get the script directory and navigate to the bruno collection root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/.." || exit 1

# Setup reports directory
REPORTS_DIR="$SCRIPT_DIR/reports"
mkdir -p "$REPORTS_DIR"

# Defaults
REPORTER_FLAGS=""
FORMAT=""
OUTPUT_FILE=""

# Parse arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --html) 
            FORMAT="html"
            OUTPUT_FILE="$REPORTS_DIR/bruno-report.html"
            REPORTER_FLAGS="--reporter-html $OUTPUT_FILE"
            shift 
            ;;
        --json) 
            FORMAT="json"
            OUTPUT_FILE="$REPORTS_DIR/bruno-report.json"
            REPORTER_FLAGS="--reporter-json $OUTPUT_FILE"
            shift 
            ;;
        --junit) 
            FORMAT="junit"
            OUTPUT_FILE="$REPORTS_DIR/bruno-report.xml"
            REPORTER_FLAGS="--reporter-junit $OUTPUT_FILE"
            shift 
            ;;
        *) 
            echo -e "${RED}Unknown option: $1${NC}"
            echo "Usage: ./run-bruno-tests.sh [--html | --json | --junit]"
            exit 1 
            ;;
    esac
done

echo -e "${BLUE}=============================================${NC}"
echo -e "${BLUE}       Running Bruno Integration Tests       ${NC}"
echo -e "${BLUE}=============================================${NC}"

# Execute the run command
npx --yes @usebruno/cli run integration -r --env local $REPORTER_FLAGS

# Capture exit code
EXIT_CODE=$?

echo -e "${BLUE}=============================================${NC}"
if [ $EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ All Bruno Integration Tests Passed Successfully!${NC}"
else
    echo -e "${RED}✗ Some Bruno Integration Tests Failed. Please check the summary above.${NC}"
fi

if [ ! -z "$FORMAT" ]; then
    echo -e "${YELLOW}Report saved to: $OUTPUT_FILE (${FORMAT} format)${NC}"
fi
echo -e "${BLUE}=============================================${NC}"

exit $EXIT_CODE
