package piwords;
import static org.junit.Assert.*;
import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
        assertEquals(-1, PiGenerator.powerMod(-1, 7, 23));
        assertEquals(-1, PiGenerator.powerMod(5, -1, 23));
        assertEquals(-1, PiGenerator.powerMod(5, 7, -1));
        assertEquals(0, PiGenerator.powerMod(0, 7, 23));
        assertEquals(1, PiGenerator.powerMod(5, 0, 23));
        assertEquals(0, PiGenerator.powerMod(23, 8, 23));
    }

    public void computePiInHexTest() {
        // 5^7 mod 23 = 17
        assertEquals(null, PiGenerator.computePiInHex(-1));
        assertEquals(null, PiGenerator.computePiInHex(0));
        assertEquals(2, PiGenerator.computePiInHex(1));
        assertArrayEquals(new int[]{2}, PiGenerator.computePiInHex(1));
        assertArrayEquals(new int[]{2,4,3,16,6,10,8,8,8,5,10,3,0,8,13,3,1,3,1,9}, PiGenerator.computePiInHex(20));
    }
}
