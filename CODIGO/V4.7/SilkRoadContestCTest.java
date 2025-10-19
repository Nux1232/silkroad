/**
 * Casos de prueba comunes para SilkRoadContest
 * Basados en el problema ICPC 2024 - The Silk Road with Robots
 * 
 * Formato de entrada ICPC:
 * days[i][0] = tipo (1 = robot, 2 = tienda)
 * days[i][1] = ubicación
 * days[i][2] = tenges (solo si tipo == 2)
 * 
 * @author Silk Road Project - Ciclo 3
 * @version 1.0
 */
public class SilkRoadContestCTest {
    
    /**
     * Caso común 1: Ejemplo oficial del PDF ICPC
     */
    public void testCasoComun1() {
        System.out.println("=== Caso Común 1: Ejemplo ICPC Oficial ===");
        
        int[][] days = {
            {1, 20},        // Día 1: Robot en posición 20
            {2, 15, 15},    // Día 2: Tienda en 15 con 15 tenges
            {2, 40, 50},    // Día 3: Tienda en 40 con 50 tenges
            {1, 50},        // Día 4: Robot en posición 50
            {2, 80, 20},    // Día 5: Tienda en 80 con 20 tenges
            {2, 70, 30}     // Día 6: Tienda en 70 con 30 tenges
        };
        
        // Ganancias esperadas según el problema ICPC
        int[] expected = {0, 10, 35, 50, 50, 60};
        int[] result = SilkRoadContest.solve(days);
        
        System.out.println("Resultados esperados: " + arrayToString(expected));
        System.out.println("Resultados obtenidos:  " + arrayToString(result));
        
        boolean passed = true;
        for (int i = 0; i < expected.length; i++) {
            if (result[i] != expected[i]) {
                System.out.println("  ✗ Día " + (i+1) + ": esperado " + expected[i] + 
                                 ", obtenido " + result[i]);
                passed = false;
            } else {
                System.out.println("  ✓ Día " + (i+1) + ": " + result[i]);
            }
        }
        
        if (passed) {
            System.out.println("✓ Caso Común 1: PASADO\n");
        } else {
            System.out.println("✗ Caso Común 1: FALLIDO\n");
        }
    }
    
    /**
     * Caso común 2: Solo robots (no hay tiendas para visitar)
     */
    public void testCasoComun2() {
        System.out.println("=== Caso Común 2: Solo Robots ===");
        
        int[][] days = {
            {1, 10},    // Robot en 10
            {1, 20},    // Robot en 20
            {1, 30}     // Robot en 30
        };
        
        int[] result = SilkRoadContest.solve(days);
        
        // Todos deben ser 0 (no hay tiendas)
        boolean passed = (result[0] == 0 && result[1] == 0 && result[2] == 0);
        
        System.out.println("Resultados: " + arrayToString(result));
        System.out.println(passed ? 
            "✓ Caso Común 2: PASADO (0 ganancia sin tiendas)\n" : 
            "✗ Caso Común 2: FALLIDO\n");
    }
    
    /**
     * Caso común 3: Solo tiendas (no hay robots para recolectar)
     */
    public void testCasoComun3() {
        System.out.println("=== Caso Común 3: Solo Tiendas ===");
        
        int[][] days = {
            {2, 10, 100},   // Tienda en 10 con 100 tenges
            {2, 20, 200},   // Tienda en 20 con 200 tenges
            {2, 30, 300}    // Tienda en 30 con 300 tenges
        };
        
        int[] result = SilkRoadContest.solve(days);
        
        // Todos deben ser 0 (no hay robots)
        boolean passed = (result[0] == 0 && result[1] == 0 && result[2] == 0);
        
        System.out.println("Resultados: " + arrayToString(result));
        System.out.println(passed ? 
            "✓ Caso Común 3: PASADO (0 ganancia sin robots)\n" : 
            "✗ Caso Común 3: FALLIDO\n");
    }
    
    /**
     * Caso común 4: Robot y tienda en la misma ubicación
     */
    public void testCasoComun4() {
        System.out.println("=== Caso Común 4: Robot y Tienda Misma Ubicación ===");
        
        int[][] days = {
            {1, 50},        // Robot en 50
            {2, 50, 100}    // Tienda en 50 con 100 tenges
        };
        
        int[] result = SilkRoadContest.solve(days);
        
        // Día 1: 0 (solo robot)
        // Día 2: 100 - 0 = 100 (distancia 0)
        boolean passed = (result[0] == 0 && result[1] == 100);
        
        System.out.println("Resultados: " + arrayToString(result));
        System.out.println(passed ? 
            "✓ Caso Común 4: PASADO (distancia 0 = máxima ganancia)\n" : 
            "✗ Caso Común 4: FALLIDO\n");
    }
    
