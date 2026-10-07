#!/usr/bin/env bash
# Monta o .zip de entrega do Google Classroom com o que o enunciado pede:
#   i)   projeto Java/Eclipse (pom.xml, src/, .project, .classpath, .settings/)
#   ii)  relatórios do EclEmma e do Baduíno   -> docs/reports/
#   iii) screenshots da view PIT Summary       -> docs/reports/
#   iv)  relatório final exportado do Drive    -> docs/relatorio-final.pdf
#
# Uso: scripts/empacotar-entrega.sh parte-2   (gera entrega/DCC168-JogoDaVelha-parte-2.zip)

set -euo pipefail

if [ $# -ne 1 ]; then
    echo "Uso: $0 <nome-da-entrega>   ex.: $0 parte-2" >&2
    exit 1
fi

cd "$(dirname "$0")/.."

for required in .project .classpath; do
    if [ ! -f "$required" ]; then
        echo "Falta $required: importe o projeto no Eclipse antes de empacotar." >&2
        exit 1
    fi
done

if [ -z "$(find docs/reports -mindepth 1 -maxdepth 1 ! -name generated ! -name linha-de-comando ! -name .gitkeep)" ]; then
    echo "Aviso: docs/reports/ não tem exportações do EclEmma, do Baduíno nem do PIT Summary."
fi
if [ ! -f docs/relatorio-final.pdf ]; then
    echo "Aviso: docs/relatorio-final.pdf não existe (exporte o relatório do Google Drive)."
fi

mkdir -p entrega
zip_file="entrega/DCC168-JogoDaVelha-$1.zip"
rm -f "$zip_file"

zip -rq "$zip_file" \
    pom.xml README.md .editorconfig .gitattributes .gitignore \
    .project .classpath .settings \
    src docs

echo "Gerado: $zip_file ($(du -h "$zip_file" | cut -f1))"
