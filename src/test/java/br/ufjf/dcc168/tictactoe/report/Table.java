package br.ufjf.dcc168.tictactoe.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    private final String title;
    private final List<String> headers;
    private final List<List<String>> rows = new ArrayList<>();

    public Table(String title, String... headers) {
        this.title = title;
        this.headers = Arrays.asList(headers);
    }

    public void addRow(String... cells) {
        if (cells.length != headers.size()) {
            throw new IllegalArgumentException(
                    "Expected " + headers.size() + " cells but got " + cells.length);
        }
        rows.add(Arrays.asList(cells));
    }

    public String getTitle() {
        return title;
    }

    public int getRowCount() {
        return rows.size();
    }

    public String toMarkdown() {
        StringBuilder markdown = new StringBuilder();
        markdown.append("**").append(title).append("**\n\n");
        markdown.append(markdownRow(headers));
        markdown.append(markdownSeparator());
        for (List<String> row : rows) {
            markdown.append(markdownRow(row));
        }
        return markdown.toString();
    }

    private String markdownRow(List<String> cells) {
        StringBuilder line = new StringBuilder("|");
        for (String cell : cells) {
            line.append(' ').append(escapeMarkdown(cell)).append(" |");
        }
        return line.append('\n').toString();
    }

    private String markdownSeparator() {
        StringBuilder line = new StringBuilder("|");
        for (int i = 0; i < headers.size(); i++) {
            line.append("---|");
        }
        return line.append('\n').toString();
    }

    // "|" separa colunas; "<" sem escape vira tag HTML e a notação "<0, 0>" some na exibição.
    private static String escapeMarkdown(String text) {
        return text.replace("|", "\\|").replace("<", "\\<").replace("\n", " ");
    }
}