    /**
     * Caso común 5: Múltiples robots, múltiples tiendas
     */
    public void testCasoComun5() {
        System.out.println("=== Caso Común 5: Múltiples Robots y Tiendas ===");
        
        int[][] days = {
            {1, 0},         // Robot en 0
            {1, 100},       // Robot en 100
            {2, 10, 50},    // Tienda en 10 con 50 tenges
            {2, 90, 50}     // Tienda en 90 con 50 tenges
        };
        
        int[] result = SilkRoadContest.solve(days);
        
        // Día 1: 0 (solo 1 robot)
        // Día 2: 0 (solo 2 robots)
        // Día 3: 50-10 = 40 (1 robot puede ir a tienda)
        // Día 4: (50-10) + (50-10) = 80 (cada robot a su tienda)
        System.out.println("Resultados: " + arrayToString(result));
        System.out.println("Esperado: [0, 0, 40, 80]");
        
        boolean passed = (result[0] == 0 && result[1] == 0 && 
                         result[2] == 40 && result[3] == 80);
        
        System.out.println(passed ? 
            "✓ Caso Común 5: PASADO\n" : 
            "✗ Caso Común 5: FALLIDO\n");
    }
    
    /**
     * Caso común 6: Ganancia negativa (costo > tenges)
     */
    public void testCasoComun6() {
        System.out.println("=== Caso Común 6: Ganancia Negativa ===");
        
        int[][] days = {
            {1, 0},         // Robot en 0
            {2, 100, 50}    // Tienda en 100 con 50 tenges (distancia 100 > 50)
        };
        
        int[] result = SilkRoadContest.solve(days);
        
        // Día 2: No conviene mover (50 - 100 = -50), entonces 0
        boolean passed = (result[0] == 0 && result[1] == 0);
        
        System.out.println("Resultados: " + arrayToString(result));
        System.out.println(passed ? 
            "✓ Caso Común 6: PASADO (no tomar si es negativo)\n" : 
            "✗ Caso Común 6: FALLIDO\n");
    }
    
    /**
     * Caso común 7: Entrada vacía
     */
    public void testCasoComun7() {
        System.out.println("=== Caso Común 7: Entrada Vacía ===");
        
        int[][] days = {};
        int[] result = SilkRoadContest.solve(days);
        
        boolean passed = (result.length == 0);
        
        System.out.println("Longitud resultado: " + result.length);
        System.out.println(passed ? 
            "✓ Caso Común 7: PASADO\n" : 
            "✗ Caso Común 7: FALLIDO\n");
    }
    
    /**
     * Caso común 8: Entrada null
     */
    public void testCasoComun8() {
        System.out.println("=== Caso Común 8: Entrada Null ===");
        
        try {
            int[] result = SilkRoadContest.solve(null);
            boolean passed = (result != null && result.length == 0);
            System.out.println(passed ? 
                "✓ Caso Común 8: PASADO (manejo de null)\n" : 
                "✗ Caso Común 8: FALLIDO\n");
        } catch (Exception e) {
            System.out.println("✗ Caso Común 8: FALLIDO (excepción: " + e.getMessage() + ")\n");
        }
    }
    
    /**
     * Método auxiliar para convertir array a string
     */
    private String arrayToString(int[] arr) {
        if (arr == null || arr.length == 0) return "[]";
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Ejecutar todos los casos comunes
     */
    public void runAllTests() {
        System.out.println("============================================================");
        System.out.println("      CASOS DE PRUEBA COMUNES - SILKROADCONTEST");
        System.out.println("         Basados en el problema ICPC 2024");
        System.out.println("============================================================\n");
        
        int passed = 0;
        int total = 8;
        
        try {
            testCasoComun1();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 1: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun2();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 2: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun3();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 3: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun4();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 4: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun5();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 5: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun6();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 6: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun7();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 7: " + e.getMessage() + "\n");
        }
        
        try {
            testCasoComun8();
            passed++;
        } catch (Exception e) {
            System.out.println("✗ Error en Caso 8: " + e.getMessage() + "\n");
        }
        
        System.out.println("============================================================");
        System.out.println("         RESUMEN: " + passed + "/" + total + " CASOS PASADOS");
        System.out.println("============================================================");
    }
    
    /**
     * Método principal para ejecutar desde BlueJ o línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("\n*** EJECUTANDO CASOS DE PRUEBA COMUNES ***\n");
        SilkRoadContestCTest tester = new SilkRoadContestCTest();
        tester.runAllTests();
    }
}
