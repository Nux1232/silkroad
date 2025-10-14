
public class Robot {

    private int location;
    private Rectangle figure; // Usando Rectangle refactorizada
    private static String[] colores = {"blue", "cyan", "green", "gray", "white"};
    private static int contadorColores = 0;
    private int x, y;
    private int ganancia = 0;
    private int initialLocation;
    private String color;
    private SpiralPath spiralPath; // Sistema de coordenadas en espiral
    private boolean followSpiral; // Si debe seguir el patrón de espiral

    /**
     * Constructor del robot con movimiento en espiral
     * @param location Ubicación inicial en la ruta
     * @param spiralPath Sistema de coordenadas de la espiral
     */
    public Robot(int location, SpiralPath spiralPath) {
        this.location = location;
        this.initialLocation = location;
        this.spiralPath = spiralPath;
        this.followSpiral = true;

        // Obtener coordenadas de la espiral
        int[] coords = spiralPath.getCoordinates(location);
        this.x = coords[0];
        this.y = coords[1];

        // Crear rectángulo usando la nueva clase refactorizada
        this.figure = new Rectangle(20, 15, x, y, colores[contadorColores % colores.length]);

        // Asignar color único
        this.color = colores[contadorColores % colores.length];
        contadorColores++;
    }

    /**
     * Constructor legacy para compatibilidad (sin espiral)
     * @param location Ubicación inicial en la ruta
     * @param x Posición X inicial en el canvas
     * @param y Posición Y inicial en el canvas
     */
    public Robot(int location, int x, int y) {
        this.location = location;
        this.initialLocation = location;
        this.x = x;
        this.y = y;
        this.followSpiral = false;
        this.spiralPath = null;

        // Crear rectángulo usando la nueva clase refactorizada
        this.figure = new Rectangle(20, 15, x, y, colores[contadorColores % colores.length]);

        // Asignar color único
        this.color = colores[contadorColores % colores.length];
        contadorColores++;
    }

    /**
     * Configurar el sistema de espiral para este robot
     * @param spiralPath Sistema de coordenadas de la espiral
     */
    public void setSpiralPath(SpiralPath spiralPath) {
        this.spiralPath = spiralPath;
        this.followSpiral = true;
        // Actualizar posición según la espiral
        moveToSpiralPosition(location);
    }

    /**
     * Mover el robot a una posición específica en la espiral
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
     * Mover el robot a la siguiente posición en la espiral
     */
    public void moveToNextPosition() {
        if (followSpiral && spiralPath != null) {
            int nextPos = spiralPath.getNextPosition(location);
            if (nextPos != location) { // Solo mover si hay una siguiente posición
                moveToSpiralPosition(nextPos);
                System.out.println("Robot movido de posición " + (nextPos-1) + " a posición " + nextPos);
            } else {
                System.out.println("Robot ya está en la última posición de la espiral");
            }
        } else {
            // Movimiento legacy
            moveDistance(1, 50); // Asumir longitud máxima de 50
        }
    }

    /**
     * Mover el robot a la posición anterior en la espiral
     */
    public void moveToPreviousPosition() {
        if (followSpiral && spiralPath != null) {
            int prevPos = spiralPath.getPreviousPosition(location);
            if (prevPos != location) { // Solo mover si hay una posición anterior
                moveToSpiralPosition(prevPos);
                System.out.println("Robot movido de posición " + (prevPos+1) + " a posición " + prevPos);
            } else {
                System.out.println("Robot ya está en la primera posición de la espiral");
            }
        } else {
            // Movimiento legacy
            moveDistance(-1, 50);
        }
    }

    /**
     * Mover el robot una cantidad específica de pasos en la espiral
     * @param steps Número de pasos (positivo = adelante, negativo = atrás)
     */
    public void moveSteps(int steps) {
        if (followSpiral && spiralPath != null) {
            int targetPosition = location + steps;
            // Asegurar que esté dentro de los límites
            targetPosition = Math.max(0, Math.min(targetPosition, spiralPath.getLength() - 1));

            // Mover paso a paso para efecto visual
            if (steps > 0) {
                for (int i = 0; i < steps && location < spiralPath.getLength() - 1; i++) {
                    moveToNextPosition();
                    Canvas.getCanvas().wait(200); // Pausa visual
                }
            } else {
                for (int i = 0; i < Math.abs(steps) && location > 0; i++) {
                    moveToPreviousPosition();
                    Canvas.getCanvas().wait(200); // Pausa visual
                }
            }
        } else {
            // Movimiento legacy
            moveDistance(steps, 50);
        }
    }

