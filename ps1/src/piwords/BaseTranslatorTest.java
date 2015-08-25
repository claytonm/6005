package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseTranslatorTest {
    @Test
    public void basicBaseTranslatorTest() {
        // Expect that .01 in base-2 is .25 in base-10
        // (0 * 1/2^1 + 1 * 1/2^2 = .25)
        int[] input = {0, 1};
        int[] expectedOutput = {2, 5};
        assertArrayEquals(expectedOutput,
                          BaseTranslator.convertBase(input, 2, 10, 2));

        int[] input1 = {-1, 1};
        int[] expectedOutput1 = null;
        assertArrayEquals(expectedOutput1,
                BaseTranslator.convertBase(input1, 2, 10, 2));

        int[] input2 = {1, -1};
        int[] expectedOutput2 = null;
        assertArrayEquals(expectedOutput2,
                BaseTranslator.convertBase(input2, 2, 10, 2));

        int[] input3 = {0, 11};
        int[] expectedOutput3 = null;
        assertArrayEquals(expectedOutput3,
                BaseTranslator.convertBase(input3, 2, 10, 2));

        int[] input4 = {0, 1};
        int[] expectedOutput4 = null;
        assertArrayEquals(expectedOutput4,
                BaseTranslator.convertBase(input4, 1, 10, 2));

        int[] input5 = {0, 1};
        int[] expectedOutput5 = null;
        assertArrayEquals(expectedOutput5,
                BaseTranslator.convertBase(input5, 2, 1, 2));

        int[] input6 = {0, 1};
        int[] expectedOutput6 = null;
        assertArrayEquals(expectedOutput6,
                BaseTranslator.convertBase(input6, 2, 10, 0));

        int[] input7 = {1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        int[] expectedOutput7 = {2, 4, 3, 15, 6};
        assertArrayEquals(expectedOutput7,
                BaseTranslator.convertBase(input7, 10, 16, 5));

        int[] input8 = {2, 4, 3, 15, 6};
        int[] expectedOutput8 = {1, 4, 1, 5, 9};
        assertArrayEquals(expectedOutput8,
                BaseTranslator.convertBase(input8, 16, 10, 5));
    }
}
