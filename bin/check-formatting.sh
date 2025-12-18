#!/bin/bash

set -e

BOOK_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../book" && pwd)"
cd "$BOOK_DIR"

echo "Validating code block formatting..."

errors=0
warnings=0

for file in *.adoc; do
    echo "Checking $file..."
    
    # Use awk for fast processing
    result=$(awk '
    BEGIN { 
        expecting_dash = 0
        expecting_close_dash = 0
        errors = 0
        warnings = 0
    }
    
    /^\[source,/ {
        if (expecting_close_dash) {
            print "ERROR: Found [source,...] at line " NR " but previous code block not closed"
            errors++
            exit
        }
        expecting_dash = 1
        next
    }
    
    /^----/ {
        if ($0 != "----") {
            print "WARNING: ---- has trailing whitespace at line " NR
            warnings++
        }
        
        if (expecting_dash) {
            expecting_dash = 0
            expecting_close_dash = 1
        } else if (expecting_close_dash) {
            expecting_close_dash = 0
        } else {
            print "WARNING: Unexpected ---- at line " NR " (no preceding [source,...])"
            errors++
            exit
        }
        next
    }
    
    {
        if (expecting_dash && NF > 0) {
            print "ERROR: [source,...] not immediately followed by ---- at line " NR
            print "    Found: " $0
            errors++
            exit
        }
    }
    
    END {
        if (expecting_close_dash) {
            print "ERROR: File ends with unclosed code block"
            errors++
        } else if (expecting_dash) {
            print "ERROR: File ends with [source,...] not followed by ----"
            errors++
        }
        print "SUMMARY:" errors ":" warnings
    }
    ' "$file")
    
    # Parse awk output
    file_errors=$(echo "$result" | grep "^ERROR:" | wc -l)
    file_warnings=$(echo "$result" | grep "^WARNING:" | wc -l)
    
    if [ "$file_errors" -gt 0 ] || [ "$file_warnings" -gt 0 ]; then
        echo "$result" | grep -v "^SUMMARY:"
    fi
    
    errors=$((errors + file_errors))
    warnings=$((warnings + file_warnings))
done

if [ $warnings -gt 0 ]; then
    echo "Found $warnings whitespace warning(s)"
fi

if [ $errors -gt 0 ]; then
    echo "ERROR: Found $errors formatting error(s)"
    exit 1
fi

echo "✓ All code block formatting is valid"
