#!/usr/bin/env bash
# Monta o .zip da entrega com o que o enunciado da Parte II pede:
#   i)  projeto Java/Eclipse com o código-fonte e os casos JUnit
#       (pom.xml, src/, .project, .classpath, .settings/)
#   ii) relatórios das ferramentas de cobertura de cada subfase
#       (docs/reports/parte-2a/ e docs/reports/parte-2b/)
# e o relatório da Parte II (docs/Relatorio-Parte-II-Teste-Estrutural.docx).
#
# Uso: scripts/empacotar-entrega.sh   (gera entrega/DCC168-JogoDaVelha-Parte-II.zip)

set -euo pipefail

cd "$(dirname "$0")/.."

for required in .project .classpath docs/reports/parte-2a docs/reports/parte-2b; do
    if [ ! -e "$required" ]; then
        echo "Falta $required." >&2
        exit 1
    fi
done

mkdir -p entrega
zip_file="entrega/DCC168-JogoDaVelha-Parte-II.zip"
rm -f "$zip_file"

zip -rq "$zip_file" \
    pom.xml README.md .editorconfig .gitattributes .gitignore \
    .project .classpath .settings \
    src scripts docs

echo "Gerado: $zip_file ($(du -h "$zip_file" | cut -f1))"
