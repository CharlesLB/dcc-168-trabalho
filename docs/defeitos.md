# Registro de defeitos

| ID | Parte | Caso que revelou | Sintoma (saída obtida) | Causa | Correção | Reteste |
|---|---|---|---|---|---|---|

## Parte II-A – TestSet-Func

Primeira execução do TestSet-Func (`mvn test -Dtest=FunctionalSuite`, em 07/10/2026):
**21 casos executados, 21 passaram, 0 falhas**. Nenhum defeito foi registrado.

### Observação

Na Fase 2, antes da implementação do domínio, o comando
`mvn test -Dtest='!*FunctionalTest'` executou o TestSet-Func sem querer, porque o filtro não
exclui as suites (`FunctionalSuite`, `FunctionalAndStructuralSuite`, `AllTestsSuite`), que rodam
os testes funcionais. As falhas dessa execução vieram dos esqueletos ainda não implementados
(`UnsupportedOperationException: TODO`). Falta de implementação não é defeito (SPEC, Fase 2), então
nada foi registrado. O filtro passou a ser `mvn test -Dtest='!*FunctionalTest,!*Suite,!*$*'`.
