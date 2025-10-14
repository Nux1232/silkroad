/**
 * Tienda que se posiciona siguiendo el patrón de espiral del simulador de la Ruta de la Seda.
 * Actualizada para posicionamiento en espiral usando coordenadas exactas.
 *
 * @author Spiral Positioning Update - Ciclo 3
 * @version 3.0 (October 2025)
 */
public class Store {

    private int location;
    private int tenges;
    private int initialTenges;
    private Circle figure; // Usando Circle refactorizada
    private static String[] colores = {"red", "orange", "yellow", "magenta", "pink", "darkGray", "lightGray", "black"};
    private static int contadorColores = 0;
    private int x, y;
    private int desocupaciones = 0;
    private String colorBase;
    private SpiralPath spiralPath; // Sistema de coordenadas en espiral
    private boolean followSpiral; // Si debe seguir el patrón de espiral

    /**
     * Constructor de la tienda con posicionamiento en espiral
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de tenges
     * @param spiralPath Sistema de coordenadas de la espiral
     */
    public Store(int location, int tenges, SpiralPath spiralPath) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.spiralPath = spiralPath;
        this.followSpiral = true;

        // Obtener coordenadas de la espiral
        int[] coords = spiralPath.getCoordinates(location);
        this.x = coords[0];
        this.y = coords[1];

        // Crear círculo usando la nueva clase refactorizada
        colorBase = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure = new Circle(25, x, y, colorBase);
    }

    /**
     * Constructor legacy para compatibilidad (sin espiral)
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
        this.followSpiral = false;
        this.spiralPath = null;

        // Crear círculo usando la nueva clase refactorizada
        colorBase = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure = new Circle(25, x, y, colorBase);
    }

    /**
     * Configurar el sistema de espiral para esta tienda
     * @param spiralPath Sistema de coordenadas de la espiral
     */
    public void setSpiralPath(SpiralPath spiralPath) {
        this.spiralPath = spiralPath;
        this.followSpiral = true;
        // Actualizar posición según la espiral
        moveToSpiralPosition(location);
    }

    /**
     * Mover la tienda a una posición específica en la espiral
     * @param newLocation Nueva ubicación en la espiral
     */
    private void moveToSpiralPosition(int newLocation) {
        if (spiralPath != null && spiralPath.isValidPosition(newLocation)) {
            int[] coords = spiralPath.getCoordinates(newLocation);
            moveTo(coords[0], coords[1]);
            location = newLocation;
        }
    }

    /**
     * Obtener información sobre la posición en la espiral
     * @return String con información detallada
     */
    public String getSpiralInfo() {
        if (followSpiral && spiralPath != null) {
            int[] coords = spiralPath.getCoordinates(location);
            return String.format("Tienda en posición %d/%d de la espiral, coordenadas (%d, %d), tenges: %d", 
                location, spiralPath.getLength() - 1, coords[0], coords[1], tenges);
        } else {
            return "Tienda en modo libre, posición " + location + ", coordenadas (" + x + ", " + y + "), tenges: " + tenges;
        }
    }

    /**
     * Verificar si la tienda está siguiendo la espiral
     * @return true si sigue el patrón de espiral
     */
    public boolean isFollowingSpiral() {
        return followSpiral && spiralPath != null;
    }

    /**
     * Activar o desactivar el seguimiento de espiral
     * @param follow true para seguir espiral, false para posicionamiento libre
     */
    public void setFollowSpiral(boolean follow) {
        this.followSpiral = follow;
    }

    /**
     * Obtener la distancia de la tienda a otra posición en la espiral
     * @param targetPosition Posición objetivo
     * @return Distancia euclidiana
     */
    public double getDistanceToPosition(int targetPosition) {
        if (followSpiral && spiralPath != null) {
            return spiralPath.getDistance(location, targetPosition);
        } else {
            // Cálculo legacy
            return Math.abs(targetPosition - location) * 50; // Aproximación
        }
    }

    /**
     * Crear un efecto visual cuando la tienda está siendo visitada
     */
    public void visitEffect() {
        String originalColor = colorBase;

        // Cambiar a color brillante temporalmente
        figure.changeColor("yellow");
        Canvas.getCanvas().wait(200);

        // Hacer parpadeo
        for (int i = 0; i < 3; i++) {
            figure.changeColor("white");
            Canvas.getCanvas().wait(100);
            figure.changeColor("yellow");
            Canvas.getCanvas().wait(100);
        }

        // Restaurar color original o marcar como vacía si corresponde
        if (tenges > 0) {
            figure.changeColor(originalColor);
        } else {
            figure.changeColor("gray");
        }
    }

    /**
     * Efecto visual cuando la tienda es reabastecida
     */
    public void resupplyEffect() {
        // Hacer brillar la tienda al reabastecer
        String originalColor = colorBase;

        for (int i = 0; i < 5; i++) {
            figure.changeColor("gold");
            Canvas.getCanvas().wait(150);
            figure.changeColor(originalColor);
            Canvas.getCanvas().wait(150);
        }

        System.out.println("Tienda en posición " + location + " reabastecida con " + initialTenges + " tenges");
    }

    // === MÉTODOS ORIGINALES MANTENIDOS PARA COMPATIBILIDAD ===

    /**
     * Reabastecer la tienda a su estado inicial
     */
    public void resupply() {
        this.tenges = initialTenges;
        this.figure.changeColor(colorBase);
        if (followSpiral) {
            resupplyEffect();
        }
    }

    /**
     * Reducir los tenges de la tienda con efecto visual
     * @param amount Cantidad a reducir
     * @return Cantidad realmente reducida
     */
    public int reduceTenges(int amount) {
        int actualReduction = Math.min(amount, tenges);
        tenges -= actualReduction;

        if (followSpiral && actualReduction > 0) {
            visitEffect();
        }

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
        if (followSpiral) {
            System.out.println("¡Tienda en posición " + location + " ha sido vaciada! (Vaciado #" + desocupaciones + ")");
        }
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
        figure.moveTo(newX, newY);
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
        String spiralInfo = followSpiral ? " (siguiendo espiral)" : " (modo libre)";
        return String.format("Store[loc=%d, tenges=%d/%d, desocupaciones=%d%s]",
            location, tenges, initialTenges, desocupaciones, spiralInfo);
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

    /**
     * Crear una copia de la tienda en una nueva ubicación
     * @param newLocation Nueva ubicación
     * @return Nueva tienda copiada
     */
    public Store copy(int newLocation) {
        Store copy;
        if (followSpiral && spiralPath != null) {
            copy = new Store(newLocation, initialTenges, spiralPath);
        } else {
            int newX = 120 + (newLocation * 50);
            int newY = 120 + (newLocation * 20);
            copy = new Store(newLocation, initialTenges, newX, newY);
        }
        copy.tenges = this.tenges;
        copy.desocupaciones = this.desocupaciones;
        return copy;
    }
}