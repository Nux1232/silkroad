package silkRoad.robots;

import shapes.Rectangle;
import shapes.Circle;

/**
 * Robot tierno que solo toma la mitad del dinero de las tiendas.
 * Es considerado con las tiendas y no las vacía completamente.
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class TenderRobot extends Robot {
    private static String[] coloresTender = {"pink", "lightGray", "white"};
    private static int contadorTender = 0;

    /**
     * Constructor de robot tierno
     * @param location Ubicación inicial en la ruta
     * @param x Posición X inicial en el canvas
     * @param y Posición Y inicial en el canvas
     */
    public TenderRobot(int location, int x, int y) {
        super(location, x, y);
        this.tipo = "Tender";
        
        // Cambiar de Rectangle a Circle para distinguir visualmente
        figure.makeInvisible();
        figure = new Circle();
        ((Circle)figure).changeSize(18);
        int initialX = 60;
        int initialY = 50;
        figure.moveHorizontal(x - initialX);
        figure.moveVertical(y - initialY);
        
        // Asignar color pastel
        this.color = coloresTender[contadorTender % coloresTender.length];
        contadorTender++;
        figure.changeColor(color);

    }

    /**
     * Robot tender solo toma la MITAD del dinero disponible
     * @param available Dinero disponible en la tienda
     * @return La mitad del dinero (redondeado hacia arriba)
     */
    @Override
    public int calculateTakeAmount(int available) {
        return (int) Math.ceil(available / 2.0);
    }

    @Override
    public String toString() {
        return String.format("TenderRobot(loc=%d, ganancia=%d, takesHalf=true)", 
            location, ganancia);
    }
}
