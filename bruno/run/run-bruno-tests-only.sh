#!/bin/bash
# Bruno CLI Executor for tragepro-api

GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
BOLD='\033[1m'
NC='\033[0m' # No Color

export PATH="/opt/homebrew/bin:/usr/local/bin:$PATH"

if ! command -v npx &> /dev/null; then
    echo -e "${RED}❌ Error: npx/node is not installed or not in PATH${NC}"
    exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/.." || exit 1

REPORTS_DIR="$SCRIPT_DIR/reports"
mkdir -p "$REPORTS_DIR"

REPORTER_FLAGS=""
FORMAT=""
OUTPUT_FILE=""

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
            echo -e "${RED}❌ Unknown option: $1${NC}"
            echo "Usage: ./run-bruno-tests.sh [--html | --json | --junit]"
            exit 1 
            ;;
    esac
done

echo -e "${YELLOW}[6/6] 🧪 Executing Bruno Integration Tests suite${NC}\n"

# Capture the raw CLI output
npx --yes @usebruno/cli run integration -r --env local $REPORTER_FLAGS > "$REPORTS_DIR/bruno-stdout.log"
EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    # On success, just show the summary table
    sed -n '/📊 Execution Summary/,$p' "$REPORTS_DIR/bruno-stdout.log"
    echo -e "\n${BOLD}=============================================${NC}"
    echo -e "${GREEN}✅ All Bruno Integration Tests Passed Successfully!${NC}"
else
    # On failure, dump the full log so errors can be debugged
    cat "$REPORTS_DIR/bruno-stdout.log"
    echo -e "\n${BOLD}=============================================${NC}"
    echo -e "${RED}❌ Some Bruno Integration Tests Failed${NC}"
fi

if [ ! -z "$FORMAT" ]; then
    echo -e "${BLUE}📝 Report saved to: $OUTPUT_FILE (${FORMAT} format)${NC}"
fi
echo -e "${BOLD}=============================================${NC}\n"

exit $EXIT_CODE
