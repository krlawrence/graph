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

REPORT="$(mktemp)"
python3 -m http.server "$PORT" --directory "$SERVE_DIR" >/tmp/afdocs-httpd.log 2>&1 &
HTTPD_PID=$!
cleanup() {
    kill "$HTTPD_PID" >/dev/null 2>&1 || true
    rm -rf "$SERVE_DIR" "$REPORT"
}
trap cleanup EXIT
sleep 1.5

# Scope validation to the checks that apply to a statically hosted llms.txt +
# Markdown mirror. afdocs' remaining checks (content-negotiation, per-page HTML
# discovery directive, .md URL support) assume the spec's canonical serving model
# -- HTML page URLs that also yield Markdown -- which a single-HTML book on
# GitHub Pages cannot satisfy (static hosting has no content negotiation). afdocs
# is version-pinned so CI results stay deterministic as the tool evolves.
AFDOCS="afdocs@0.20.0"
CHECKS="llms-txt-exists,llms-txt-valid,llms-txt-size,llms-txt-links-resolve,llms-txt-links-markdown"

echo "=== Running afdocs check against http://127.0.0.1:${PORT}/llms.txt ==="
# Stream output live (so failures are visible in CI) while capturing afdocs'
# exit status; a bare command substitution here would trip `set -e` and hide it.
set +e
npx --yes "$AFDOCS" check "http://127.0.0.1:${PORT}/llms.txt" \
    --max-links 500 --max-concurrency 4 --request-delay 30 --checks "$CHECKS" 2>&1 | tee "$REPORT"
STATUS=${PIPESTATUS[0]}
set -e

# Fail on a non-zero afdocs exit or any failed (✗) check.
if [ "$STATUS" -ne 0 ] || grep -q '✗' "$REPORT"; then
    echo "" >&2
    echo "✗ afdocs validation failed." >&2
    exit 1
fi

echo ""
echo "✓ afdocs validation passed."
