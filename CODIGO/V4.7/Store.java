public class Store {
    private int location;
    private int tenges;
    private int initialTenges;
    private Circle figure;
    private static String[] colores = {"red", "orange", "yellow", "magenta", "pink", "darkGray", "lightGray", "black"};
    private static int contadorColores = 0;
    private int x, y;
    private int desocupaciones = 0;
    private String colorBase;

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
        
        if (tenges <= 0) {
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
     * Obtener la ubicación de la tienda
     * @return Ubicación en la ruta
     */
    public int getLocation() { 
        return location; 
    }

    /**
     * Obtener la cantidad actual de tenges
     * @return Tenges disponibles
     */
    public int getTenges() {
        return tenges;
    }

    /**
     * Establecer la cantidad de tenges
     * @param tenges Nueva cantidad de tenges
     */
    public void setTenges(int tenges) {
        this.tenges = Math.max(0, tenges);
        
        if (this.tenges == 0) {
            marcarDesocupada();
        } else {
            this.figure.changeColor(colorBase);
        }
    }

    /**
     * Obtener el número de veces que se ha vaciado la tienda
     * @return Número de desocupaciones
     */
    public int getDesocupaciones() { 
        return desocupaciones; 
    }

    /**
     * Obtener la cantidad inicial de tenges
     * @return Tenges iniciales
     */
    public int getInitialTenges() {
        return initialTenges;
    }

    /**
     * Verificar si la tienda está vacía
     * @return true si no tiene tenges
     */
    public boolean isEmpty() {
        return tenges <= 0;
    }

    /**
     * Obtener la posición X en el canvas
     * @return Coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Obtener la posición Y en el canvas
     * @return Coordenada Y
     */
    public int getY() {
        return y;
    }

    /**
     * Cambiar la posición de la tienda
     * @param newX Nueva posición X
     * @param newY Nueva posición Y
     */
    public void moveTo(int newX, int newY) {
        figure.moveHorizontal(newX - x);
        figure.moveVertical(newY - y);
        x = newX;
        y = newY;
    }

    /**
     * Reiniciar el contador de desocupaciones
     */
    public void resetDesocupaciones() {
        desocupaciones = 0;
    }

    /**
     * Cambiar el color de la tienda
     * @param newColor Nuevo color
     */
    public void changeColor(String newColor) {
        colorBase = newColor;
        figure.changeColor(newColor);
    }

    /**
     * Obtener información completa de la tienda como string
     * @return Información detallada de la tienda
     */
    @Override
    public String toString() {
        return String.format("Store[loc=%d, tenges=%d/%d, desocupaciones=%d]", 
                           location, tenges, initialTenges, desocupaciones);
    }

    /**
     * Comparar tiendas por ubicación
     * @param other Otra tienda
     * @return Comparación por ubicación
     */
    public int compareTo(Store other) {
        return Integer.compare(this.location, other.location);
    }

    /**
     * Verificar si dos tiendas son iguales (misma ubicación)
     * @param obj Objeto a comparar
     * @return true si están en la misma ubicación
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Store store = (Store) obj;
        return location == store.location;
    }

    /**
     * Generar hash code basado en la ubicación
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(location);
    }
}