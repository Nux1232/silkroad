import java.util.*;

/**
 * Clase para resolver el problema de la maratón ICPC 2024 - The Silk Road with Robots
 * Ciclo 3 - DOPO-I03-2025-02
 * 
 * Esta clase contiene los algoritmos para:
 * 1. Resolver el problema de la maratón (calcular máxima utilidad diaria)
 * 2. Simular la solución paso a paso
 */
public class SilkRoadContest {

    /**
     * Resuelve el problema de la maratón: calcular la máxima utilidad diaria
     * 
     * Formato de entrada days[i]:
     * - days[i][0] = tipo (1 = robot, 2 = tienda)
     * - days[i][1] = ubicación (location)
     * - days[i][2] = tenges (solo si tipo == 2)
     * 
     * @param days Matriz con datos de cada día
     * @return Array con la máxima utilidad obtenible para cada día
     */
    public static int[] solve(int[][] days) {
        if (days == null || days.length == 0) {
            return new int[0];
        }
        
        int numDays = days.length;
        int[] maxProfits = new int[numDays];
        
        ArrayList<Integer> robotLocations = new ArrayList<Integer>();
        ArrayList<int[]> storeData = new ArrayList<int[]>();
        
        for (int day = 0; day < numDays; day++) {
            int type = days[day][0];
            int location = days[day][1];
            
            if (type == 1) {
                robotLocations.add(Integer.valueOf(location));
            } else if (type == 2) {
                int tenges = days[day][2];
                storeData.add(new int[]{location, tenges});
            }
            
            maxProfits[day] = calculateMaxProfit(robotLocations, storeData);
        }
        
        return maxProfits;
    }
    
    /**
     * Calcula la máxima ganancia posible dado un conjunto de robots y tiendas
     * @param robotLocations Lista de ubicaciones de robots
     * @param storeData Lista de tiendas [location, tenges]
     * @return Máxima ganancia obtenible
     */
    private static int calculateMaxProfit(ArrayList<Integer> robotLocations, ArrayList<int[]> storeData) {
        if (robotLocations.isEmpty() || storeData.isEmpty()) {
            return 0;
        }
        
        int numRobots = robotLocations.size();
        int numStores = storeData.size();
        
        int[][] profitMatrix = new int[numRobots][numStores];
        
        for (int r = 0; r < numRobots; r++) {
            int robotLoc = robotLocations.get(r).intValue();
            for (int s = 0; s < numStores; s++) {
                int storeLoc = storeData.get(s)[0];
                int storeTenges = storeData.get(s)[1];
                int distance = Math.abs(robotLoc - storeLoc);
                int profit = storeTenges - distance;
                profitMatrix[r][s] = Math.max(0, profit);
            }
        }
        
        return findOptimalAssignment(profitMatrix);
    }
    
    /**
     * Encuentra la asignación óptima de robots a tiendas usando DP con bitmask
     * @param profitMatrix Matriz de ganancias [robot][store]
     * @return Máxima ganancia total
     */
    private static int findOptimalAssignment(int[][] profitMatrix) {
        int numRobots = profitMatrix.length;
        int numStores = profitMatrix[0].length;
        
        int maxAssignments = Math.min(numRobots, numStores);
        int maxMask = 1 << numStores;
        int[][] dp = new int[numRobots + 1][maxMask];
        
        for (int i = 0; i <= numRobots; i++) {
            for (int mask = 0; mask < maxMask; mask++) {
                dp[i][mask] = -1;
            }
        }
        dp[0][0] = 0;
        
        for (int r = 0; r < numRobots; r++) {
            for (int mask = 0; mask < maxMask; mask++) {
                if (dp[r][mask] < 0) continue;
                
                dp[r + 1][mask] = Math.max(dp[r + 1][mask], dp[r][mask]);
                
                for (int s = 0; s < numStores; s++) {
                    if ((mask & (1 << s)) == 0) {
                        int newMask = mask | (1 << s);
                        int newProfit = dp[r][mask] + profitMatrix[r][s];
                        dp[r + 1][newMask] = Math.max(dp[r + 1][newMask], newProfit);
                    }
                }
            }
        }
        
        int maxProfit = 0;
        for (int mask = 0; mask < maxMask; mask++) {
            maxProfit = Math.max(maxProfit, dp[numRobots][mask]);
        }
        
        return maxProfit;
    }

