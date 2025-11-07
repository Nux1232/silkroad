package silkRoad;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SilkRoadContestCTest {

    @Test
    public void testCasoComun1() {
        int[][] days = {
            {1, 20},
            {2, 15, 15},
            {2, 40, 50},
            {1, 50},
            {2, 80, 20},
            {2, 70, 30}
        };
        int[] expected = {0, 10, 35, 50, 50, 60};
        int[] result = SilkRoadContest.solve(days);
        assertArrayEquals(expected, result, "Caso Común 1 falló");
    }

    @Test
    public void testCasoComun2() {
        int[][] days = {
            {1, 10},
            {1, 20},
            {1, 30}
        };
        int[] result = SilkRoadContest.solve(days);
        int[] expected = {0, 0, 0};
        assertArrayEquals(expected, result, "Caso Común 2 falló");
    }

    @Test
    public void testCasoComun3() {
        int[][] days = {
            {2, 10, 100},
            {2, 20, 200},
            {2, 30, 300}
        };
        int[] result = SilkRoadContest.solve(days);
        int[] expected = {0, 0, 0};
        assertArrayEquals(expected, result, "Caso Común 3 falló");
    }

    @Test
    public void testCasoComun4() {
        int[][] days = {
            {1, 50},
            {2, 50, 100}
        };
        int[] expected = {0, 100};
        int[] result = SilkRoadContest.solve(days);
        assertArrayEquals(expected, result, "Caso Común 4 falló");
    }

    @Test
    public void testCasoComun5() {
        int[][] days = {
            {1, 0},
            {1, 100},
            {2, 10, 50},
            {2, 90, 50}
        };
        int[] expected = {0, 0, 40, 80};
        int[] result = SilkRoadContest.solve(days);
        assertArrayEquals(expected, result, "Caso Común 5 falló");
    }

    @Test
    public void testCasoComun6() {
        int[][] days = {
            {1, 0},
            {2, 100, 50}
        };
        int[] expected = {0, 0};
        int[] result = SilkRoadContest.solve(days);
        assertArrayEquals(expected, result, "Caso Común 6 falló");
    }

    @Test
    public void testCasoComun7() {
        int[][] days = {};
        int[] result = SilkRoadContest.solve(days);
        assertEquals(0, result.length, "Caso Común 7 falló");
    }

    @Test
    public void testCasoComun8() {
        int[] result = null;
        try {
            result = SilkRoadContest.solve(null);
            assertNotNull(result, "Resultado no debería ser null");
            assertEquals(0, result.length, "Caso Común 8 falló");
        } catch (Exception e) {
            fail("Caso Común 8 lanzó excepción: " + e.getMessage());
        }
    }
}

