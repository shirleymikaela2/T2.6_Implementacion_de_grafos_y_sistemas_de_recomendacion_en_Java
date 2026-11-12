#!/usr/bin/env bash
set -euo pipefail
mkdir -p bin
find src -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 --release 17 -d bin
echo "Compilación correcta. Ejecute: java -cp bin app.Main"
