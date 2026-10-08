package br.ufjf.dcc168.tictactoe.report;

import br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph;
import br.ufjf.dcc168.tictactoe.causeeffect.DecisionRule;
import br.ufjf.dcc168.tictactoe.causeeffect.DecisionTable;
import br.ufjf.dcc168.tictactoe.causeeffect.DotExporter;
import br.ufjf.dcc168.tictactoe.causeeffect.GraphvizRenderer;
import br.ufjf.dcc168.tictactoe.causeeffect.Node;
import br.ufjf.dcc168.tictactoe.functional.ConsoleFunctionalTest;
import br.ufjf.dcc168.tictactoe.functional.GameFunctionalTest;
import br.ufjf.dcc168.tictactoe.specification.CauseEffectSpecification;
import br.ufjf.dcc168.tictactoe.specification.EquivalenceClass;
import br.ufjf.dcc168.tictactoe.specification.InputCondition;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gera os artefatos do relatório a partir do código de teste.
 *
 * <ul>
 *   <li>Tabela 1 (classes de equivalência): lida do enum {@link EquivalenceClass}.
 *   <li>Grafo de causa-efeito (.dot e .png) e tabela de decisão: lidos de {@link
 *       CauseEffectSpecification}.
 *   <li>Tabela 2 (casos de teste): lida das anotações {@link TestCase}; a coluna "Saída Obtida" vem
 *       da execução real dos testes.
 * </ul>
 *
 * <p>Saída em docs/reports/generated/. O relatorio.md junta tudo, com a imagem do grafo.
 *
 * <p>Como rodar: botão direito nesta classe → Run as Java Application, ou {@code mvn test-compile
 * exec:java@report}.
 */
public final class ReportGenerator {

    private static final Path DEFAULT_OUTPUT_DIRECTORY = Paths.get("docs", "reports", "generated");

    private static final String GRAPH_FILE_NAME = "grafo-causa-efeito";

    private static final String TEST_SET_NAME = "TestSet-Func";

    private static final Class<?>[] TEST_CLASSES = {
        GameFunctionalTest.class, ConsoleFunctionalTest.class
    };

