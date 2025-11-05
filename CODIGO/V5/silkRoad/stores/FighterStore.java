package silkRoad.stores;

import shapes.*;

/**
 * Tienda luchadora que solo permite ser robada por robots con más dinero.
 * Defiende su dinero de robots pobres.
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class FighterStore extends Store {
    private static String[] coloresFighter = {"darkGray", "black", "gray"};
    private static int contadorFighter = 0;

    /**
     * Constructor de tienda luchadora
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     * @param x Posición X en el canvas
     * @param y Posición Y en el canvas
     */
    public FighterStore(int location, int tenges, int x, int y) {
        super(location, tenges, x, y);
        this.tipo = "Fighter";
        
        // Cambiar de Circle a Rectangle para distinguir visualmente
        figure.makeInvisible();
        figure = new Rectangle();
        ((Rectangle)figure).changeSize(25, 25);
        int initialX = 60;
        int initialY = 50;
        figure.moveHorizontal(x - initialX);
        figure.moveVertical(y - initialY);
        
        // Asignar color oscuro
        colorBase = coloresFighter[contadorFighter % coloresFighter.length];
        contadorFighter++;
        figure.changeColor(colorBase);
    }

    /**
     * Verifica si el robot puede robar de esta tienda
     * Solo robots con MÁS dinero que la tienda pueden robar
     * @param robotMoney Cantidad de dinero del robot
     * @return true si el robot tiene más dinero que la tienda
     */
    @Override
    public boolean canBeRobbedBy(int robotMoney) {
        return robotMoney > tenges;
    }

    @Override
    public String toString() {
        return String.format("FighterStore(loc=%d, tenges=%d/%d, defensive=true)", 
            location, tenges, initialTenges);
    }
}
