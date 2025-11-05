package silkRoad;

import silkRoad.stores.*;
import silkRoad.robots.*;
import shapes.*;
import java.util.*;

public class SilkRoad {
    // Atributos principales
    private int length;
    private ArrayList stores;
    private ArrayList robots;
    private Canvas canvas;
    private boolean visible;
    private int totalProfit;
    private int days;
    private boolean isFinished;
    private boolean lastOperationOk;
    private ProgressBar progressBar;

    // NUEVO: Parámetros para tablero CUADRADO
    private int boardSize;           // Tamaño del tablero (ej: 10x10, 15x15)
    private int cellSize = 100;       // Tamaño de cada celda
    private int boardX = 100;        // Posición X del tablero
    private int boardY = 100;        // Posición Y del tablero

    // Historial de ganancias por robot
    private HashMap robotProfitHistory;

    /**
     * Constructor que crea una ruta de longitud específica con tablero cuadrado
     * @param length Longitud de la ruta (número de posiciones en la espiral)
     */
    public SilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList();
        this.robots = new ArrayList();
        this.canvas = Canvas.getCanvas();
        this.visible = true;
        this.totalProfit = 0;
        this.isFinished = false;
        this.lastOperationOk = true;
        this.robotProfitHistory = new HashMap();

        // Calcular tamaño del tablero cuadrado necesario para la longitud
        this.boardSize = (int) Math.ceil(Math.sqrt(length)) + 2;

        // Dibujar el tablero cuadrado con camino espiral
        dibujarTableroConEspiral();

