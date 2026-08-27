#!/bin/bash

set -e

# validate-llms.sh
# ----------------
# Validates the generated llms.txt + Markdown mirror against the Agent Friendly
# Documentation Spec using `npx afdocs check` (https://agentdocsspec.com/), the
# same validator TinkerPop uses. Because afdocs only resolves absolute http(s)
# links, this serves the built site locally and points afdocs at an llms.txt
# regenerated with matching absolute URLs.
#
# Assumes split-markdown.rb + generate-llms-txt.rb have already run (target/md
# and target/md/.llms-manifest.json exist). Exits non-zero if afdocs reports a
# failure, so it can gate CI. Usage: bin/validate-llms.sh [port]

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$REPO_ROOT"

PORT="${1:-8199}"
MD_DIR="target/md"
SERVE_DIR="target/llms-serve"

if ! command -v npx >/dev/null 2>&1; then
    echo "⚠️  npx (Node.js) not found; skipping afdocs validation."
    exit 0
fi
if ! command -v python3 >/dev/null 2>&1; then
    echo "⚠️  python3 not found; skipping afdocs validation."
    exit 0
fi
if [ ! -f "$MD_DIR/.llms-manifest.json" ]; then
    echo "ERROR: $MD_DIR not built. Run bin/llms/split-markdown.rb first." >&2
    exit 1
fi

# Stage a serving directory: the Markdown tree plus an llms.txt whose links are
# absolute against the local server.
rm -rf "$SERVE_DIR"
mkdir -p "$SERVE_DIR"
ln -s ../md "$SERVE_DIR/md"
ruby bin/llms/generate-llms-txt.rb --prefix "http://127.0.0.1:${PORT}/" --out "$SERVE_DIR/llms.txt" "$MD_DIR" >/dev/null

python3 -m http.server "$PORT" --directory "$SERVE_DIR" >/tmp/afdocs-httpd.log 2>&1 &
HTTPD_PID=$!
cleanup() {
    kill "$HTTPD_PID" >/dev/null 2>&1 || true
    rm -rf "$SERVE_DIR"
}
trap cleanup EXIT
sleep 1.5

echo "=== Running afdocs check against http://127.0.0.1:${PORT}/llms.txt ==="
OUTPUT="$(npx --yes afdocs check "http://127.0.0.1:${PORT}/llms.txt" \
    --max-links 500 --max-concurrency 4 --request-delay 30 2>&1)"
STATUS=$?
echo "$OUTPUT"

# Fail on a non-zero afdocs exit or any failed (✗) check.
if [ "$STATUS" -ne 0 ] || echo "$OUTPUT" | grep -q '✗'; then
    echo "" >&2
    echo "✗ afdocs validation failed." >&2
    exit 1
fi

echo ""
echo "✓ afdocs validation passed."
