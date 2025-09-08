#!/usr/bin/env bash
# Local lint runner for developers.
# By default this runs Checkstyle/PMD/SpotBugs without failing the build.
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")/.."

echo "Running local verify (non-failing for PMD/SpotBugs/Checkstyle)..."
mvn -DskipTests -Dspotbugs.fail=false -Dpmd.fail=false -Dcheckstyle.fail=false verify

echo "To run strict lint (CI-like), use: mvn -Pstrict-lint -DskipTests verify"
