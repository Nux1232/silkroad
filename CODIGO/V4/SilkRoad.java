import java.util.*;

public class SilkRoad {

    private int length;
    private ArrayList<Store> stores;
    private ArrayList<Robot> robots;
    private Canvas canvas;
    private boolean visible;
    private int totalProfit;
    private int[][] days; // Para el constructor con días
    private ArrayList<Integer> profitHistory; // Historial de ganancias por movimiento
    private boolean isFinished;
    private SpiralPath spiralPath; // Sistema de coordenadas en espiral
    private boolean useSpiralMovement; // Si usar movimiento en espiral

    /**
     * Constructor que crea una ruta de longitud específica con movimiento en espiral
     * @param length Longitud de la ruta
     */
    public SilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.canvas = Canvas.getCanvas();
        this.visible = true;
        this.totalProfit = 0;
        this.profitHistory = new ArrayList<>();
        this.isFinished = false;
        this.useSpiralMovement = true;

        // Crear el sistema de coordenadas en espiral
        int inicioX = 120;
        int inicioY = 120;
        int longitudInicial = 30;
        int incrementoLongitud = 30;
        this.spiralPath = new SpiralPath(length, inicioX, inicioY, longitudInicial, incrementoLongitud);

        // Configurar título del canvas
        canvas.setTitle("SilkRoad Simulator - Spiral Movement System (Length: " + length + ")");

        // Dibujar la espiral como camino
        mostrarCaminoEspiral(length, inicioX, inicioY, 15, longitudInicial, incrementoLongitud);