    private final Path outputDirectory;
    private final List<String> generatedFiles = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    private ReportGenerator(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public static void main(String[] args) throws IOException {
        generate(DEFAULT_OUTPUT_DIRECTORY);
    }

    public static void generate(Path outputDirectory) throws IOException {
        new ReportGenerator(outputDirectory).generateAll();
    }

    private void generateAll() throws IOException {
        Files.createDirectories(outputDirectory);

        Table equivalenceClassTable = buildEquivalenceClassTable();
        write("tabela-1-classes-equivalencia.md", equivalenceClassTable.toMarkdown());

        CauseEffectGraph graph = CauseEffectSpecification.build();
        boolean graphImageCreated = writeGraph(graph);
        Table decisionTable = buildDecisionTable(DecisionTable.from(graph));
        write("tabela-decisao-causa-efeito.md", decisionTable.toMarkdown());

        List<Method> testCaseMethods = annotatedTestMethodsSortedById();
        Table testCaseTable = buildTestCaseTable(testCaseMethods);
        write("tabela-2-testset-func.md", testCaseTable.toMarkdown());

        write(
                "relatorio.md",
                buildFullReport(
                        equivalenceClassTable, graphImageCreated, decisionTable, testCaseTable));

        checkUniqueIds(testCaseMethods);
        checkEveryClassIsExercisedBy(testCaseMethods);
        printSummary();
    }

    // ---------------------------------------------------------------- Tabela 1

    private Table buildEquivalenceClassTable() {
        Table table =
                new Table(
                        "Tabela 1 – Classes de Equivalência",
                        "Condição de Entrada",
                        "Classes de Equivalência Válidas",
                        "Classes de Equivalência Inválidas");

        for (InputCondition condition : InputCondition.values()) {
            table.addRow(
                    condition.getDescription(),
                    joinClasses(condition, true),
                    joinClasses(condition, false));
        }
        return table;
    }

    private String joinClasses(InputCondition condition, boolean valid) {
        List<String> classTexts =
                Arrays.stream(EquivalenceClass.values())
                        .filter(eqClass -> eqClass.getCondition() == condition)
                        .filter(eqClass -> eqClass.isValid() == valid)
                        .map(EquivalenceClass::toTableText)
                        .collect(Collectors.toList());
        return classTexts.isEmpty() ? "–" : joinWithAnd(classTexts);
    }

    /**
     * Junta os itens como na Tabela 1 da Parte I: vírgula entre eles e " e " antes do último. Ex.:
     * "a (V6), b (V7) e c (V8)".
     */
    static String joinWithAnd(List<String> items) {
        int lastIndex = items.size() - 1;
        if (lastIndex == 0) {
            return items.get(0);
        }
        return String.join(", ", items.subList(0, lastIndex)) + " e " + items.get(lastIndex);
    }

    // ---------------------------------------------------------------- Grafo de causa-efeito

    /** Escreve o .dot e tenta gerar o .png; retorna true se a imagem foi criada. */
    private boolean writeGraph(CauseEffectGraph graph) throws IOException {
        Path dotFile = write(GRAPH_FILE_NAME + ".dot", DotExporter.toDot(graph));
        Path pngFile = outputDirectory.resolve(GRAPH_FILE_NAME + ".png");

        boolean created = GraphvizRenderer.renderPng(dotFile, pngFile);
        if (created) {
            generatedFiles.add(pngFile.getFileName().toString());
        } else {
            warnings.add(
                    "Graphviz não encontrado: "
                            + GRAPH_FILE_NAME
                            + ".png não foi gerado. Instale o Graphviz (veja o README) e rode de"
                            + " novo.");
        }
        return created;
    }

    // Linhas = causas e efeitos; colunas = regras (R1, R2...). Cada regra vira um caso de teste.
    private Table buildDecisionTable(DecisionTable decisionTable) {
        List<DecisionRule> rules = decisionTable.getRules();

        String[] headers = new String[rules.size() + 1];
        headers[0] = "Causa / Efeito";
        for (int i = 0; i < rules.size(); i++) {
            headers[i + 1] = "R" + (i + 1);
        }
        Table table = new Table("Tabela de Decisão – Grafo de Causa-Efeito", headers);

        List<Node> causes = decisionTable.getCauses();
        for (int causeIndex = 0; causeIndex < causes.size(); causeIndex++) {
            String[] row = newRow(causes.get(causeIndex), rules.size());
            for (int i = 0; i < rules.size(); i++) {
                row[i + 1] = rules.get(i).valueOfCause(causeIndex).getSymbol();
            }
            table.addRow(row);
        }

        for (Node effect : decisionTable.getEffects()) {
            String[] row = newRow(effect, rules.size());
            for (int i = 0; i < rules.size(); i++) {
                row[i + 1] = rules.get(i).triggers(effect) ? "X" : "";
            }
            table.addRow(row);
        }
        return table;
    }

    // Primeira célula com a causa ou o efeito; as demais, uma por regra, ficam para o chamador.
    private static String[] newRow(Node node, int ruleCount) {
        String[] row = new String[ruleCount + 1];
        row[0] = describe(node);
        return row;
    }

    private static String describe(Node node) {
        return node.getId() + " – " + node.getDescription().replace("\n", " ");
    }

    // ---------------------------------------------------------------- Tabela 2

    private Table buildTestCaseTable(List<Method> testCaseMethods) {
        OutcomeCollector outcomes = runTests();

        Table table =
                new Table(
                        "Tabela 2 – Casos de Teste (" + TEST_SET_NAME + ")",
                        "ID",
                        "Condições de Entrada",
                        "Saída Esp.",
                        "Classes Eq. Exercitadas",
                        "Saída Obtida");

        for (Method method : testCaseMethods) {
            TestCase testCase = method.getAnnotation(TestCase.class);
            TestOutcome outcome = outcomes.outcomeOf(method.getDeclaringClass(), method.getName());
            table.addRow(
                    testCase.id(),
                    testCase.input(),
                    testCase.expected(),
                    joinClassNames(testCase.classes()),
                    describeObtainedOutput(testCase, outcome));
        }
        return table;
    }

    private OutcomeCollector runTests() {
        OutcomeCollector collector = new OutcomeCollector();
        JUnitCore junit = new JUnitCore();
        junit.addListener(collector);
        junit.run(TEST_CLASSES);
        return collector;
    }

    private List<Method> annotatedTestMethodsSortedById() {
        List<Method> annotated = new ArrayList<>();
        for (Class<?> testClass : TEST_CLASSES) {
            for (Method method : testClass.getMethods()) {
                if (method.isAnnotationPresent(TestCase.class)) {
                    annotated.add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    warnings.add(
                            TEST_SET_NAME
                                    + ": "
                                    + method.getName()
                                    + " não tem @TestCase e ficou fora da tabela");
                }
            }
        }
        annotated.sort(Comparator.comparingInt(ReportGenerator::idNumber));
        return annotated;
    }

    // Ordena pelo número do ID, para CT2 vir antes de CT10.
    private static int idNumber(Method method) {
        String digits = method.getAnnotation(TestCase.class).id().replaceAll("\\D", "");
        return digits.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(digits);
    }

    private static String joinClassNames(EquivalenceClass[] classes) {
        return Arrays.stream(classes).map(Enum::name).collect(Collectors.joining(", "));
    }

    // Se o teste passou, a saída obtida é igual à esperada (é isso que as asserções garantem).
    private static String describeObtainedOutput(TestCase testCase, TestOutcome outcome) {
        switch (outcome.getStatus()) {
            case PASSED:
                return testCase.expected();
            case FAILED:
                return "FALHOU – " + outcome.getFailureMessage();
            default:
                return "Não executado";
        }
    }

    // ---------------------------------------------------------------- Verificações

    private void checkUniqueIds(List<Method> testCaseMethods) {
        Set<String> seenIds = new HashSet<>();
        for (TestCase testCase : testCasesOf(testCaseMethods)) {
            if (!seenIds.add(testCase.id())) {
                warnings.add("ID duplicado: " + testCase.id());
            }
        }
    }

    // A Parte I exige que o conjunto funcional exercite todas as classes de equivalência.
    private void checkEveryClassIsExercisedBy(List<Method> testCaseMethods) {
        Set<EquivalenceClass> notExercised = EnumSet.allOf(EquivalenceClass.class);
        for (TestCase testCase : testCasesOf(testCaseMethods)) {
            notExercised.removeAll(Arrays.asList(testCase.classes()));
        }
        for (EquivalenceClass eqClass : notExercised) {
            warnings.add(TEST_SET_NAME + " não exercita a classe " + eqClass.toTableText());
        }
    }

    private static List<TestCase> testCasesOf(List<Method> testCaseMethods) {
        return testCaseMethods.stream()
                .map(method -> method.getAnnotation(TestCase.class))
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------- Saída

    private String buildFullReport(
            Table equivalenceClassTable,
            boolean graphImageCreated,
            Table decisionTable,
            Table testCaseTable) {
        StringBuilder report = new StringBuilder();
        report.append("# Artefatos gerados para o relatório\n\n");
        report.append(equivalenceClassTable.toMarkdown()).append('\n');

        report.append("**Grafo de Causa-Efeito**\n\n");
        if (graphImageCreated) {
            report.append("![Grafo de Causa-Efeito](").append(GRAPH_FILE_NAME).append(".png)\n\n");
        } else {
            report.append("_Imagem não gerada: Graphviz não instalado._\n\n");
        }
        report.append(decisionTable.toMarkdown()).append('\n');
        report.append(testCaseTable.toMarkdown()).append('\n');
        return report.toString();
    }

    private Path write(String fileName, String content) throws IOException {
        Path file = outputDirectory.resolve(fileName);
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        generatedFiles.add(fileName);
        return file;
    }

    private void printSummary() {
        System.out.println("Arquivos gerados em " + outputDirectory.toAbsolutePath() + ":");
        for (String fileName : generatedFiles) {
            System.out.println("  " + fileName);
        }
        if (!warnings.isEmpty()) {
            System.out.println();
            System.out.println("Avisos:");
            for (String warning : warnings) {
                System.out.println("  - " + warning);
            }
        }
    }
}
