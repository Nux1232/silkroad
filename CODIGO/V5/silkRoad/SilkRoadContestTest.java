package silkRoad;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SilkRoadContestTest {

    @Test
    public void testSolveBasic() {
        int[][] days1 = {{5, 2, 8, 3, 1}};
        int[] result1 = SilkRoadContest.solve(days1);
        assertEquals(13, result1[0], "Error en caso básico, debe sumar 5 + 8");

        int[][] days2 = {
            {5, 2, 8, 3, 1},
            {3, 7, 1, 9, 4},
            {2, 1, 6, 4, 3}
        };
        int[] result2 = SilkRoadContest.solve(days2);
        // Aquí podrías adaptar según el resultado esperado correcto
        assertNotNull(result2);
        assertEquals(3, result2.length, "Debe retornar resultado para cada día");
    }

    @Test
    public void testSolveEdgeCases() {
        int[][] emptyDays = {};
        int[] emptyResult = SilkRoadContest.solve(emptyDays);
        assertEquals(0, emptyResult.length, "Array vacío debe retornar array vacío");

        int[][] singleDay = {{10}};
        int[] singleResult = SilkRoadContest.solve(singleDay);
        assertEquals(10, singleResult[0], "Un solo elemento debe retornar ese elemento");

        int[][] negativeDays = {{-5, -2, -8, -3, -1}};
        int[] negativeResult = SilkRoadContest.solve(negativeDays);
        assertEquals(0, negativeResult[0], "Valores negativos deben dar resultado 0");

        int[][] twoDays = {{5, 3}};
        int[] twoResult = SilkRoadContest.solve(twoDays);
        assertEquals(5, twoResult[0], "Debe tomar el mayor entre dos elementos");
    }

    @Test
    public void testSolveComplex() {
        int[][] complexDays = {
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {10, 1, 1, 10, 1}
        };
        int[] complexResult = SilkRoadContest.solve(complexDays);
        assertTrue(complexResult[0] >= 9, "Ascendente debe ser al menos 9");
        assertTrue(complexResult[1] >= 9, "Descendente debe ser al menos 9");
        assertTrue(complexResult[2] >= 11, "Alternado debe ser al menos 11");
    }

    @Test
    public void testSolveNullHandling() {
        try {
            int[] nullResult = SilkRoadContest.solve(null);
            assertNotNull(nullResult, "Null input no debe devolver null");
            assertEquals(0, nullResult.length, "Null input debe devolver array vacío");
        } catch (Exception e) {
            fail("No debe lanzar excepción al pasar null");
        }
    }

    @Test
    public void testSimulateBasic() {
        int[][] testDays = {
            {5, 2, 8},
            {3, 7, 1}
        };

        assertDoesNotThrow(() -> SilkRoadContest.simulate(testDays, false), "Simulación básica falló");
    }

    @Test
    public void testSimulateEdgeCases() {
        assertDoesNotThrow(() -> SilkRoadContest.simulate(new int[][]{}, false), "Simulación con array vacío falló");
        assertDoesNotThrow(() -> SilkRoadContest.simulate(null, false), "Simulación con null falló");
    }

    @Test
    public void testPerformance() {
        int numDays = 100;
        int numLocations = 50;
        int[][] largeDays = new int[numDays][numLocations];
        for (int i = 0; i < numDays; i++) {
            for (int j = 0; j < numLocations; j++) {
                largeDays[i][j] = (int) (Math.random() * 100);
            }
        }
        long startTime = System.currentTimeMillis();
        int[] results = SilkRoadContest.solve(largeDays);
        long endTime = System.currentTimeMillis();
        assertEquals(numDays, results.length, "Debe procesar todos los días");
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ms");
    }

    @Test
    public void testConsistency() {
        int[][] testDays = {
            {10, 5, 2, 7, 8},
            {1, 4, 9, 2, 3}
        };
        int[] result1 = SilkRoadContest.solve(testDays);
        int[] result2 = SilkRoadContest.solve(testDays);
        int[] result3 = SilkRoadContest.solve(testDays);

        assertArrayEquals(result1, result2, "Resultados inconsistentes entre ejecuciones");
        assertArrayEquals(result2, result3, "Resultados inconsistentes entre ejecuciones");
    }
}