        System.out.println("SilkRoad inicializado con sistema de movimiento en espiral");
        System.out.println("Coordenadas de la espiral calculadas para " + length + " posiciones");
    }

    /**
     * Constructor que crea una ruta basada en días de simulación
     * @param days Matriz de días con información de tiendas y ganancias
     */
    public SilkRoad(int[][] days) {
        this(days.length > 0 ? days[0].length : 10); // Usar número de locaciones como longitud
        this.days = days;

        // Inicializar tiendas basadas en los días usando espiral
        if (days.length > 0) {
            for (int location = 0; location < days[0].length; location++) {
                int initialTenges = days[0][location]; // Usar primer día como valor inicial
                placeStore(location, initialTenges);
            }
        }
    }

    /**
     * Activar o desactivar el movimiento en espiral
     * @param useSpiral true para usar espiral, false para movimiento lineal
     */
    public void setSpiralMovement(boolean useSpiral) {
        this.useSpiralMovement = useSpiral;

        if (useSpiral) {
            // Configurar todos los robots y tiendas para usar espiral
            for (Robot robot : robots) {
                robot.setSpiralPath(spiralPath);
            }
            for (Store store : stores) {
                store.setSpiralPath(spiralPath);
            }
            System.out.println("Movimiento en espiral ACTIVADO");
        } else {
            // Desactivar espiral en robots y tiendas
            for (Robot robot : robots) {
                robot.setFollowSpiral(false);
            }
            for (Store store : stores) {
                store.setFollowSpiral(false);
            }
            System.out.println("Movimiento en espiral DESACTIVADO - usando movimiento lineal");
        }
    }

    /**
     * Coloca una tienda en una ubicación específica usando posicionamiento en espiral
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     */
    public void placeStore(int location, int tenges) {
        // Verificar si ya existe una tienda en esa ubicación
        removeStore(location); // Remover si existe

        // Crear nueva tienda usando el sistema de espiral
        Store newStore;
        if (useSpiralMovement && spiralPath != null) {
            newStore = new Store(location, tenges, spiralPath);
            System.out.println("Tienda colocada en posición " + location + " (espiral): " + newStore.getSpiralInfo());
        } else {
            // Fallback a posicionamiento legacy
            int x = 120 + (location * 50);
            int y = 120 + (location * 20);
            newStore = new Store(location, tenges, x, y);
            System.out.println("Tienda colocada en posición " + location + " (lineal): (" + x + ", " + y + ")");
        }

        stores.add(newStore);

        if (visible) {
            newStore.show();
        }
    }

    /**
     * Coloca un robot en una ubicación específica usando movimiento en espiral
     * @param location Ubicación inicial del robot
     */
    public void placeRobot(int location) {
        // Crear nuevo robot usando el sistema de espiral
        Robot newRobot;
        if (useSpiralMovement && spiralPath != null) {
            newRobot = new Robot(location, spiralPath);
            System.out.println("Robot colocado en posición " + location + " (espiral): " + newRobot.getSpiralInfo());
        } else {
            // Fallback a posicionamiento legacy
            int x = 120 + (location * 50);
            int y = 100 + (location * 20);
            newRobot = new Robot(location, x, y);
            System.out.println("Robot colocado en posición " + location + " (lineal): (" + x + ", " + y + ")");
        }

        robots.add(newRobot);

        if (visible) {
            newRobot.show();
        }
    }

    /**
     * Mueve todos los robots siguiendo el patrón de espiral
     */
    public void moveRobots() {
        System.out.println("\n=== Moviendo todos los robots ===");
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = robots.get(i);
            System.out.println("Moviendo Robot " + (i+1) + " desde posición " + robot.getLocation());

            if (useSpiralMovement && robot.isFollowingSpiral()) {
                robot.moveToNextPosition();
                // Interactuar con tienda si existe en la nueva ubicación
                interactWithStore(robot, robot.getLocation());
            } else {
                // Movimiento legacy
                int currentLocation = robot.getLocation();
                int nextLocation = (currentLocation + 1) % length;
                moveRobot(currentLocation, nextLocation);
            }
        }
    }

    /**
     * Mueve un robot específico a una ubicación específica
     * @param robotLocation Ubicación actual del robot
     * @param targetLocation Ubicación objetivo
     */
    public void moveRobot(int robotLocation, int targetLocation) {
        for (Robot robot : robots) {
            if (robot.getLocation() == robotLocation) {
                System.out.println("Moviendo robot de posición " + robotLocation + " a posición " + targetLocation);

                if (useSpiralMovement && robot.isFollowingSpiral()) {
                    robot.slowMoveToPosition(targetLocation);
                } else {
                    // Movimiento legacy
                    robot.setLocation(targetLocation);
                    int newX = 120 + (targetLocation * 50);
                    int newY = 100 + (targetLocation * 20);
                    robot.moveTo(newX, newY);
                }

                // Interactuar con tienda si existe en la nueva ubicación
                interactWithStore(robot, targetLocation);
                break;
            }
        }
    }

    /**
     * Hace que un robot siga todo el recorrido de la espiral
     * @param robotIndex Índice del robot en la lista
     * @param delay Tiempo entre cada paso (milisegundos)
     */
    public void robotFollowCompleteSpiral(int robotIndex, int delay) {
        if (robotIndex < 0 || robotIndex >= robots.size()) {
            System.out.println("Índice de robot inválido: " + robotIndex);
            return;
        }

        Robot robot = robots.get(robotIndex);
        if (!useSpiralMovement || !robot.isFollowingSpiral()) {
            System.out.println("Robot no configurado para movimiento en espiral");
            return;
        }

        System.out.println("\n=== Robot " + robotIndex + " iniciando recorrido completo de la espiral ===");
        robot.followCompleteSpiral(delay);

        // Interactuar con todas las tiendas en el camino
        for (int pos = 0; pos < spiralPath.getLength(); pos++) {
            interactWithStore(robot, pos);
        }

        System.out.println("Robot " + robotIndex + " completó el recorrido de la espiral");
        destacarRobotGanador();
    }

    /**
     * Lógica de interacción entre robot y tienda mejorada con efectos visuales
     */
    private void interactWithStore(Robot robot, int location) {
        for (Store store : stores) {
            if (store.getLocation() == location && !store.isEmpty()) {
                System.out.println("Robot interactuando con tienda en posición " + location);

                // Simular recolección de tenges
                int profit = Math.min(10, store.getTenges()); // Recoger máximo 10 tenges
                robot.addGanancia(profit);
                store.reduceTenges(profit);
                totalProfit += profit;
                profitHistory.add(profit);

                System.out.println("Robot recolectó " + profit + " tenges. Ganancia total del robot: " + robot.getGanancia());

                if (store.getTenges() <= 0) {
                    store.marcarDesocupada();
                }
                break;
            }
        }
    }

    /**
     * Simulación completa paso a paso con movimiento en espiral
     * @param steps Número de pasos a simular
     */
    public void simulate(int steps) {
        System.out.println("\n=== INICIANDO SIMULACIÓN EN ESPIRAL ===");
        System.out.println("Pasos a simular: " + steps);
        System.out.println("Movimiento en espiral: " + (useSpiralMovement ? "ACTIVADO" : "DESACTIVADO"));
        System.out.println("Robots: " + robots.size() + ", Tiendas: " + stores.size());

        for (int step = 0; step < steps; step++) {
            System.out.println("\n--- Paso " + (step + 1) + " de " + steps + " ---");
            moveRobots();

            // Mostrar estado actual
            System.out.println("Ganancia total: " + totalProfit);
            System.out.println("Robots activos: " + robots.size());
            System.out.println("Tiendas con stock: " + 
                stores.stream().mapToInt(s -> s.isEmpty() ? 0 : 1).sum());

            // Mostrar información de espiral si está activada
            if (useSpiralMovement) {
                for (int i = 0; i < Math.min(robots.size(), 3); i++) { // Mostrar hasta 3 robots
                    Robot robot = robots.get(i);
                    System.out.println("  Robot " + (i+1) + ": " + robot.getSpiralInfo());
                }
            }

            canvas.wait(500); // Pausa entre pasos

            if (!ok()) {
                System.out.println("⚠️ Simulación detenida: Estado inválido detectado.");
                break;
            }
        }

        System.out.println("\n=== SIMULACIÓN COMPLETADA ===");
        mostrarEstadisticasCompletas();
        destacarRobotGanador();
    }

    /**
     * Demostración completa del recorrido en espiral
     * @param delay Tiempo entre movimientos en milisegundos
     */
    public void demoSpiralMovement(int delay) {
        System.out.println("\n=== DEMOSTRACIÓN DE MOVIMIENTO EN ESPIRAL ===");

        if (!useSpiralMovement) {
            setSpiralMovement(true);
        }

        // Colocar un robot en el centro para demostración
        if (robots.isEmpty()) {
            placeRobot(0);
        }

        Robot demoRobot = robots.get(0);
        System.out.println("Robot de demostración siguiendo toda la espiral...");

        robotFollowCompleteSpiral(0, delay);

        System.out.println("\nDemostración completada!");
        System.out.println("Ganancia final del robot: " + demoRobot.getGanancia());
    }

    /**
     * Mostrar estadísticas completas incluyendo información de espiral
     */
    private void mostrarEstadisticasCompletas() {
        System.out.println("\n=== ESTADÍSTICAS COMPLETAS DE LA SIMULACIÓN ===");
        System.out.println("Longitud de la ruta: " + length);
        System.out.println("Movimiento en espiral: " + (useSpiralMovement ? "ACTIVADO" : "DESACTIVADO"));
        System.out.println("Número de tiendas: " + stores.size());
        System.out.println("Número de robots: " + robots.size());
        System.out.println("Ganancia total acumulada: " + totalProfit);
        System.out.println("Movimientos registrados: " + profitHistory.size());

        if (!robots.isEmpty()) {
            int maxGanancia = robots.stream().mapToInt(Robot::getGanancia).max().orElse(0);
            int minGanancia = robots.stream().mapToInt(Robot::getGanancia).min().orElse(0);
            double avgGanancia = robots.stream().mapToInt(Robot::getGanancia).average().orElse(0.0);
            System.out.println("Ganancia máxima por robot: " + maxGanancia);
            System.out.println("Ganancia mínima por robot: " + minGanancia);
            System.out.println("Ganancia promedio por robot: " + String.format("%.2f", avgGanancia));
        }

        long tiendasVacias = stores.stream().mapToLong(Store::getDesocupaciones).sum();
        System.out.println("Total de vaciados de tiendas: " + tiendasVacias);

        if (useSpiralMovement && spiralPath != null) {
            System.out.println("\nINFORMACIÓN DE LA ESPIRAL:");
            System.out.println("Posiciones totales en la espiral: " + spiralPath.getLength());
            System.out.println("Coordenadas calculadas correctamente");
        }

        System.out.println("Estado de la simulación: " + (isFinished ? "Finalizada" : "Activa"));
        System.out.println("===============================================");
    }

    /**
     * Obtener información detallada de la espiral
     * @return String con información de la espiral
     */
    public String getSpiralInfo() {
        if (spiralPath != null) {
            return String.format("SpiralPath: %d posiciones, inicio=(%d,%d), movimiento=%s",
                spiralPath.getLength(), 120, 120, useSpiralMovement ? "ACTIVADO" : "DESACTIVADO");
        } else {
            return "SpiralPath: No configurado";
        }
    }

    // === MÉTODOS ORIGINALES MANTENIDOS PARA COMPATIBILIDAD ===

    /**
     * Destacar el robot ganador con efectos visuales mejorados
     */
    public void destacarRobotGanador() {
        if (robots.isEmpty()) {
            System.out.println("No hay robots para destacar.");
            return;
        }

        // Buscar el robot con mayor ganancia
        Robot ganador = null;
        int maxGanancia = -1;
        for (Robot robot : robots) {
            if (robot.getGanancia() > maxGanancia) {
                maxGanancia = robot.getGanancia();
                ganador = robot;
            }
        }

        // Destacar al ganador si existe
        if (ganador != null && maxGanancia > 0) {
            System.out.println("\n🏆 ¡ROBOT GANADOR ENCONTRADO! 🏆");
            System.out.println("Ganancia: " + maxGanancia + " tenges");
            if (useSpiralMovement) {
                System.out.println("Información: " + ganador.getSpiralInfo());
            }

            // Cambiar a color dorado para destacar
            String colorOriginal = ganador.getColor();
            ganador.changeColor("gold");

            // Hacer que parpadee varias veces
            for (int i = 0; i < 3; i++) {
                ganador.parpadear();
                canvas.wait(500);
            }

            // Destacar con borde en el canvas
            canvas.highlightShape(ganador, "red");
            canvas.drawText("¡GANADOR!", ganador.getX() + 25, ganador.getY() - 10, "red");
            canvas.wait(2000);

            // Restaurar color original
            ganador.changeColor(colorOriginal);
            System.out.println("Robot ganador destacado exitosamente.");
        } else {
            System.out.println("No se encontró un robot ganador con ganancias positivas.");
        }
    }

    /**
     * Remueve una tienda de una ubicación específica
     * @param location Ubicación de la tienda a remover
     */
    public void removeStore(int location) {
        stores.removeIf(store -> {
            if (store.getLocation() == location) {
                store.hide();
                return true;
            }
            return false;
        });
    }

    /**
     * Remueve un robot de una ubicación específica
     * @param location Ubicación del robot a remover
     */
    public void removeRobot(int location) {
        robots.removeIf(robot -> {
            if (robot.getLocation() == location) {
                robot.hide();
                return true;
            }
            return false;
        });
    }

    /**
     * Reabastecer todas las tiendas a su estado inicial
     */
    public void resupplyStores() {
        System.out.println("\n=== Reabasteciendo todas las tiendas ===");
        for (Store store : stores) {
            store.resupply();
        }
        System.out.println("Todas las tiendas han sido reabastecidas.");
    }

    /**
     * Retornar todos los robots a su posición inicial
     */
    public void returnRobots() {
        System.out.println("\n=== Retornando robots a posición inicial ===");
        for (Robot robot : robots) {
            robot.returnToStart();
            if (useSpiralMovement) {
                System.out.println("Robot retornado: " + robot.getSpiralInfo());
            }
        }
        System.out.println("Todos los robots han regresado a su posición inicial.");
    }

    /**
     * Reiniciar el estado completo de la simulación
     */
    public void reboot() {
        System.out.println("\n=== REINICIANDO SIMULACIÓN COMPLETA ===");

        totalProfit = 0;
        profitHistory.clear();
        isFinished = false;

        // Reiniciar robots
        for (Robot robot : robots) {
            robot.setLocation(0);
            robot.resetGanancia();
        }

        // Reabastecer tiendas
        resupplyStores();

        // Limpiar canvas y redibujar
        canvas.clear();
        mostrarCaminoEspiral(length, 120, 120, 15, 30, 30);

        // Mostrar elementos nuevamente
        if (visible) {
            makeVisible();
        }

        System.out.println("Simulación reiniciada completamente.");
        System.out.println("Modo espiral: " + (useSpiralMovement ? "ACTIVADO" : "DESACTIVADO"));
    }

    /**
     * Obtener la ganancia total actual
     * @return Ganancia total acumulada
     */
    public int profit() {
        return totalProfit;
    }

    /**
     * Obtener información de todas las tiendas
     * @return Matriz con [ubicación, tenges] para cada tienda
     */
    public int[][] stores() {
        int[][] result = new int[stores.size()][2];
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            result[i][0] = store.getLocation();
            result[i][1] = store.getTenges();
        }

        // Ordenar por ubicación
        Arrays.sort(result, (a, b) -> Integer.compare(a[0], b[0]));
        return result;
    }

    /**
     * Obtener tiendas que se han vaciado
     * @return Matriz con [ubicación, veces_vaciada] ordenada por ubicación
     */
    public int[][] emptiedStores() {
        ArrayList<int[]> emptied = new ArrayList<>();
        for (Store store : stores) {
            if (store.getDesocupaciones() > 0) {
                emptied.add(new int[]{store.getLocation(), store.getDesocupaciones()});
            }
        }

        // Convertir a matriz y ordenar por ubicación
        int[][] result = emptied.toArray(new int[emptied.size()][]);
        Arrays.sort(result, (a, b) -> Integer.compare(a[0], b[0]));
        return result;
    }

    /**
     * Obtener ubicaciones de todos los robots
     * @return Array con las ubicaciones de los robots
     */
    public int[] robots() {
        int[] result = new int[robots.size()];
        for (int i = 0; i < robots.size(); i++) {
            result[i] = robots.get(i).getLocation();
        }
        return result;
    }

    /**
     * Obtener historial de ganancias por movimiento
     * @return Matriz con [ubicación, ganancia_mov1, ganancia_mov2, ...] ordenada por ubicación
     */
    public int[][] profitPerMove() {
        Map<Integer, List<Integer>> profitsByLocation = new HashMap<>();

        for (Robot robot : robots) {
            int location = robot.getLocation();
            int profit = robot.getGanancia();
            profitsByLocation.computeIfAbsent(location, k -> new ArrayList<>()).add(profit);
        }

        // Convertir a matriz ordenada por ubicación
        int[][] result = new int[profitsByLocation.size()][];
        int index = 0;
        for (Map.Entry<Integer, List<Integer>> entry : profitsByLocation.entrySet()) {
            List<Integer> profits = entry.getValue();
            result[index] = new int[profits.size() + 1];
            result[index][0] = entry.getKey(); // ubicación
            for (int i = 0; i < profits.size(); i++) {
                result[index][i + 1] = profits.get(i);
            }
            index++;
        }
        Arrays.sort(result, (a, b) -> Integer.compare(a[0], b[0]));
        return result;
    }

    /**
     * Hacer visible la simulación
     */
    public void makeVisible() {
        visible = true;
        canvas.setVisible(true);

        for (Store store : stores) {
            store.show();
        }

        for (Robot robot : robots) {
            robot.show();
        }
    }

    /**
     * Hacer invisible la simulación
     */
    public void makeInvisible() {
        visible = false;

        for (Store store : stores) {
            store.hide();
        }

        for (Robot robot : robots) {
            robot.hide();
        }
    }

    /**
     * Finalizar la simulación
     */
    public void finish() {
        isFinished = true;
        makeInvisible();
        System.out.println("Simulación finalizada.");
    }

    /**
     * Verificar si el estado de la simulación es válido
     * @return true si todo está en orden, false si hay errores
     */
    public boolean ok() {
        // Verificar que no hay robots en la misma posición
        Set<Integer> robotPositions = new HashSet<>();
        for (Robot robot : robots) {
            int position = robot.getLocation();
            if (robotPositions.contains(position)) {
                return false; // Dos robots en la misma posición
            }
            robotPositions.add(position);
        }

        // Verificar que las ubicaciones están dentro del rango válido
        for (Robot robot : robots) {
            if (robot.getLocation() < 0 || robot.getLocation() >= length) {
                return false;
            }
        }

        for (Store store : stores) {
            if (store.getLocation() < 0 || store.getLocation() >= length) {
                return false;
            }
        }

        return !isFinished; // Válido si no ha terminado
    }

    // Método auxiliar para mostrar el camino en espiral (mantener funcionalidad original)
    private void mostrarCaminoEspiral(int lados, int x0, int y0, int grosor, int lonInicial, int incremento) {
        int x = x0;
        int y = y0;
        int dx = 1; // dirección horizontal: derecha
        int dy = 0; // dirección vertical
        int length = lonInicial;
        int dirCount = 0;

        for (int i = 0; i < lados; i++) {
            // Crear rectángulo usando la clase refactorizada
            Rectangle linea = new Rectangle();
            linea.changeColor("black");

            // Selecciona orientación y posiciona segmento
            if (dx != 0) { // horizontal
                linea.changeSize(grosor, length);
                if (dx == 1) { // derecha
                    linea.moveHorizontal(x - 70);
                    linea.moveVertical(y - 15);
                    x += length;
                } else { // izquierda
                    linea.moveHorizontal(x - length - 70);
                    linea.moveVertical(y - 15);
                    x -= length;
                }
            } else { // vertical
                linea.changeSize(length, grosor);
                if (dy == 1) { // abajo
                    linea.moveHorizontal(x - 70);
                    linea.moveVertical(y - 15);
                    y += length;
                } else { // arriba
                    linea.moveHorizontal(x - 70);
                    linea.moveVertical(y - length - 15);
                    y -= length;
                }
            }

            if (visible) {
                linea.makeVisible();
            }

            // Cambiar dirección
            int temp = dx;
            dx = -dy;
            dy = temp;

            // Incrementar longitud cada dos lados
            dirCount++;
            if (dirCount == 2) {
                dirCount = 0;
                length += incremento;
            }
        }
    }

    // Getters adicionales para compatibilidad y testing

    /**
     * Obtener la longitud de la ruta
     * @return Longitud de la ruta
     */
    public int getLength() {
        return length;
    }

    /**
     * Obtener una copia de todas las tiendas
     * @return ArrayList con todas las tiendas
     */
    public ArrayList<Store> getStores() {
        return new ArrayList<>(stores);
    }

    /**
     * Obtener una copia de todos los robots
     * @return ArrayList con todos los robots
     */
    public ArrayList<Robot> getRobots() {
        return new ArrayList<>(robots);
    }

    /**
     * Obtener el canvas actual
     * @return Instancia del canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Verificar si la simulación está visible
     * @return true si es visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Verificar si la simulación ha terminado
     * @return true si ha terminado
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Obtener el historial completo de ganancias
     * @return Lista con todas las ganancias registradas
     */
    public List<Integer> getProfitHistory() {
        return new ArrayList<>(profitHistory);
    }

    /**
     * Obtener el sistema de coordenadas en espiral
     * @return Instancia de SpiralPath
     */
    public SpiralPath getSpiralPath() {
        return spiralPath;
    }

    /**
     * Verificar si el movimiento en espiral está activado
     * @return true si está usando movimiento en espiral
     */
    public boolean isUsingSpiralMovement() {
        return useSpiralMovement;
    }

    /**
     * Estadísticas de la simulación incluyendo información de espiral
     * @return String con estadísticas detalladas
     */
    public String getStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE SILKROAD CON ESPIRAL ===\n");
        stats.append("Longitud de la ruta: ").append(length).append("\n");
        stats.append("Movimiento en espiral: ").append(useSpiralMovement ? "ACTIVADO" : "DESACTIVADO").append("\n");
        stats.append("Número de tiendas: ").append(stores.size()).append("\n");
        stats.append("Número de robots: ").append(robots.size()).append("\n");
        stats.append("Ganancia total: ").append(totalProfit).append("\n");
        stats.append("Movimientos realizados: ").append(profitHistory.size()).append("\n");

        if (!robots.isEmpty()) {
            int maxGanancia = robots.stream().mapToInt(Robot::getGanancia).max().orElse(0);
            int minGanancia = robots.stream().mapToInt(Robot::getGanancia).min().orElse(0);
            double avgGanancia = robots.stream().mapToInt(Robot::getGanancia).average().orElse(0.0);
            stats.append("Ganancia máxima por robot: ").append(maxGanancia).append("\n");
            stats.append("Ganancia mínima por robot: ").append(minGanancia).append("\n");
            stats.append("Ganancia promedio por robot: ").append(String.format("%.2f", avgGanancia)).append("\n");
        }

        long tiendasVacias = stores.stream().mapToLong(Store::getDesocupaciones).sum();
        stats.append("Total de vaciados de tiendas: ").append(tiendasVacias).append("\n");

        if (useSpiralMovement && spiralPath != null) {
            stats.append("Información de espiral: ").append(getSpiralInfo()).append("\n");
        }

        stats.append("Estado: ").append(isFinished ? "Finalizado" : "Activo").append("\n");

        return stats.toString();
    }
}