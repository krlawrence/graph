#!/bin/bash

set -e

# Parse command line arguments
CHECK_URLS=false
while [[ $# -gt 0 ]]; do
    case $1 in
        --check-urls)
            CHECK_URLS=true
            shift
            ;;
        *)
            echo "Unknown option: $1"
            echo "Usage: $0 [--check-urls]"
            exit 1
            ;;
    esac
done

BOOK_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../book" && pwd)"
cd "$BOOK_DIR"

echo "Checking for orphaned cross-references..."

# Extract all cross-references (handle filename:match format)
refs=$(grep -o "<<[^>]*>>" *.adoc | cut -d: -f2- | sed 's/<<//g; s/>>//g' | sort -u)

# Extract all anchor definitions (handle filename:match format)
anchors=$(grep -o "\[\[[^]]*\]\]" *.adoc | cut -d: -f2- | sed 's/\[\[//g; s/\]\]//g' | sort -u)

# Find orphaned references with their files
orphaned=()
while IFS= read -r ref; do
  if ! echo "$anchors" | grep -qx "$ref"; then
    # Find which files contain this orphaned reference
    files=$(grep -l "<<$ref>>" *.adoc | paste -sd' ')
    orphaned+=("$ref (referenced in: $files)")
  fi
done <<< "$refs"

if [ ${#orphaned[@]} -gt 0 ]; then
  echo "ERROR: Found ${#orphaned[@]} orphaned cross-reference(s):"
  printf '  - %s\n' "${orphaned[@]}"
  exit 1
fi

echo "✓ No orphaned cross-references found"

# URL validation (optional)
if [ "$CHECK_URLS" = true ]; then
    echo ""
    echo "Checking URLs..."
    
    # Extract URLs from AsciiDoc files (both bare URLs and link syntax)
    # Exclude localhost URLs as they're examples/demos
    # Remove common trailing punctuation that's part of sentences
    # Exclude XML namespace URLs that aren't retrievable resources
    urls=$(grep -oh "https\?://[^][ ]*" *.adoc | sed 's/\[.*$//' | sed "s/[.,;:!?)']*$//" | grep -v "localhost" | grep -v "http://graphml.graphdrawing.org/xmlns" | sort -u)
    
    if [ -n "$urls" ]; then
        warnings=0
        while IFS= read -r url; do
            # Find which files and line numbers contain this URL
            locations=$(grep -n "$url" *.adoc | cut -d: -f1,2 | tr '\n' ' ')
            
            if command -v curl >/dev/null 2>&1; then
                response=$(curl -s -I --max-time 10 --user-agent "Book-Validator/1.0" "$url" 2>/dev/null | head -1)
                if echo "$response" | grep -q "200\|301\|302"; then
                    echo "  ✓ $url (at: $locations)"
                else
                    echo "  ⚠️  $url (at: $locations) - $(echo "$response" | cut -d' ' -f2- || echo "Connection failed")"
                    warnings=$((warnings + 1))
                fi
            else
                echo "  ? $url (at: $locations) - curl not available"
            fi
        done <<< "$urls"
        
        if [ $warnings -gt 0 ]; then
            echo "Found $warnings URL warning(s)"
        else
            echo "✓ All URLs are accessible"
        fi
    else
        echo "No URLs found to check"
    fi
fi
