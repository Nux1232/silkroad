package silkRoad.stores;

import shapes.*;

/**
 * Tienda autónoma que escoge su propia posición.
 * No acepta la ubicación indicada, sino que escoge una cercana.
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class AutonomousStore extends Store {
    private int suggestedLocation;
    private static String[] coloresAutonomous = {"blue", "cyan", "purple"};
    private static int contadorAutonomous = 0;

    /**
     * Constructor de tienda autónoma
     * @param suggestedLocation Ubicación sugerida (será modificada)
     * @param tenges Cantidad inicial de tenges
     * @param x Posición X sugerida en el canvas
     * @param y Posición Y sugerida en el canvas
     */
    public AutonomousStore(int suggestedLocation, int tenges, int x, int y) {
        super(chooseOwnLocation(suggestedLocation), tenges, x, y);
        this.suggestedLocation = suggestedLocation;
        this.tipo = "Autonomous";
        
        // Cambiar de Circle a Triangle para distinguir visualmente
        figure.makeInvisible();
        figure = new Triangle();
        ((Triangle)figure).changeSize(35, 35);
        int initialX = 60;
        int initialY = 50;
        figure.moveHorizontal(x - initialX);
        figure.moveVertical(y - initialY);
        
        // Asignar color frío
        colorBase = coloresAutonomous[contadorAutonomous % coloresAutonomous.length];
        contadorAutonomous++;
        figure.changeColor(colorBase);
    }

    /**
     * La tienda escoge su propia ubicación cercana a la sugerida
     * @param suggested Ubicación sugerida
     * @return Ubicación escogida autónomamente
     */
    private static int chooseOwnLocation(int suggested) {
        // Escoge una ubicación entre -2 y +2 de la sugerida
        int offset = (int) (Math.random() * 5) - 2; // -2, -1, 0, 1, 2
        return Math.max(0, suggested + offset);
    }

    public int getSuggestedLocation() {
        return suggestedLocation;
    }

    @Override
    public String toString() {
        return String.format("AutonomousStore(suggested=%d, actual=%d, tenges=%d/%d)", 
            suggestedLocation, location, tenges, initialTenges);
    }
}
