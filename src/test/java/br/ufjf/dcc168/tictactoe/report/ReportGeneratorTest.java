package br.ufjf.dcc168.tictactoe.report;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class ReportGeneratorTest {

    @Test
    public void joinWithAnd_withOneItem_returnsTheItem() {
        assertEquals(
                "vazia (V4)", ReportGenerator.joinWithAnd(Collections.singletonList("vazia (V4)")));
    }

    @Test
    public void joinWithAnd_withTwoItems_joinsWithAnd() {
        assertEquals(
                "a (I1) e b (I2)", ReportGenerator.joinWithAnd(Arrays.asList("a (I1)", "b (I2)")));
    }

    @Test
    public void joinWithAnd_withThreeItems_usesCommasAndAndBeforeTheLast() {
        assertEquals(
                "a (V6), b (V7) e c (V8)",
                ReportGenerator.joinWithAnd(Arrays.asList("a (V6)", "b (V7)", "c (V8)")));
    }
}
