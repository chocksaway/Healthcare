#!/usr/bin/env python3
"""
Simple linter for Thymeleaf templates.

Checks for:
 - Java-style getter calls inside Thymeleaf expressions (e.g. p.getGender())
 - Multiple ${...} expressions inside a single attribute value (mixing ${} segments)

Usage: python3 scripts/lint_templates.py
Exits with code 1 if any issues are found.
"""
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TEMPLATES_DIR = ROOT / 'src' / 'main' / 'resources' / 'templates'

GETTER_RE = re.compile(r'\bget[A-Z][A-Za-z0-9_]*\s*\(')
ATTR_RE = re.compile(r'(th:\w+)\s*=\s*"([^"]*)"')
EXPR_RE = re.compile(r'\$\{[^}]*\}')

def report(items):
    for fn, line_no, col, msg in items:
        print(f"{fn}:{line_no}:{col}: {msg}")

def lint_file(path):
    issues = []
    text = path.read_text(encoding='utf-8')
    # Check for getter calls inside ${...}
    for m in re.finditer(r'\$\{([^}]*)\}', text):
        expr = m.group(1)
        if GETTER_RE.search(expr):
            # compute line/col
            start = m.start()
            line_no = text.count('\n', 0, start) + 1
            col = start - text.rfind('\n', 0, start)
            issues.append((str(path), line_no, col, f"Use of Java getter inside expression: '{{{expr}}}'"))

    # Check for attributes that contain multiple ${...} occurrences
    for am in ATTR_RE.finditer(text):
        attr_name = am.group(1)
        attr_value = am.group(2)
        exprs = EXPR_RE.findall(attr_value)
        if len(exprs) > 1:
            start = am.start(2)
            line_no = text.count('\n', 0, start) + 1
            col = start - text.rfind('\n', 0, start)
            issues.append((str(path), line_no, col, f"Attribute {attr_name} contains multiple ${{}} expressions: {exprs}"))

    return issues


def main():
    if not TEMPLATES_DIR.exists():
        print(f"Templates directory not found: {TEMPLATES_DIR}")
        sys.exit(0)

    all_issues = []
    for p in sorted(TEMPLATES_DIR.rglob('*.html')):
        all_issues.extend(lint_file(p))

    if all_issues:
        print("\nThymeleaf linter found issues:")
        report(all_issues)
        sys.exit(1)
    else:
        print("No Thymeleaf linter issues found.")
        sys.exit(0)

if __name__ == '__main__':
    main()