        // Crear barra de progreso
        progressBar = new ProgressBar(50, 800, 400, 30);
        if (visible) {
            progressBar.makeVisible();
        }
    }

    /**
     * Constructor que crea una ruta basada en días de simulación
     * @param days Matriz de días con información de simulación
     */
    public SilkRoad(int[][] days) {
        this(100); // Longitud por defecto
        this.days = days.length;
        lastOperationOk = true;
    }

    /**
     * NUEVO: Dibujar el tablero cuadrado con líneas de cuadrícula y camino espiral
     */
    private void dibujarTableroConEspiral() {
        // Dibujar borde del tablero
        Rectangle borde = new Rectangle();
        borde.changeSize(boardSize * cellSize, boardSize * cellSize);
        borde.moveHorizontal(boardX);
        borde.moveVertical(boardY);
        borde.changeColor("black");
        if (visible) {
            borde.makeVisible();
        }

        // Dibujar cuadrícula
        for (int i = 0; i <= boardSize; i++) {
            // Líneas horizontales
            Rectangle lineaH = new Rectangle();
            lineaH.changeSize(boardSize * cellSize, 2);
            lineaH.moveHorizontal(boardX);
            lineaH.moveVertical(boardY + i * cellSize);
            lineaH.changeColor("lightGray");
            if (visible) lineaH.makeVisible();

            // Líneas verticales
            Rectangle lineaV = new Rectangle();
            lineaV.changeSize(2, boardSize * cellSize);
            lineaV.moveHorizontal(boardX + i * cellSize);
            lineaV.moveVertical(boardY);
            lineaV.changeColor("lightGray");
            if (visible) lineaV.makeVisible();
        }

        // Marcar el camino espiral en color
        marcarCaminoEspiral();
    }

    /**
     * NUEVO: Marcar visualmente el camino espiral en el tablero
     */
    private void marcarCaminoEspiral() {
        for (int i = 0; i < Math.min(length, boardSize * boardSize); i++) {
            int[] coords = getSpiralCoordinates(i);
            int x = coords[0];  // Primer elemento = coordenada X
            int y = coords[1];  // Segundo elemento = coordenada Y
            
            Rectangle celda = new Rectangle();
            celda.changeSize(cellSize - 4, cellSize - 4);
            celda.moveHorizontal(boardX + x * cellSize + 2);
            celda.moveVertical(boardY + y * cellSize + 2);
            celda.changeColor("yellow");
            if (visible) celda.makeVisible();
        }
    }

    /**
     * NUEVO: Calcular coordenadas (x, y) en el tablero para una posición en la espiral
     * @param position Posición en la espiral (0, 1, 2, ...)
     * @return Array [x, y] con coordenadas de cuadrícula
     */
    private int[] getSpiralCoordinates(int position) {
        if (position == 0) {
            return new int[]{boardSize / 2, boardSize / 2}; // Centro del tablero
        }

        // Algoritmo de espiral: derecha, abajo, izquierda, arriba
        int x = boardSize / 2;
        int y = boardSize / 2;
        int steps = 1;
        int stepCount = 0;
        int direction = 0; // 0=derecha, 1=abajo, 2=izquierda, 3=arriba
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 1; i <= position; i++) {
            x += dx[direction];
            y += dy[direction];
            stepCount++;

            if (stepCount == steps) {
                stepCount = 0;
                direction = (direction + 1) % 4;
                if (direction % 2 == 0) {
                    steps++;
                }
            }
        }

        return new int[]{x, y};
    }

    /**
     * MODIFICADO: Calcular posición X en píxeles para una ubicación en la espiral
     * @param location Ubicación en la ruta
     * @return Posición X en píxeles
     */
    private int calculateSpiralX(int location) {
        int[] coords = getSpiralCoordinates(location);
        return boardX + coords[0] * cellSize + cellSize / 2;  // coords[0] en lugar de coords
    }

    /**
     * MODIFICADO: Calcular posición Y en píxeles para una ubicación en la espiral
     * @param location Ubicación en la ruta
     * @return Posición Y en píxeles
     */
    private int calculateSpiralY(int location) {
        int[] coords = getSpiralCoordinates(location);
        return boardY + coords[1] * cellSize + cellSize / 2;  // coords[1] en lugar de coords
    }

    /**
     * MODIFICADO: Coloca una tienda en una ubicación específica
     * Ahora soporta diferentes tipos de tiendas
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     */
    public void placeStore(int location, int tenges) {
        placeStore(location, tenges, "normal");
    }

    /**
     * NUEVO: Coloca una tienda de un tipo específico
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     * @param type Tipo de tienda: "normal", "autonomous", "fighter", "speed"
     */
    public void placeStore(int location, int tenges, String type) {
        if (location < 0 || location >= length) {
            lastOperationOk = false;
            showError("Ubicación inválida para tienda: " + location);
            return;
        }

        removeStore(location);

        int x = calculateSpiralX(location);
        int y = calculateSpiralY(location);

        Store newStore;
        switch (type.toLowerCase()) {
            case "autonomous":
                newStore = new AutonomousStore(location, tenges, x, y);
                break;
            case "fighter":
                newStore = new FighterStore(location, tenges, x, y);
                break;
            case "speed":
                newStore = new SpeedStore(location, tenges, x, y);
                break;
            default:
                newStore = new Store(location, tenges, x, y);
        }

        stores.add(newStore);
        if (visible) {
            newStore.show();
        }
        lastOperationOk = true;
    }

    /**
     * MODIFICADO: Coloca un robot en una ubicación específica
     * Ahora soporta diferentes tipos de robots
     * @param location Ubicación inicial del robot
     */
    public void placeRobot(int location) {
        placeRobot(location, "normal");
    }

    /**
     * @param location Ubicación inicial del robot
     * @param type Tipo de robot: "normal", "neverback", "tender", "scout"
     */
    public void placeRobot(int location, String type) {
        if (location < 0 || location >= length) {
            lastOperationOk = false;
            showError("Ubicación inválida para robot: " + location);
            return;
        }

        // Verificar que no haya otro robot en la misma ubicación inicial
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            if (robot.getInitialLocation() == location) {
                lastOperationOk = false;
                showError("Ya existe un robot en la ubicación: " + location);
                return;
            }
        }

        int x = calculateSpiralX(location);
        int y = calculateSpiralY(location);

        Robot newRobot;
        switch (type.toLowerCase()) {
            case "neverback":
                newRobot = new NeverbackRobot(location, x, y);
                break;
            case "tender":
                newRobot = new TenderRobot(location, x, y);
                break;
            case "scout":
                newRobot = new ScoutRobot(location, x, y);
                break;
            default:
                newRobot = new Robot(location, x, y);
        }

        robots.add(newRobot);
        robotProfitHistory.put(new Integer(location), new ArrayList());
        if (visible) {
            newRobot.show();
        }
        lastOperationOk = true;
    }

    /**
     * MODIFICADO: Mueve un robot específico una cantidad de metros
     * Ahora verifica restricciones según el tipo de robot
     */
    public void moveRobot(int location, int meters) {
        Robot robotToMove = null;
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            if (robot.getLocation() == location) {
                robotToMove = robot;
                break;
            }
        }

        if (robotToMove == null) {
            lastOperationOk = false;
            if (visible) {
                System.err.println("ERROR: No hay robot en ubicación " + location);
            }
            return;
        }

        int newLocation = location + meters;

        // NUEVO: Verificar si el robot puede moverse a la nueva ubicación
        if (!robotToMove.canMoveTo(newLocation)) {
            lastOperationOk = false;
            if (visible) {
                System.err.println("ERROR: Robot tipo " + robotToMove.getTipo() +
                        " no puede moverse a ubicación " + newLocation);
            }
            return;
        }

        if (newLocation < 0 || newLocation >= length) {
            lastOperationOk = false;
            if (visible) {
                System.err.println("ERROR: Fuera de límites [0-" + (length-1) + "]");
            }
            return;
        }

        robotToMove.setLocation(newLocation);
        int newX = calculateSpiralX(newLocation);
        int newY = calculateSpiralY(newLocation);
        robotToMove.moveTo(newX, newY);

        int profit = interactWithStore(robotToMove, newLocation, Math.abs(meters));

        Integer key = new Integer(robotToMove.getInitialLocation());
        ArrayList history = (ArrayList) robotProfitHistory.get(key);
        if (history == null) {
            history = new ArrayList();
            robotProfitHistory.put(key, history);
        }
        history.add(new Integer(profit));

        if (progressBar != null && visible) {
            updateProgressBar();
        }

        lastOperationOk = true;
    }

    /**
     * MODIFICADO: Interacción entre robot y tienda
     * Ahora considera tipos especiales de robots y tiendas
     */
    private int interactWithStore(Robot robot, int location, int distance) {
        int movementCost = distance;
        int gained = 0;

        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            if (store.getLocation() == location && !store.isEmpty()) {
                // NUEVO: Verificar si la tienda permite ser robada por este robot
                if (!store.canBeRobbedBy(robot.getGanancia())) {
                    // Tienda Fighter rechaza al robot pobre
                    if (visible) {
                        System.out.println("¡Tienda Fighter rechaza a robot! " +
                                "Robot tiene " + robot.getGanancia() +
                                " pero tienda tiene " + store.getTenges());
                    }
                    break;
                }

                // NUEVO: Calcular cuánto tomar según el tipo de robot
                int available = store.getTenges();
                int toTake = robot.calculateTakeAmount(available);

                gained = store.reduceTenges(toTake);
                robot.addGanancia(gained);

                // NUEVO: Calcular costo de movimiento según tipo de tienda
                movementCost = store.getMovementCost(distance);

                if (store.getTenges() == 0) {
                    store.marcarDesocupada();
                }
                break;
            }
        }

        int netProfit = gained - movementCost;
        totalProfit += netProfit;
        return netProfit;
    }

    // Métodos auxiliares que faltan
    private void showError(String message) {
        if (visible) {
            System.err.println("ERROR: " + message);
        }
    }

    private void removeStore(int location) {
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            if (store.getLocation() == location) {
                stores.remove(i);
                break;
            }
        }
    }

    private void updateProgressBar() {
        if (progressBar != null) {
            int progress = (int) ((double) totalProfit / 1000 * 100); // Ajusta según tu lógica
            progressBar.setCurrentValue(progress);
        }
    }
        
        /**
     * Hace visible la simulación completa (canvas y barra de progreso)
     */
    public void makeVisible() {
        this.visible = true;
        if (canvas != null) {
            canvas.isVisible();
        }
        if (progressBar != null) {
            progressBar.makeVisible();
        }
        // Hacer visibles todos los robots y tiendas
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            robot.show();
        }
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            store.show();
        }
    }
    
    /**
     * Reabastecer todas las tiendas a sus valores iniciales
     */
    public void resupplyStores() {
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            // Aquí asumiendo que Store tiene un método para reabastecer
            // Si no existe, puedes crear una nueva tienda con los mismos datos
            System.out.println("Reabasteciendo tienda en ubicación " + store.getLocation());
            // store.resupply();  // Si este método existe en Store
        }
    }
    
    /**
     * Retorna todos los robots a sus ubicaciones iniciales
     */
    public void returnRobots() {
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            int initialLocation = robot.getInitialLocation();
            int currentLocation = robot.getLocation();
            
            if (currentLocation != initialLocation) {
                System.out.println("Retornando robot de ubicación " + currentLocation + 
                                 " a ubicación inicial " + initialLocation);
                
                int x = calculateSpiralX(initialLocation);
                int y = calculateSpiralY(initialLocation);
                robot.setLocation(initialLocation);
                robot.moveTo(x, y);
            }
        }
    }
    
    /**
     * Finaliza la simulación (limpia recursos)
     */
    public void finish() {
        System.out.println("\nFinalizando simulación...");
        System.out.println("Ganancia total obtenida: " + totalProfit);
        
        // Hacer invisibles todos los elementos
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            robot.hide();
        }
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            store.hide();
        }
        
        if (progressBar != null) {
            progressBar.makeInvisible();
        }
        
        this.visible = false;
        System.out.println("Simulación finalizada.");
    }

    
    
}
