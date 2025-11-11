#!/usr/bin/env python3

import re
import sys
from typing import Optional, Tuple

HeaderSpec = Tuple[re.Pattern, int, str]

# Header underline patterns: char run → ATX level
HEADER_SPECS: Tuple[HeaderSpec, ...] = (
    (re.compile(r"^=+$"), 1, "="),
    (re.compile(r"^-+$"), 2, "-"),
    (re.compile(r"^~+$"), 3, "~"),
    (re.compile(r"^\^+$"), 4, "^"),
    (re.compile(r"^\++$"), 5, "+"),
)

# Recognize delimited block fences (listing, literal, example, quote, sidebar, passthrough, comment)
# Asciidoctor recommends 4 or more of the same char; tables use a special "|===" fence.
FENCE_CHARS = "-.=_*+/"  # do NOT include '|' here (table handled separately)
FENCE_RE = re.compile(rf"^([{re.escape(FENCE_CHARS)}])\1{{3,}}$")
TABLE_FENCE_RE = re.compile(r"^\|={3,}$")  # |===, |====, ...

ATTR_LINE_RE = re.compile(r"^\[[^\]]*\]$")  # e.g., [source,groovy], [cols="3*^,1*<"]


def is_attr_or_block_title(line: str) -> bool:
    """Lines that typically precede a fence opening: attribute lines ( [..] ), or block titles starting with '.' ."""
    if not line:
        return False
    s = line.strip()
    return ATTR_LINE_RE.match(s) is not None or s.startswith(".")


def is_opening_fence(prev_nonblank: Optional[str], line: str) -> Optional[str]:
    """Return a token if 'line' is likely an opening fence given the previous non-blank context."""
    if not line:
        return None
    if TABLE_FENCE_RE.fullmatch(line):
        # Tables usually start with attributes or a table row/title immediately above.
        return "|="
    m = FENCE_RE.fullmatch(line)
    if not m:
        return None
    ch = m.group(1)
    # A fence is likely an opening fence if the previous non-blank line is absent, blank,
    # an attribute line, or a block title (".Title"). This prevents misclassifying header underlines
    # that sit directly under a real title line.
    if prev_nonblank is None or prev_nonblank.strip() == "" or is_attr_or_block_title(prev_nonblank):
        return ch
    return None


def is_closing_fence(current_block: Optional[str], line: str) -> bool:
    if not line:
        return False
    if current_block == "|=":
        return TABLE_FENCE_RE.fullmatch(line) is not None
    if current_block:
        return re.fullmatch(rf"{re.escape(current_block)}{{4,}}", line) is not None
    return False


def looks_like_title_line(line: str) -> bool:
    if not line:
        return False
    # Must not be indented (section titles in setext form are usually flush left)
    if line[0].isspace():
        return False
    s = line.strip()
    if s == "":
        return False
    # Not an attribute or block title line
    if is_attr_or_block_title(line):
        return False
    # Avoid false positives on lines that are themselves fence-like
    if FENCE_RE.fullmatch(s) or TABLE_FENCE_RE.fullmatch(s):
        return False
    return True


def convert_file(path: str) -> int:
    try:
        with open(path, "r", encoding="utf-8", newline="") as f:
            lines = [ln.rstrip("\r\n") for ln in f]
    except FileNotFoundError:
        print(f"error: file not found: {path}", file=sys.stderr)
        return 2

    out = []
    in_block: Optional[str] = None  # token for the current block (e.g., '-', '.', '|=', etc.)

    # Helper to find the previous non-blank line content (for opening-fence heuristics)
    def prev_nonblank(idx: int) -> Optional[str]:
        j = idx - 1
        while j >= 0:
            if lines[j].strip() != "":
                return lines[j]
            j -= 1
        return None

    i = 0
    n = len(lines)
    while i < n:
        prev_line = lines[i - 1] if i - 1 >= 0 else None
        curr_line = lines[i]
        next_line = lines[i + 1] if i + 1 < n else None

        # Update block state: closing fence?
        if in_block and is_closing_fence(in_block, curr_line):
            out.append(curr_line)
            in_block = None
            i += 1
            continue

        # Opening fence?
        if not in_block:
            token = is_opening_fence(prev_nonblank(i), curr_line)
            if token is not None:
                in_block = token
                out.append(curr_line)
                i += 1
                continue

        # If in a block, pass through unchanged
        if in_block:
            out.append(curr_line)
            i += 1
            continue

        # Outside blocks: attempt heading conversion when we have prev+curr
        if prev_line is not None and looks_like_title_line(prev_line):
            for rx, level, ch in HEADER_SPECS:
                if rx.fullmatch(curr_line):
                    # Heuristics to avoid common false positives:
                    # - For '-' underlines, avoid exactly four hyphens (typical listing fence)
                    if ch == '-' and len(curr_line) == 4:
                        break
                    # - Require underline length to be >= title length
                    if len(curr_line) < len(prev_line):
                        break
                    # - Prefer a blank/EOF after the underline (common in prose)
                    if next_line is not None and next_line.strip() != "":
                        # If next line is non-blank, this is likely not a title underline
                        break
                    # Passed checks: emit ATX header, skip printing prev_line and underline
                    out[-1:] = []  # remove previously appended prev_line if any (we haven't appended it yet)
                    # Replace the previous line in output if present, else ensure we didn't emit it earlier
                    if len(out) > 0 and out[-1] == prev_line:
                        out.pop()
                    out.append("{} {}\n".format("=" * level, prev_line))
                    i += 1  # Skip current underline line; prev_line was consumed by replacement
                    break
            else:
                # No header match; just emit current line
                out.append(curr_line)
                i += 1
                continue
            # We matched some header pattern and handled advancing 'i' inside the loop
            i += 1
            continue

        # Default passthrough
        out.append(curr_line)
        i += 1

    # Write output with \n
    sys.stdout.write("\n".join(out) + "\n")
    return 0


def main(argv):
    if len(argv) != 2:
        print(f"usage: {argv[0]} <input.adoc>", file=sys.stderr)
        return 2
    return convert_file(argv[1])

if __name__ == "__main__":
    raise SystemExit(main(sys.argv))