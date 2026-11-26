#!/usr/bin/env bash
# Cobertura de fluxo de dados (critério todos-os-usos) com o BA-DUA, o motor do Baduíno.
#
# Uso: scripts/cobertura-fluxo-de-dados.sh <Suite> <pasta-de-saída>
#   ex.: scripts/cobertura-fluxo-de-dados.sh FunctionalSuite docs/reports/parte-2a/baduino
#
# O BA-DUA 0.8.0 é baixado do Maven Central para target/ba-dua (não é dependência do projeto).
# Usa instrumentação offline porque o modo agente (-javaagent) derruba o JDK 21+.

set -euo pipefail

if [ $# -ne 2 ]; then
    echo "Uso: $0 <Suite> <pasta-de-saída>" >&2
    exit 1
fi

suite="br.ufjf.dcc168.tictactoe.domain.suites.$1"
output_directory="$2"

cd "$(dirname "$0")/.."

badua_version="0.8.0"
badua_home="target/ba-dua"
badua_cli="$badua_home/lib/ba-dua-cli-$badua_version-all.jar"
badua_runtime="$badua_home/lib/ba-dua-agent-rt-$badua_version-all.jar"

mvn -q test-compile dependency:build-classpath -Dmdep.outputFile=target/test-classpath.txt

if [ ! -f "$badua_cli" ]; then
    mkdir -p "$badua_home"
    curl -sSfL -o "$badua_home/ba-dua.zip" \
        "https://repo.maven.apache.org/maven2/br/usp/each/saeg/ba-dua/$badua_version/ba-dua-$badua_version.zip"
    unzip -q -o "$badua_home/ba-dua.zip" -d "$badua_home"
fi

rm -rf target/ba-dua-instrumented
java -jar "$badua_cli" instrument -src target/classes -dest target/ba-dua-instrumented

classpath="target/ba-dua-instrumented:target/test-classes:$badua_runtime:$(cat target/test-classpath.txt)"
java -Doutput.file=target/ba-dua-coverage.ser -cp "$classpath" org.junit.runner.JUnitCore "$suite"

mkdir -p "$output_directory"
java -jar "$badua_cli" report -input target/ba-dua-coverage.ser -classes target/classes \
    -show-classes -show-methods -xml "$output_directory/ba-dua.xml" \
    > "$output_directory/ba-dua.txt"

echo "Relatório do BA-DUA em $output_directory"
