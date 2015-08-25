package piwords;
import static org.junit.Assert.*;

import org.junit.Test;

public class DigitsToStringConverterTest {
    @Test
    public void basicNumberSerializerTest() {
        // Input is a 4 digit number, 0.123 represented in base 4
        int[] input = {0, 1, 2, 3};

        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet = {'d', 'c', 'b', 'a'};

        String expectedOutput = "dcba";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));


        int[] input1 = {0, 1, 2, 3};
        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet1 = {'d', 'c', 'b'};

        String expectedOutput1 = null;
        assertEquals(expectedOutput1,
                DigitsToStringConverter.convertDigitsToString(
                        input1, 4, alphabet1));


        int[] input2 = {0, 1, 2, 5};
        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet2 = {'d', 'c', 'b', 'd'};

        String expectedOutput2 = null;
        assertEquals(expectedOutput2,
                DigitsToStringConverter.convertDigitsToString(
                        input2, 4, alphabet2));


    }

}
