#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Running all validation checks..."
echo ""

# Run cross-reference check
echo "=== Checking cross-references ==="
"$SCRIPT_DIR/check-refs.sh"
echo ""

# Run formatting check
echo "=== Checking code block formatting ==="
"$SCRIPT_DIR/check-formatting.sh"
echo ""

echo "✓ All validation checks passed"
