#!/bin/bash

set -e

BOOK_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../book" && pwd)"
cd "$BOOK_DIR"

echo "Checking for orphaned cross-references..."

# Extract all cross-references with filenames
refs_with_files=$(grep -roh "<<[^>]*>>" *.adoc | sed 's/<<//g; s/>>//g')
refs=$(echo "$refs_with_files" | sort -u)

# Extract all anchor definitions
anchors=$(grep -roh "\[\[[^]]*\]\]" *.adoc | sed 's/\[\[//g; s/\]\]//g' | sort -u)

# Find orphaned references with their files
orphaned=()
while IFS= read -r ref; do
  if ! echo "$anchors" | grep -qx "$ref"; then
    # Find which files contain this orphaned reference
    files=$(grep -l "<<$ref>>" *.adoc | tr '\n' ' ')
    orphaned+=("$ref (in: $files)")
  fi
done <<< "$refs"

if [ ${#orphaned[@]} -gt 0 ]; then
  echo "ERROR: Found ${#orphaned[@]} orphaned cross-reference(s):"
  printf '  - %s\n' "${orphaned[@]}"
  exit 1
fi

echo "✓ No orphaned cross-references found"
