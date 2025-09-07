import java.util.ArrayList;
import java.util.Iterator;

/**
 * Clase que representa la ruta SilkRoad con tiendas, robots y visualización en un canvas.
 */
public class SilkRoad {
    private int length;
    private ArrayList<Store> stores;
    private ArrayList<Robot> robots;
    private Canvas canvas;
    private boolean visible;
    private int profit;

    // Barra de progreso horizontal
    private Rectangle barraProgreso;

    /**
     * Constructor que inicializa la ruta con una longitud dada.
     * @param length Longitud de la ruta.
     */
    public SilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.canvas = Canvas.getCanvas();
        this.visible = true;
        this.profit = 0;
    }

    /**
     * Coloca una tienda en una ubicación dada con cantidad inicial de tenges.
     * @param location Posición en la ruta.
     * @param tenges Cantidad inicial de tenges de la tienda.
     */
    public void placeStore(int location, int tenges) {
        int[] coords = calculateXY(location);
        Store s = new Store(location, tenges, coords[0], coords[1]);
        stores.add(s);
        if (visible) s.show();
    }

    /**
     * Remueve una tienda de la ubicación especificada.
     * Corrige el uso incorrecto de eliminación durante la iteración de la lista.
     * @param location Posición en la ruta de la tienda a eliminar.
     */
    public void removeStore(int location) {
        Iterator<Store> it = stores.iterator();
        while (it.hasNext()) {
            Store s = it.next();
            if (s.getLocation() == location) {
                s.hide();
                it.remove();  // eliminación segura con iterador
                break;
            }
        }
    }

    /**
     * Coloca un robot en una ubicación dada.
     * @param location Posición inicial del robot.
     */
    public void placeRobot(int location) {
        int[] coords = calculateXY(location);
        Robot r = new Robot(location, coords[0], coords[1]);
        robots.add(r);
        if (visible) r.show();
    }

    /**
     * Remueve un robot de la ubicación especificada.
     * Corrige la eliminación segura durante la iteración.
     * @param location Posición en la ruta del robot a eliminar.
     */
    public void removeRobot(int location) {
        Iterator<Robot> it = robots.iterator();
        while (it.hasNext()) {
            Robot r = it.next();
            if (r.getLocation() == location) {
                r.hide();
                it.remove();  // eliminación segura con iterador
                break;
            }
        }
    }

    /**
     * Mueve un robot de una ubicación por una cantidad de metros.
     * Realiza movimiento cíclico dentro de la longitud de la ruta.
     * @param location Ubicación actual del robot.
     * @param meters Cantidad de metros a mover (puede ser negativo).
     */
    public void moveRobot(int location, int meters) {
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                int newLoc = (location + meters) % length;
                if (newLoc < 0) newLoc += length;
                int[] coords = calculateXY(newLoc);
                r.moveTo(coords[0], coords[1]);
                r.setLocation(newLoc);
                break;
            }
        }
    }

    /**
     * Calcula las coordenadas (x,y) en pantalla para la posición dada.
     * Actualmente usa un patrón de cuadrícula (no una espiral real).
     * @param pos Posición en la ruta.
     * @return arreglo con coordenadas x e y.
     */
    private int[] calculateXY(int pos) {
        int spacing = 60;
        int x = 100 + (pos % 10) * spacing;
        int y = 100 + (pos / 10) * spacing;
        return new int[]{x, y};
    }

    /**
     * Resurtido de todas las tiendas.
     */
    public void resupplyStores() {
        for (Store s : stores) s.resupply();
    }

    /**
     * Hace visibles todas las tiendas y robots.
     */
    public void makeVisible() {
        visible = true;
        for (Store s : stores) s.show();
        for (Robot r : robots) r.show();
    }

    /**
     * Hace invisibles todas las tiendas y robots.
     */
    public void makeInvisible() {
        visible = false;
        for (Store s : stores) s.hide();
        for (Robot r : robots) r.hide();
    }

    /**
     * Finaliza la simulación ocultando y limpiando tiendas y robots.
     */
    public void finish() {
        for (Store s : stores) s.hide();
        for (Robot r : robots) r.hide();
        stores.clear();
        robots.clear();
    }

    /**
     * Muestra la barra de progreso horizontal según el porcentaje dado.
     * @param porcentaje Valor entre 0.0 y 1.0 que indica el progreso.
     */
    public void mostrarBarraProgreso(double porcentaje) {
        if (barraProgreso == null) {
            barraProgreso = new Rectangle();
            barraProgreso.changeColor("green");
            // Ubicación fija para barra horizontal (ajustable)
            barraProgreso.moveHorizontal(100); // mueve a la derecha
            barraProgreso.moveVertical(300);    // mueve hacia abajo
            barraProgreso.makeVisible(); // imprescindible para mostrar
        }
        // Cambiar ancho para representar el progreso; altura fija pequeña
        barraProgreso.changeSize((int)(porcentaje * 500), 30);
    }
}
