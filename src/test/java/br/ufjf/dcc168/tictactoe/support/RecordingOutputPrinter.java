package br.ufjf.dcc168.tictactoe.support;

import br.ufjf.dcc168.tictactoe.io.OutputPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Printer de teste: guarda as linhas escritas para serem verificadas nas asserções. */
public class RecordingOutputPrinter implements OutputPrinter {

    private final List<String> lines = new ArrayList<>();

    @Override
    public void printLine(String text) {
        lines.add(text);
    }

    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public String getLastLine() {
        return lines.isEmpty() ? null : lines.get(lines.size() - 1);
    }

    /** Indica se alguma linha impressa contém o trecho informado. */
    public boolean hasLineContaining(String fragment) {
        for (String line : lines) {
            if (line.contains(fragment)) {
                return true;
            }
        }
        return false;
    }
}
