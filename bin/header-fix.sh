#!/bin/bash

for f in book/*.adoc; do
  python3 bin/headers.py "$f" > "$f.tmp" && mv -- "$f.tmp" "$f"
  echo "Converted: $f"
done