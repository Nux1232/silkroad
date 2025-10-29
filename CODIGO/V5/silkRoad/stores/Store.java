package silkRoad.stores;

import shapes.*;

/**
 * Clase base para representar una tienda en la Ruta de la Seda.
 * Esta es la tienda tipo "Normal".
 * 
 * @author DOPO - Ciclo 4
 * @version 4.0 - Octubre 2025
 */
public class Store {
    protected int location;
    protected int tenges;
    protected int initialTenges;
    protected Shape figure; // CAMBIADO: Shape en lugar de Circle
    protected static String[] colores = {"red", "orange", "yellow", "magenta", "pink"};
    protected static int contadorColores = 0;
    protected int x, y;
    protected int desocupaciones = 0;
    protected String colorBase;
    protected String tipo = "Normal";

    /**
     * Constructor de la tienda
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     * @param x Posición X en el canvas
     * @param y Posición Y en el canvas
     */
    public Store(int location, int tenges, int x, int y) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.x = x;
        this.y = y;
        this.figure = new Circle();
        
        // Posicionar la figura visual
        int initialX = 60;
        int initialY = 50;
        this.figure.moveHorizontal(x - initialX);
        this.figure.moveVertical(y - initialY);
        
        // Asignar color único
        colorBase = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure.changeColor(colorBase);
    }

    /**
     * Reabastecer la tienda a su estado inicial
     */
    public void resupply() {
        this.tenges = initialTenges;
        this.figure.changeColor(colorBase);
    }

    /**
     * Reducir los tenges de la tienda
     * @param amount Cantidad a reducir
     * @return Cantidad realmente reducida
     */
    public int reduceTenges(int amount) {
        int actualReduction = Math.min(amount, tenges);
        tenges -= actualReduction;
        if (tenges == 0) {
            marcarDesocupada();
        }
        return actualReduction;
    }

    /**
     * Marcar la tienda como desocupada (vacía)
     */
    public void marcarDesocupada() {
        this.figure.changeColor("gray");
        desocupaciones++;
    }

    /**
     * Mostrar la tienda en el canvas
     */
    public void show() {
        figure.makeVisible();
    }

    /**
     * Ocultar la tienda del canvas
     */
    public void hide() {
        figure.makeInvisible();
    }

    /**
     * Verificar si el robot puede robar de esta tienda
     * @param robotMoney Cantidad de dinero del robot
     * @return true si puede robar
     */
    public boolean canBeRobbedBy(int robotMoney) {
        return true; // Tienda normal permite cualquier robot
    }

    /**
     * Calcular el costo de movimiento hacia esta tienda
     * @param distance Distancia al robot
     * @return Costo de movimiento
     */
    public int getMovementCost(int distance) {
        return distance; // Tienda normal: costo = distancia
    }

    // Getters y métodos existentes
    public int getLocation() { return location; }
    public int getTenges() { return tenges; }
    public void setTenges(int tenges) {
        this.tenges = Math.max(0, tenges);
        if (this.tenges == 0) {
            marcarDesocupada();
        } else {
            this.figure.changeColor(colorBase);
        }
    }
    public int getDesocupaciones() { return desocupaciones; }
    public int getInitialTenges() { return initialTenges; }
    public boolean isEmpty() { return tenges == 0; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getTipo() { return tipo; }

    public void moveTo(int newX, int newY) {
        figure.moveHorizontal(newX - x);
        figure.moveVertical(newY - y);
        x = newX;
        y = newY;
    }

    public void resetDesocupaciones() {
        desocupaciones = 0;
    }

    public void changeColor(String newColor) {
        colorBase = newColor;
        figure.changeColor(newColor);
    }

    @Override
    public String toString() {
        return String.format("Store[%s](loc=%d, tenges=%d/%d, desoc=%d)", 
            tipo, location, tenges, initialTenges, desocupaciones);
    }
}