    /**
     * Simula la solución paso a paso, mostrando los movimientos día a día
     * @param days Matriz con los datos de cada día (formato ICPC)
     * @param slow true para simulación lenta, false para simulación rápida
     */
    public static void simulate(int[][] days, boolean slow) {
        if (days == null || days.length == 0) {
            System.out.println("No hay datos para simular.");
            return;
        }
        
        System.out.println("\\n============================================================");
        System.out.println("  SIMULACIÓN SILK ROAD - PROBLEMA MARATÓN ICPC 2024");
        System.out.println("============================================================");
        System.out.println("Modo: " + (slow ? "LENTO (paso a paso)" : "RÁPIDO"));
        System.out.println("Días a simular: " + days.length);
        System.out.println();
        
        SilkRoad simulator = new SilkRoad(10000);
        simulator.makeVisible();
        
        ArrayList<Integer> robotLocations = new ArrayList<Integer>();
        ArrayList<int[]> storeData = new ArrayList<int[]>();
        
        for (int day = 0; day < days.length; day++) {
            System.out.println("\\n------------------------------------------------------------");
            System.out.println("DÍA " + (day + 1));
            System.out.println("------------------------------------------------------------");
            
            int type = days[day][0];
            int location = days[day][1];
            
            if (day > 0) {
                simulator.resupplyStores();
                simulator.returnRobots();
            }
            
            if (type == 1) {
                System.out.println("+ Agregando ROBOT en ubicación " + location);
                robotLocations.add(Integer.valueOf(location));
                simulator.placeRobot(location);
            } else if (type == 2) {
                int tenges = days[day][2];
                System.out.println("+ Agregando TIENDA en ubicación " + location + " con " + tenges + " tenges");
                storeData.add(new int[]{location, tenges});
                simulator.placeStore(location, tenges);
            }
            
            waitTime(slow ? 1000 : 300);
            
            int maxProfit = calculateMaxProfit(robotLocations, storeData);
            System.out.println("\\nGanancia máxima posible: " + maxProfit);
            
            if (!robotLocations.isEmpty() && !storeData.isEmpty()) {
                int[][] assignment = findOptimalAssignmentDetails(robotLocations, storeData);
                System.out.println("\\nMovimientos óptimos:");
                
                for (int i = 0; i < assignment.length; i++) {
                    int robotIdx = assignment[i][0];
                    int storeIdx = assignment[i][1];
                    
                    if (storeIdx >= 0) {
                        int robotLoc = robotLocations.get(robotIdx).intValue();
                        int storeLoc = storeData.get(storeIdx)[0];
                        int storeTenges = storeData.get(storeIdx)[1];
                        int distance = Math.abs(storeLoc - robotLoc);
                        int profit = storeTenges - distance;
                        
                        if (profit > 0) {
                            System.out.println("  Robot en " + robotLoc + " → Tienda en " + storeLoc + 
                                             " (distancia: " + distance + ", ganancia: " + profit + ")");
                            
                            int meters = storeLoc - robotLoc;
                            simulator.moveRobot(robotLoc, meters);
                            waitTime(slow ? 1500 : 400);
                        }
                    }
                }
            }
            
            System.out.println();
            waitTime(slow ? 2000 : 500);
        }
        
        System.out.println("\\n============================================================");
        System.out.println("  SIMULACIÓN COMPLETADA");
        System.out.println("============================================================");
        
        waitTime(2000);
        simulator.finish();
    }
    
    /**
     * Encuentra los detalles de la asignación óptima
     * @return Matriz [robot_index, store_index]
     */
    private static int[][] findOptimalAssignmentDetails(ArrayList<Integer> robotLocations, ArrayList<int[]> storeData) {
        int numRobots = robotLocations.size();
        int numStores = storeData.size();
        
        int[][] profitMatrix = new int[numRobots][numStores];
        for (int r = 0; r < numRobots; r++) {
            int robotLoc = robotLocations.get(r).intValue();
            for (int s = 0; s < numStores; s++) {
                int storeLoc = storeData.get(s)[0];
                int storeTenges = storeData.get(s)[1];
                int distance = Math.abs(robotLoc - storeLoc);
                profitMatrix[r][s] = Math.max(0, storeTenges - distance);
            }
        }
        
        boolean[] storeUsed = new boolean[numStores];
        int[][] assignments = new int[numRobots][2];
        
        for (int r = 0; r < numRobots; r++) {
            assignments[r][0] = r;
            assignments[r][1] = -1;
            
            int bestStore = -1;
            int bestProfit = 0;
            
            for (int s = 0; s < numStores; s++) {
                if (!storeUsed[s] && profitMatrix[r][s] > bestProfit) {
                    bestProfit = profitMatrix[r][s];
                    bestStore = s;
                }
            }
            
            if (bestStore >= 0) {
                assignments[r][1] = bestStore;
                storeUsed[bestStore] = true;
            }
        }
        
        return assignments;
    }

    /**
     * Método auxiliar para pausas en la simulación
     */
    private static void waitTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
