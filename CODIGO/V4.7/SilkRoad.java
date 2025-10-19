import java.util.*;

/**
 * Simulador de la Ruta de la Seda con robots
 *
 */
public class SilkRoad {
    // Atributos principales
    private int length;
    private ArrayList stores;      
    private ArrayList robots;      
    private Canvas canvas;
    private boolean visible;
    private int totalProfit;
    private int[][] days;
    private boolean isFinished;
    private boolean lastOperationOk;
    private ProgressBar progressBar;
    
    // Parámetros de la espiral centrada
    private int centerX = 600;
    private int centerY = 450;
    
    // Historial de ganancias por robot (sin genéricos para BlueJ)
    private HashMap robotProfitHistory;

    /**
     * Constructor que crea una ruta de longitud específica
     * @param length Longitud de la ruta
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
        
        // Dibujar la espiral centrada
        mostrarCaminoEspiral(length, centerX, centerY, 10, 30, 30);
        
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
        this.days = days;
        lastOperationOk = true;
    }

    /**
     * Coloca una tienda en una ubicación específica
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     */
    public void placeStore(int location, int tenges) {
        if (location < 0 || location >= length) {
            lastOperationOk = false;
            showError("Ubicación inválida para tienda: " + location);
            return;
        }
        
        removeStore(location);
        int x = calculateSpiralX(location);
        int y = calculateSpiralY(location);
        Store newStore = new Store(location, tenges, x, y);
        stores.add(newStore);
        
        if (visible) {
            newStore.show();
        }
        lastOperationOk = true;
    }

    /**
     * Remueve una tienda de una ubicación específica
     * @param location Ubicación de la tienda a remover
     */
    public void removeStore(int location) {
        Iterator it = stores.iterator();
        while (it.hasNext()) {
            Store store = (Store) it.next();  // Cast explícito
            if (store.getLocation() == location) {
                store.hide();
                it.remove();
            }
        }
        lastOperationOk = true;
    }

