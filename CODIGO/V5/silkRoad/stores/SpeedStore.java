package silkRoad.stores;

import shapes.*;

/**
 * Tienda rápida que atiende sin costo de movimiento.
 * Los robots no pagan el costo de distancia al visitarla.
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class SpeedStore extends Store {
    private static String[] coloresSpeed = {"white", "lightGray"};
    private static int contadorSpeed = 0;

    /**
     * Constructor de tienda rápida
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     * @param x Posición X en el canvas
     * @param y Posición Y en el canvas
     */
    public SpeedStore(int location, int tenges, int x, int y) {
        super(location, tenges, x, y);
        this.tipo = "Speed";
        
        // Mantener Circle pero más pequeño
        ((Circle)figure).changeSize(20);
        
        // Asignar color brillante
        colorBase = coloresSpeed[contadorSpeed % coloresSpeed.length];
        contadorSpeed++;
        figure.changeColor(colorBase);
    }

    /**
     * Tienda rápida: no cobra costo de movimiento
     * @param distance Distancia al robot
     * @return 0 (sin costo de movimiento)
     */
    @Override
    public int getMovementCost(int distance) {
        return 0; // ¡Atención gratis!
    }

    @Override
    public String toString() {
        return String.format("SpeedStore(loc=%d, tenges=%d/%d, freeCost=true)", 
            location, tenges, initialTenges);
    }
}
