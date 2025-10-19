public class Robot {
    private int location;
    private Rectangle figure;
    private static String[] colores = {"blue", "cyan", "green", "gray", "white"};
    private static int contadorColores = 0;
    private int x, y;
    private int ganancia = 0;
    private int initialLocation;
    private String color;

    /**
     * Constructor del robot
     * @param location Ubicación inicial en la ruta
     * @param x Posición X inicial en el canvas
     * @param y Posición Y inicial en el canvas
     */
    public Robot(int location, int x, int y) {
        this.location = location;
        this.initialLocation = location;
        this.x = x;
        this.y = y;
        this.figure = new Rectangle();
        
        // Posicionar la figura visual
        int initialX = 60;
        int initialY = 50;
        this.figure.moveHorizontal(x - initialX);
        this.figure.moveVertical(y - initialY);
        
        // Asignar color único
        this.color = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure.changeColor(color);
    }

    /**
     * Mover el robot a una nueva posición en el canvas
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
     * Mostrar el robot en el canvas
     */
    public void show() { 
        figure.makeVisible(); 
    }

    /**
     * Ocultar el robot del canvas
     */
    public void hide() { 
        figure.makeInvisible(); 
    }

    /**
     * Obtener la ubicación actual del robot en la ruta
     * @return Ubicación en la ruta
     */
    public int getLocation() { 
        return location; 
    }

    /**
     * Establecer nueva ubicación del robot en la ruta
     * @param loc Nueva ubicación
     */
    public void setLocation(int loc) { 
        location = loc; 
    }

    /**
     * Obtener la posición X actual en el canvas
     * @return Coordenada X
     */
    public int getX() { 
        return x; 
    }

    /**
     * Obtener la posición Y actual en el canvas
     * @return Coordenada Y
     */
    public int getY() { 
        return y; 
    }

    /**
     * Agregar ganancia al robot
     * @param g Cantidad de ganancia a agregar
     */
    public void addGanancia(int g) { 
        ganancia += g; 
    }

    /**
     * Obtener la ganancia total del robot
     * @return Ganancia acumulada
     */
    public int getGanancia() { 
        return ganancia; 
    }

    /**
     * Reiniciar la ganancia del robot a cero
     */
    public void resetGanancia() {
        ganancia = 0;
    }

    /**
     * Efecto visual de parpadeo para resaltar el robot
     */
    public void parpadear() {
        for (int i = 0; i < 6; i++) {
            figure.makeInvisible();
            Canvas.getCanvas().wait(50);
            figure.makeVisible();
            Canvas.getCanvas().wait(50);
        }
    }

    /**
     * Obtener la ubicación inicial del robot
     * @return Ubicación inicial
     */
    public int getInitialLocation() {
        return initialLocation;
    }

    /**
     * Retornar el robot a su posición inicial
     */
    public void returnToStart() {
        setLocation(initialLocation);
        // Calcular posición visual inicial
        int startX = 600 + (initialLocation * 50);
        int startY = 450 + (initialLocation * 20);
        moveTo(startX, startY);
    }

    /**
     * Mover el robot una distancia específica en la ruta
     * @param distance Distancia a mover (puede ser negativa)
     * @param maxLocation Ubicación máxima válida en la ruta
     */
    public void moveDistance(int distance, int maxLocation) {
        int newLocation = location + distance;
        
        // Verificar límites
        if (newLocation < 0) {
            newLocation = 0;
        } else if (newLocation >= maxLocation) {
            newLocation = maxLocation - 1;
        }
        
        setLocation(newLocation);
        
        // Calcular nueva posición visual
        int newX = 600 + (newLocation * 50);
        int newY = 450 + (newLocation * 20);
        moveTo(newX, newY);
    }

    /**
     * Cambiar el color del robot
     * @param newColor Nuevo color
     */
    public void changeColor(String newColor) {
        color = newColor;
        figure.changeColor(newColor);
    }

    /**
     * Obtener el color actual del robot
     * @return Color del robot
     */
    public String getColor() {
        return color;
    }

    /**
     * Verificar si el robot está en una ubicación específica
     * @param loc Ubicación a verificar
     * @return true si el robot está en esa ubicación
     */
    public boolean isAtLocation(int loc) {
        return location == loc;
    }

    /**
     * Calcular la distancia a otra ubicación
     * @param targetLocation Ubicación objetivo
     * @return Distancia absoluta
     */
    public int distanceTo(int targetLocation) {
        return Math.abs(location - targetLocation);
    }

    /**
     * Mover el robot lentamente hacia una ubicación objetivo
     * @param targetLocation Ubicación objetivo
     * @param maxLocation Ubicación máxima válida
     */
    public void slowMoveTo(int targetLocation, int maxLocation) {
        int distance = targetLocation - location;
        int steps = Math.abs(distance);
        int direction = distance > 0 ? 1 : -1;
        
        for (int i = 0; i < steps; i++) {
            moveDistance(direction, maxLocation);
            Canvas.getCanvas().wait(200); // Pausa para efecto visual
        }
    }

    /**
     * Obtener información completa del robot como string
     * @return Información detallada del robot
     */
    @Override
    public String toString() {
        return String.format("Robot[loc=%d, ganancia=%d, pos=(%d,%d), color=%s]", 
                           location, ganancia, x, y, color);
    }

    /**
     * Comparar robots por ubicación
     * @param other Otro robot
     * @return Comparación por ubicación
     */
    public int compareTo(Robot other) {
        return Integer.compare(this.location, other.location);
    }

    /**
     * Verificar si dos robots son iguales (misma ubicación)
     * @param obj Objeto a comparar
     * @return true si están en la misma ubicación
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Robot robot = (Robot) obj;
        return location == robot.location;
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
     * Crear una copia del robot en una nueva ubicación
     * @param newLocation Nueva ubicación
     * @return Nuevo robot copiado
     */
    public Robot copy(int newLocation) {
        int newX = 120 + (newLocation * 50);
        int newY = 100 + (newLocation * 20);
        Robot copy = new Robot(newLocation, newX, newY);
        copy.ganancia = this.ganancia;
        return copy;
    }
}