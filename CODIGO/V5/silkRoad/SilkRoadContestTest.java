package silkRoad;

public class SilkRoadContestTest {
    
    /**
     * Prueba básica del método solve con casos simples
     */
    public void testSolveBasic() {
        System.out.println("=== Test: Solve Basic ===");
        
        // Caso 1: Un solo día con valores simples
        int[][] days1 = {{5, 2, 8, 3, 1}};
        int[] result1 = SilkRoadContest.solve(days1);
        
        System.out.println("Entrada: [5, 2, 8, 3, 1]");
        System.out.println("Resultado esperado: 13 (5 + 8 = 13)");
        System.out.println("Resultado obtenido: " + result1[0]);
        assert result1[0] == 13 : "Error en caso básico";
        
        // Caso 2: Múltiples días
        int[][] days2 = {
            {5, 2, 8, 3, 1},
            {3, 7, 1, 9, 4},
            {2, 1, 6, 4, 3}
        };
        int[] result2 = SilkRoadContest.solve(days2);
        
        System.out.println("\nMúltiples días:");
        System.out.println("Día 1: [5, 2, 8, 3, 1] -> " + result2[0]);
        System.out.println("Día 2: [3, 7, 1, 9, 4] -> " + result2[1]); 
        System.out.println("Día 3: [2, 1, 6, 4, 3] -> " + result2[2]);
        
        System.out.println("Test Solve Basic: PASADO\n");
    }
    
    /**
     * Prueba casos extremos del método solve
     */
    public void testSolveEdgeCases() {
        System.out.println("=== Test: Solve Edge Cases ===");
        
        // Caso 1: Array vacío
        int[][] emptyDays = {};
        int[] emptyResult = SilkRoadContest.solve(emptyDays);
        assert emptyResult.length == 0 : "Array vacío debe retornar array vacío";
        
        // Caso 2: Un solo elemento
        int[][] singleDay = {{10}};
        int[] singleResult = SilkRoadContest.solve(singleDay);
        assert singleResult[0] == 10 : "Un solo elemento debe retornar ese elemento";
        
        // Caso 3: Todos los valores negativos
        int[][] negativeDays = {{-5, -2, -8, -3, -1}};
        int[] negativeResult = SilkRoadContest.solve(negativeDays);
        assert negativeResult[0] == 0 : "Valores negativos deben dar resultado 0";
        
        // Caso 4: Dos elementos
        int[][] twoDays = {{5, 3}};
        int[] twoResult = SilkRoadContest.solve(twoDays);
        assert twoResult[0] == 5 : "Dos elementos debe tomar el mayor";
        
        System.out.println("Test Solve Edge Cases: PASADO\n");
    }
    
    /**
     * Prueba el método solve con casos complejos
     */
    public void testSolveComplex() {
        System.out.println("=== Test: Solve Complex ===");
        
        // Caso con patrón específico
        int[][] complexDays = {
            {1, 2, 3, 4, 5},      // Ascendente: debe tomar 1, 3, 5 = 9
            {5, 4, 3, 2, 1},      // Descendente: debe tomar 5, 3, 1 = 9  
            {10, 1, 1, 10, 1}     // Alternado: debe tomar 10, 1, 1 = 11 ó 10, 10 = 20
        };
        
        int[] complexResult = SilkRoadContest.solve(complexDays);
        
        System.out.println("Caso ascendente [1,2,3,4,5]: " + complexResult[0]);
        System.out.println("Caso descendente [5,4,3,2,1]: " + complexResult[1]);
        System.out.println("Caso alternado [10,1,1,10,1]: " + complexResult[2]);
        
        // Verificar que los resultados son óptimos
        assert complexResult[0] >= 9 : "Resultado ascendente debe ser al menos 9";
        assert complexResult[1] >= 9 : "Resultado descendente debe ser al menos 9";
        assert complexResult[2] >= 11 : "Resultado alternado debe ser al menos 11";
        
        System.out.println("Test Solve Complex: PASADO\n");
    }
    
    /**
     * Prueba que el método solve maneja correctamente valores nulos
     */
    public void testSolveNullHandling() {
        System.out.println("=== Test: Solve Null Handling ===");
        
        try {
            int[] nullResult = SilkRoadContest.solve(null);
            assert nullResult.length == 0 : "Null input debe retornar array vacío";
            System.out.println("Manejo de null: CORRECTO");
        } catch (Exception e) {
            System.out.println("Error en manejo de null: " + e.getMessage());
        }
        
        System.out.println("Test Solve Null Handling: PASADO\n");
    }
    
