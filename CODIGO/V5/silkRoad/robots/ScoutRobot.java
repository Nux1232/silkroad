package silkRoad.robots;

import shapes.*;

/**
 * Robot explorador que puede ver tiendas futuras antes de moverse.
 * Toma decisiones más informadas al explorar el camino adelante.
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class ScoutRobot extends Robot {
    private static String[] coloresScout = {"magenta", "purple"};
    private static int contadorScout = 0;
    private int scoutRange = 3; // Puede ver 3 tiendas adelante

    /**
     * Constructor de robot explorador
     * @param location Ubicación inicial en la ruta
     * @param x Posición X inicial en el canvas
     * @param y Posición Y inicial en el canvas
     */
    public ScoutRobot(int location, int x, int y) {
        super(location, x, y);
        this.tipo = "Scout";
        
        // Mantener Rectangle pero más grande
        ((Rectangle)figure).changeSize(22, 22);
        
        // Asignar color intenso
        this.color = coloresScout[contadorScout % coloresScout.length];
        contadorScout++;
        figure.changeColor(color);
    }

    /**
     * Obtener el rango de exploración del robot
     * @return Número de tiendas que puede ver adelante
     */
    public int getScoutRange() {
        return scoutRange;
    }

    /**
     * Establecer el rango de exploración
     * @param range Nuevo rango de exploración
     */
    public void setScoutRange(int range) {
        this.scoutRange = Math.max(1, range);
    }

    @Override
    public String toString() {
        return String.format("ScoutRobot(loc=%d, ganancia=%d, scoutRange=%d)", 
            location, ganancia, scoutRange);
    }
}
