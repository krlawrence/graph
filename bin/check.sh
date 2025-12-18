#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Parse command line arguments
CHECK_URLS_FLAG=""
while [[ $# -gt 0 ]]; do
    case $1 in
        --check-urls)
            CHECK_URLS_FLAG="--check-urls"
            shift
            ;;
        *)
            echo "Unknown option: $1"
            echo "Usage: $0 [--check-urls]"
            exit 1
            ;;
    esac
done

echo "Running all validation checks..."
echo ""

# Run cross-reference check (with optional URL checking)
echo "=== Checking cross-references ==="
"$SCRIPT_DIR/check-refs.sh" $CHECK_URLS_FLAG
echo ""

# Run formatting check
echo "=== Checking code block formatting ==="
"$SCRIPT_DIR/check-formatting.sh"
echo ""

echo "✓ All validation checks passed"
