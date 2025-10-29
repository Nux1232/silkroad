package silkRoad.robots;

import shapes.*;

/**
 * Robot que nunca retrocede.
 * Solo acepta movimientos hacia adelante (ubicaciones mayores).
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class NeverbackRobot extends Robot {
    private static String[] coloresNeverback = {"red", "orange", "yellow"};
    private static int contadorNeverback = 0;

    /**
     * Constructor de robot que nunca retrocede
     * @param location Ubicación inicial en la ruta
     * @param x Posición X inicial en el canvas
     * @param y Posición Y inicial en el canvas
     */
    public NeverbackRobot(int location, int x, int y) {
        super(location, x, y);
        this.tipo = "Neverback";
        
        // Cambiar de Rectangle a Triangle para distinguir visualmente
        figure.makeInvisible();
        figure = new Triangle();
        ((Triangle)figure).changeSize(25, 25);
        int initialX = 60;
        int initialY = 50;
        figure.moveHorizontal(x - initialX);
        figure.moveVertical(y - initialY);
        
        // Asignar color cálido
        this.color = coloresNeverback[contadorNeverback % coloresNeverback.length];
        contadorNeverback++;
        figure.changeColor(color);
    }

    /**
     * Robot neverback solo puede moverse hacia adelante
     * @param newLocation Nueva ubicación propuesta
     * @return true solo si newLocation >= location actual
     */
    @Override
    public boolean canMoveTo(int newLocation) {
        return newLocation >= location;
    }

    @Override
    public String toString() {
        return String.format("NeverbackRobot(loc=%d, ganancia=%d, onlyForward=true)", 
            location, ganancia);
    }
}
