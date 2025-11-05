package silkRoad.robots;
import shapes.Rectangle;
import shapes.Triangle;

/**
 * Robot que nunca retrocede.
 * Solo acepta movimientos hacia adelante (ubicaciones mayores).
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
        Triangle triangulo = new Triangle();
        int initialX = x;
        int initialY = 50;
        triangulo.moveHorizontal(x - initialX);
        triangulo.moveVertical(y - initialY);
        
        // Asignar color cálido
        this.color = coloresNeverback[contadorNeverback % coloresNeverback.length];
        contadorNeverback++;
        triangulo.changeColor(color);   
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
