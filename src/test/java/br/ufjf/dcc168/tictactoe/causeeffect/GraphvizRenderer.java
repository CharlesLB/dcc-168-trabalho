package br.ufjf.dcc168.tictactoe.causeeffect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Converte um arquivo .dot em imagem PNG chamando o programa "dot" do Graphviz.
 *
 * <p>O Graphviz precisa estar instalado e no PATH:
 *
 * <ul>
 *   <li>Windows: {@code winget install graphviz}
 *   <li>macOS: {@code brew install graphviz}
 *   <li>Linux: {@code sudo apt install graphviz}
 * </ul>
 */
public final class GraphvizRenderer {

    private GraphvizRenderer() {}

    /**
     * @return true se a imagem foi gerada; false se o Graphviz não está instalado
     * @throws IOException se o Graphviz rodou mas falhou (ex.: erro de sintaxe no .dot)
     */
    public static boolean renderPng(Path dotFile, Path pngFile) throws IOException {
        Process process;
        try {
            process =
                    new ProcessBuilder(
                                    "dot",
                                    "-Tpng",
                                    "-Gdpi=150",
                                    dotFile.toString(),
                                    "-o",
                                    pngFile.toString())
                            .redirectErrorStream(true)
                            .start();
        } catch (IOException graphvizNotFound) {
            return false;
        }

        String output = readAll(process.getInputStream());
        int exitCode = waitFor(process);
        if (exitCode != 0) {
            throw new IOException("Graphviz failed (exit " + exitCode + "): " + output);
        }
        return true;
    }

    private static String readAll(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = stream.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    private static int waitFor(Process process) throws IOException {
        try {
            return process.waitFor();
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while waiting for Graphviz", interrupted);
        }
    }
}
