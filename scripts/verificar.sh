#!/usr/bin/env sh
set -eu
ROOT="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
BUILD="$ROOT/build-verificacion"
rm -rf "$BUILD"
mkdir -p "$BUILD"
find "$ROOT/src/main/java" -name '*.java' -print > "$BUILD/fuentes.txt"
javac -encoding UTF-8 -d "$BUILD" @"$BUILD/fuentes.txt"
javac -encoding UTF-8 -cp "$BUILD" -d "$BUILD" "$ROOT/scripts/VerificacionDia6.java"
java -Djava.awt.headless=true -cp "$BUILD" gestor.VerificacionDia6
java -Djava.awt.headless=true -cp "$BUILD" gestor.Main