    /**
     * Coloca un robot en una ubicación específica
     * @param location Ubicación inicial del robot
     */
    public void placeRobot(int location) {
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
                showError("Ya existe un robot en la ubicación " + location);
                return;
            }
        }
        
        int x = calculateSpiralX(location);
        int y = calculateSpiralY(location);
        Robot newRobot = new Robot(location, x, y);
        robots.add(newRobot);
        robotProfitHistory.put(new Integer(location), new ArrayList());
        
        if (visible) {
            newRobot.show();
        }
        lastOperationOk = true;
    }

    /**
     * Remueve un robot de una ubicación específica
     * @param location Ubicación del robot a remover
     */
    public void removeRobot(int location) {
        Robot toRemove = null;
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            if (robot.getLocation() == location) {
                toRemove = robot;
                break;
            }
        }
        
        if (toRemove != null) {
            toRemove.hide();
            robots.remove(toRemove);
            lastOperationOk = true;
        } else {
            lastOperationOk = false;
            showError("No hay robot en la ubicación " + location);
        }
    }

    /**
     * Mueve todos los robots automáticamente
     */
    public void moveRobots() {
        if (robots.isEmpty() || stores.isEmpty()) {
            lastOperationOk = true;
            return;
        }
        
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            int currentLoc = robot.getLocation();
            
            int bestStoreLoc = findBestStoreForRobot(robot);
            
            if (bestStoreLoc != -1 && bestStoreLoc != currentLoc) {
                int meters = bestStoreLoc - currentLoc;
                moveRobot(currentLoc, meters);
            }
        }
        
        if (progressBar != null && visible) {
            updateProgressBar();
        }
        
        // Destacar robot ganador al final
        destacarRobotGanador();
        
        lastOperationOk = true;
    }

    /**
     * Mueve un robot específico una cantidad de metros
     * @param location Ubicación ACTUAL del robot
     * @param meters Cantidad de metros a mover
     */
    public void moveRobot(int location, int meters) {
        // Buscar robot
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
        
        // Calcular nueva ubicación
        int newLocation = location + meters;
        
        // Verificar límites
        if (newLocation < 0 || newLocation >= length) {
            lastOperationOk = false;
            if (visible) {
                System.err.println("ERROR: Fuera de límites (0-" + (length-1) + ")");
            }
            return;
        }
        
        // Actualizar ubicación lógica
        robotToMove.setLocation(newLocation);
        
        // Calcular posición visual
        int newX = calculateSpiralX(newLocation);
        int newY = calculateSpiralY(newLocation);
        
        // Mover visualmente
        robotToMove.moveTo(newX, newY);
        
        // Interactuar con tienda
        int profit = interactWithStore(robotToMove, newLocation, Math.abs(meters));
        
        // Registrar ganancia
        Integer key = new Integer(robotToMove.getInitialLocation());
        ArrayList history = (ArrayList) robotProfitHistory.get(key);
        if (history == null) {
            history = new ArrayList();
            robotProfitHistory.put(key, history);
        }
        history.add(new Integer(profit));
        
        // Actualizar barra
        if (progressBar != null && visible) {
            updateProgressBar();
        }
        
        lastOperationOk = true;
    }

    /**
     * Interacción entre robot y tienda
     */
    private int interactWithStore(Robot robot, int location, int distance) {
        int movementCost = distance;
        int gained = 0;
        
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            if (store.getLocation() == location && !store.isEmpty()) {
                gained = store.getTenges();
                robot.addGanancia(gained);
                store.reduceTenges(gained);
                
                if (store.getTenges() <= 0) {
                    store.marcarDesocupada();
                }
                break;
            }
        }
        
        int netProfit = gained - movementCost;
        totalProfit += netProfit;
        return netProfit;
    }

    /**
     * Encuentra la mejor tienda para un robot
     */
    private int findBestStoreForRobot(Robot robot) {
        int robotLoc = robot.getLocation();
        int bestStoreLoc = -1;
        int maxProfit = 0;
        
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            if (!store.isEmpty()) {
                int distance = Math.abs(store.getLocation() - robotLoc);
                int profit = store.getTenges() - distance;
                
                if (profit > maxProfit) {
                    maxProfit = profit;
                    bestStoreLoc = store.getLocation();
                }
            }
        }
        
        return bestStoreLoc;
    }

    /**
     * Hace parpadear al robot con mayor ganancia
     */
    private void destacarRobotGanador() {
        if (robots.isEmpty()) return;
        
        // Encontrar robot con mayor ganancia
        Robot ganador = null;
        int maxGanancia = Integer.MIN_VALUE;
        
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            if (robot.getGanancia() > maxGanancia) {
                maxGanancia = robot.getGanancia();
                ganador = robot;
            }
        }
        
        if (ganador == null || maxGanancia <= 0) return;
        
        final Robot robotGanador = ganador;
        
        // Thread para parpadear
        Thread parpadeo = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 6; i++) {
                        robotGanador.hide();
                        Thread.sleep(300);
                        robotGanador.show();
                        Thread.sleep(300);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        parpadeo.start();
    }

    /**
     * Reabastecer todas las tiendas
     */
    public void resupplyStores() {
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            store.resupply();
        }
        lastOperationOk = true;
    }

    /**
     * Retornar robots a posiciones iniciales
     */
    public void returnRobots() {
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            robot.returnToStart();
            int x = calculateSpiralX(robot.getLocation());
            int y = calculateSpiralY(robot.getLocation());
            robot.moveTo(x, y);
        }
        lastOperationOk = true;
    }

    /**
     * Reiniciar la simulación
     */
    public void reboot() {
        totalProfit = 0;
        isFinished = false;
        robotProfitHistory.clear();
        
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            robot.returnToStart();
            robot.resetGanancia();
            int x = calculateSpiralX(robot.getLocation());
            int y = calculateSpiralY(robot.getLocation());
            robot.moveTo(x, y);
            robotProfitHistory.put(new Integer(robot.getInitialLocation()), new ArrayList());
        }
        
        resupplyStores();
        
        // Redibujar
        canvas.clear();
        mostrarCaminoEspiral(length, centerX, centerY, 10, 30, 30);
        
        if (visible) {
            for (int i = 0; i < stores.size(); i++) {
                Store store = (Store) stores.get(i);
                store.show();
            }
            for (int i = 0; i < robots.size(); i++) {
                Robot robot = (Robot) robots.get(i);
                robot.show();
            }
            if (progressBar != null) {
                progressBar.makeVisible();
            }
        }
        
        // Resetear barra
        if (progressBar != null) {
            progressBar.updateValues(0, 100);
        }
        
        lastOperationOk = true;
    }

    /**
     * Obtener ganancia total
     */
    public int profit() {
        return totalProfit;
    }

    /**
     * Obtener información de tiendas
     */
    public int[][] stores() {
        int[][] result = new int[stores.size()][2];
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            result[i][0] = store.getLocation();
            result[i][1] = store.getTenges();
        }
        Arrays.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                int[] a = (int[]) o1;
                int[] b = (int[]) o2;
                return Integer.compare(a[0], b[0]);
            }
        });
        return result;
    }

    /**
     * Obtener tiendas desocupadas
     */
    public int[][] emptiedStores() {
        ArrayList emptied = new ArrayList();
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            if (store.getDesocupaciones() > 0) {
                emptied.add(new int[]{store.getLocation(), store.getDesocupaciones()});
            }
        }
        int[][] result = (int[][]) emptied.toArray(new int[emptied.size()][]);
        Arrays.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                int[] a = (int[]) o1;
                int[] b = (int[]) o2;
                return Integer.compare(a[0], b[0]);
            }
        });
        return result;
    }

    /**
     * Obtener información de robots
     */
    public int[][] robots() {
        int[][] result = new int[robots.size()][2];
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            result[i][0] = robot.getLocation();
            result[i][1] = robot.getGanancia();
        }
        Arrays.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                int[] a = (int[]) o1;
                int[] b = (int[]) o2;
                return Integer.compare(a[0], b[0]);
            }
        });
        return result;
    }

    /**
     * Obtener ganancias por movimiento
     */
    public int[][] profitPerMove() {
        ArrayList result = new ArrayList();
        
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            Integer key = new Integer(robot.getInitialLocation());
            ArrayList history = (ArrayList) robotProfitHistory.get(key);
            if (history == null) history = new ArrayList();
            
            int[] robotData = new int[1 + history.size()];
            robotData[0] = robot.getInitialLocation();
            for (int j = 0; j < history.size(); j++) {
                robotData[j + 1] = ((Integer) history.get(j)).intValue();
            }
            result.add(robotData);
        }
        
        int[][] finalResult = (int[][]) result.toArray(new int[result.size()][]);
        Arrays.sort(finalResult, new Comparator() {
            public int compare(Object o1, Object o2) {
                int[] a = (int[]) o1;
                int[] b = (int[]) o2;
                return Integer.compare(a[0], b[0]);
            }
        });
        return finalResult;
    }

    /**
     * Hacer visible
     */
    public void makeVisible() {
        visible = true;
        canvas.setVisible(true);
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            store.show();
        }
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            robot.show();
        }
        if (progressBar != null) {
            progressBar.makeVisible();
        }
        lastOperationOk = true;
    }

    /**
     * Hacer invisible
     */
    public void makeInvisible() {
        visible = false;
        for (int i = 0; i < stores.size(); i++) {
            Store store = (Store) stores.get(i);
            store.hide();
        }
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            robot.hide();
        }
        if (progressBar != null) {
            progressBar.makeInvisible();
        }
        lastOperationOk = true;
    }

    /**
     * Finalizar simulador
     */
    public void finish() {
        isFinished = true;
        makeInvisible();
        lastOperationOk = true;
    }

    /**
     * Verificar última operación
     */
    public boolean ok() {
        return lastOperationOk && !isFinished;
    }

    /**
     * Mostrar error
     */
    private void showError(String message) {
        if (visible) {
            System.err.println("ERROR: " + message);
        }
    }

    /**
     * Calcular X en espiral
     */
    private int calculateSpiralX(int location) {
        if (location == 0) return centerX;
        
        int ring = (int) Math.ceil((Math.sqrt(location) - 1) / 2);
        int ringStart = 4 * ring * ring;
        int positionInRing = location - ringStart;
        int sideLength = 2 * ring + 1;
        
        int x = centerX;
        
        if (positionInRing < sideLength) {
            x = centerX + (ring * 30);
        } else if (positionInRing < 2 * sideLength - 1) {
            x = centerX + (ring * 30) - ((positionInRing - sideLength + 1) * 30);
        } else if (positionInRing < 3 * sideLength - 2) {
            x = centerX - (ring * 30);
        } else {
            x = centerX - (ring * 30) + ((positionInRing - 3 * sideLength + 3) * 30);
        }
        
        return x;
    }

    /**
     * Calcular Y en espiral
     */
    private int calculateSpiralY(int location) {
        if (location == 0) return centerY;
        
        int ring = (int) Math.ceil((Math.sqrt(location) - 1) / 2);
        int ringStart = 4 * ring * ring;
        int positionInRing = location - ringStart;
        int sideLength = 2 * ring + 1;
        
        int y = centerY;
        
        if (positionInRing < sideLength) {
            y = centerY - (ring * 30) + (positionInRing * 30);
        } else if (positionInRing < 2 * sideLength - 1) {
            y = centerY + (ring * 30);
        } else if (positionInRing < 3 * sideLength - 2) {
            y = centerY + (ring * 30) - ((positionInRing - 2 * sideLength + 2) * 30);
        } else {
            y = centerY - (ring * 30);
        }
        
        return y;
    }

    /**
     * Dibujar espiral
     */
    private void mostrarCaminoEspiral(int lados, int x0, int y0, int grosor, int lonInicial, int incremento) {
        int x = x0;
        int y = y0;
        int dx = 1;
        int dy = 0;
        int length = lonInicial;
        int dirCount = 0;

        for (int i = 0; i < lados; i++) {
            Rectangle linea = new Rectangle();
            linea.changeColor("black");
            
            if (dx != 0) {
                linea.changeSize(grosor, length);
                if (dx == 1) {
                    linea.moveHorizontal(x - 60);
                    linea.moveVertical(y - 50);
                    x += length;
                } else {
                    linea.moveHorizontal(x - length - 60);
                    linea.moveVertical(y - 50);
                    x -= length;
                }
            } else {
                linea.changeSize(length, grosor);
                if (dy == 1) {
                    linea.moveHorizontal(x - 60);
                    linea.moveVertical(y - 50);
                    y += length;
                } else {
                    linea.moveHorizontal(x - 60);
                    linea.moveVertical(y - length - 50);
                    y -= length;
                }
            }
            
            if (visible) {
                linea.makeVisible();
            }

            int temp = dx;
            dx = -dy;
            dy = temp;
            
            dirCount++;
            if (dirCount == 2) {
                dirCount = 0;
                length += incremento;
            }
        }
    }

    /**
     * Actualizar barra de progreso
     */
    private void updateProgressBar() {
        if (progressBar == null || !visible) {
            return;
        }
        
        int currentProfit = totalProfit;
        int maxPossibleProfit = calculateMaxPossibleProfit();
        
        progressBar.updateValues(currentProfit, maxPossibleProfit);
    }

    /**
     * Calcular ganancia máxima posible
     */
    private int calculateMaxPossibleProfit() {
        if (robots.isEmpty() || stores.isEmpty()) {
            return Math.max(1, totalProfit);
        }
        
        int potentialProfit = 0;
        
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = (Robot) robots.get(i);
            int robotLoc = robot.getLocation();
            int bestForThisRobot = 0;
            
            for (int j = 0; j < stores.size(); j++) {
                Store store = (Store) stores.get(j);
                if (!store.isEmpty()) {
                    int distance = Math.abs(store.getLocation() - robotLoc);
                    int profit = store.getTenges() - distance;
                    if (profit > bestForThisRobot) {
                        bestForThisRobot = profit;
                    }
                }
            }
            
            potentialProfit += Math.max(0, bestForThisRobot);
        }
        
        return Math.max(1, totalProfit + potentialProfit);
    }
}