    /**
     * Mover el robot lentamente a una posición específica en la espiral
     * @param targetLocation Posición objetivo
     */
    public void slowMoveToPosition(int targetLocation) {
        if (followSpiral && spiralPath != null && spiralPath.isValidPosition(targetLocation)) {
            int steps = targetLocation - location;
            moveSteps(steps);
        } else {
            // Fallback a movimiento legacy
            slowMoveTo(targetLocation, 50);
        }
    }

    /**
     * Hacer que el robot siga un recorrido completo de la espiral
     * @param delay Tiempo de pausa entre cada paso (milisegundos)
     */
    public void followCompleteSpiral(int delay) {
        if (!followSpiral || spiralPath == null) {
            System.out.println("Robot no configurado para movimiento en espiral");
            return;
        }

        System.out.println("Robot iniciando recorrido completo de la espiral...");

        // Ir al inicio
        moveToSpiralPosition(0);
        Canvas.getCanvas().wait(delay);

        // Recorrer toda la espiral
        for (int pos = 1; pos < spiralPath.getLength(); pos++) {
            moveToNextPosition();
            Canvas.getCanvas().wait(delay);

            // Mostrar progreso cada 10 posiciones
            if (pos % 10 == 0) {
                System.out.println("Robot en posición " + pos + "/" + spiralPath.getLength());
            }
        }

        System.out.println("Robot completó el recorrido de la espiral");
    }

    /**
     * Obtener la distancia del robot a otra posición en la espiral
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
     * Verificar si el robot está siguiendo la espiral
     * @return true si sigue el patrón de espiral
     */
    public boolean isFollowingSpiral() {
        return followSpiral && spiralPath != null;
    }

    /**
     * Activar o desactivar el seguimiento de espiral
     * @param follow true para seguir espiral, false para movimiento libre
     */
    public void setFollowSpiral(boolean follow) {
        this.followSpiral = follow;
    }

    /**
     * Obtener información sobre la posición en la espiral
     * @return String con información detallada
     */
    public String getSpiralInfo() {
        if (followSpiral && spiralPath != null) {
            int[] coords = spiralPath.getCoordinates(location);
            return String.format("Robot en posición %d/%d de la espiral, coordenadas (%d, %d)", 
                location, spiralPath.getLength() - 1, coords[0], coords[1]);
        } else {
            return "Robot en modo libre, posición " + location + ", coordenadas (" + x + ", " + y + ")";
        }
    }

    // === MÉTODOS ORIGINALES MANTENIDOS PARA COMPATIBILIDAD ===

    /**
     * Mover el robot a una nueva posición en el canvas
     * @param newX Nueva posición X
     * @param newY Nueva posición Y
     */
    public void moveTo(int newX, int newY) {
        figure.moveTo(newX, newY);
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
        if (followSpiral && spiralPath != null) {
            moveToSpiralPosition(loc);
        } else {
            location = loc;
        }
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
            Canvas.getCanvas().wait(100);
            figure.makeVisible();
            Canvas.getCanvas().wait(100);
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
        if (followSpiral && spiralPath != null) {
            moveToSpiralPosition(initialLocation);
        } else {
            setLocation(initialLocation);
            int startX = 120 + (initialLocation * 50);
            int startY = 100 + (initialLocation * 20);
            moveTo(startX, startY);
        }
    }

    /**
     * Mover el robot una distancia específica en la ruta (método legacy)
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

        if (followSpiral && spiralPath != null) {
            moveToSpiralPosition(newLocation);
        } else {
            setLocation(newLocation);
            int newX = 120 + (newLocation * 50);
            int newY = 100 + (newLocation * 20);
            moveTo(newX, newY);
        }
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
     * Mover el robot lentamente hacia una ubicación objetivo (método legacy)
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
        String spiralInfo = followSpiral ? " (siguiendo espiral)" : " (modo libre)";
        return String.format("Robot[loc=%d, ganancia=%d, pos=(%d,%d), color=%s%s]",
            location, ganancia, x, y, color, spiralInfo);
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
        Robot copy;
        if (followSpiral && spiralPath != null) {
            copy = new Robot(newLocation, spiralPath);
        } else {
            int newX = 120 + (newLocation * 50);
            int newY = 100 + (newLocation * 20);
            copy = new Robot(newLocation, newX, newY);
        }
        copy.ganancia = this.ganancia;
        return copy;
    }
}