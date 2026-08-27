#!/bin/bash

set -e

# make-llms.sh
# ------------
# Produces the agent-friendly documentation target: a split Markdown mirror of
# the book under target/md/ plus an llms.txt index at target/llms.txt, following
# the Agent Friendly Documentation Spec (the same publishing pattern used by
# Apache TinkerPop). Conversion is done with downdoc (npm); see bin/llms/.
#
# Options:
#   --site-url URL   Absolute site root (e.g. https://krlawrence.github.io/graph/).
#                    Makes llms.txt links and the per-page pointers absolute
#                    (used at publish time). Omit for local relative links.
#   --no-check       Skip bin/check.sh (used when the caller already ran it).
#   --no-validate    Skip the afdocs validation step.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$REPO_ROOT"

SITE_URL=""
RUN_CHECK=1
RUN_VALIDATE=1
while [[ $# -gt 0 ]]; do
    case $1 in
        --site-url) SITE_URL="$2"; shift 2 ;;
        --no-check) RUN_CHECK=0; shift ;;
        --no-validate) RUN_VALIDATE=0; shift ;;
        *) echo "Unknown option: $1"; echo "Usage: $0 [--site-url URL] [--no-check] [--no-validate]"; exit 1 ;;
    esac
done

if [ "$RUN_CHECK" -eq 1 ]; then
    echo "*** Validating AsciiDoc structure ***"
    ./bin/check.sh
fi

# downdoc is an npm package; install it globally if it is not already available.
if ! command -v downdoc >/dev/null 2>&1; then
    echo "*** Installing downdoc (npm) ***"
    npm install -g downdoc
fi

SITE_ARGS=()
PREFIX_ARGS=()
if [ -n "$SITE_URL" ]; then
    SITE_ARGS=(--site-url "$SITE_URL")
    PREFIX_ARGS=(--prefix "$SITE_URL")
fi

echo "*** Producing MARKDOWN pages ***"
ruby bin/llms/split-markdown.rb --strict "${SITE_ARGS[@]}"

echo "*** Producing LLMS.TXT ***"
ruby bin/llms/generate-llms-txt.rb "${PREFIX_ARGS[@]}" target/md

if [ "$RUN_VALIDATE" -eq 1 ]; then
    echo "*** Validating against the Agent Friendly Documentation Spec ***"
    ./bin/validate-llms.sh
fi