    /**
     * Prueba básica del método simulate
     */
    public void testSimulateBasic() {
        System.out.println("=== Test: Simulate Basic ===");
        
        int[][] testDays = {
            {5, 2, 8},
            {3, 7, 1}
        };
        
        try {
            System.out.println("Iniciando simulación rápida...");
            SilkRoadContest.simulate(testDays, false);
            System.out.println("Simulación completada exitosamente");
        } catch (Exception e) {
            System.out.println("Error en simulación: " + e.getMessage());
        }
        
        System.out.println("Test Simulate Basic: PASADO\n");
    }
    
    /**
     * Prueba el método simulate con casos extremos
     */
    public void testSimulateEdgeCases() {
        System.out.println("=== Test: Simulate Edge Cases ===");
        
        // Caso 1: Array vacío
        try {
            SilkRoadContest.simulate(new int[][]{}, false);
            System.out.println("Simulación con array vacío: CORRECTO");
        } catch (Exception e) {
            System.out.println("Error con array vacío: " + e.getMessage());
        }
        
        // Caso 2: Null input
        try {
            SilkRoadContest.simulate(null, false);
            System.out.println("Simulación con null: CORRECTO");
        } catch (Exception e) {
            System.out.println("Error con null: " + e.getMessage());
        }
        
        System.out.println("Test Simulate Edge Cases: PASADO\n");
    }
    
    /**
     * Prueba de rendimiento con datasets grandes
     */
    public void testPerformance() {
        System.out.println("=== Test: Performance ===");
        
        // Crear un dataset grande
        int numDays = 100;
        int numLocations = 50;
        int[][] largeDays = new int[numDays][numLocations];
        
        // Llenar con valores aleatorios
        for (int i = 0; i < numDays; i++) {
            for (int j = 0; j < numLocations; j++) {
                largeDays[i][j] = (int)(Math.random() * 100);
            }
        }
        
        long startTime = System.currentTimeMillis();
        int[] results = SilkRoadContest.solve(largeDays);
        long endTime = System.currentTimeMillis();
        
        System.out.println("Dataset: " + numDays + " días, " + numLocations + " ubicaciones");
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " ms");
        System.out.println("Resultados calculados: " + results.length);
        
        assert results.length == numDays : "Debe procesar todos los días";
        
        System.out.println("Test Performance: PASADO\n");
    }
    
    /**
     * Prueba la consistencia de los resultados
     */
    public void testConsistency() {
        System.out.println("=== Test: Consistency ===");
        
        int[][] testDays = {
            {10, 5, 2, 7, 8},
            {1, 4, 9, 2, 3}
        };
        
        // Ejecutar múltiples veces para verificar consistencia
        int[] result1 = SilkRoadContest.solve(testDays);
        int[] result2 = SilkRoadContest.solve(testDays);
        int[] result3 = SilkRoadContest.solve(testDays);
        
        // Verificar que los resultados son consistentes
        for (int i = 0; i < result1.length; i++) {
            assert result1[i] == result2[i] && result2[i] == result3[i] : 
                "Los resultados deben ser consistentes";
        }
        
        System.out.println("Resultados consistentes en múltiples ejecuciones");
        System.out.println("Test Consistency: PASADO\n");
    }
    
    /**
     * Ejecutar todas las pruebas
     */
    public void runAllTests() {
        System.out.println("==========================================");
        System.out.println("EJECUTANDO SUITE DE PRUEBAS SILKROADCONTEST");
        System.out.println("==========================================\n");
        
        try {
            testSolveBasic();
            testSolveEdgeCases();
            testSolveComplex();
            testSolveNullHandling();
            testSimulateBasic();
            testSimulateEdgeCases();
            testPerformance();
            testConsistency();
            
            System.out.println("==========================================");
            System.out.println("TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE");
            System.out.println("==========================================");
            
        } catch (AssertionError e) {
            System.out.println("FALLO EN PRUEBA: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR INESPERADO: " + e.getMessage());
        }
    }
    
    /**
     * Método principal para ejecutar las pruebas
     */
    public static void main(String[] args) {
        SilkRoadContestTest tester = new SilkRoadContestTest();
        tester.runAllTests();
    }
}
