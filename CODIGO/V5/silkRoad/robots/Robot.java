package silkRoad.robots;

import shapes.Rectangle;


public class Robot {
    protected int location;
    protected Rectangle figure;
    protected static String[] colores = {"blue", "cyan", "green", "gray"};
    protected static int contadorColores = 0;
    protected int x, y;
    protected int ganancia = 0;
    protected int initialLocation;
    protected String color;
    protected String tipo = "Normal";

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
     * Verificar si el robot puede moverse a una nueva ubicación
     * @param newLocation Nueva ubicación propuesta
     * @return true si puede moverse
     */
    public boolean canMoveTo(int newLocation) {
        return true; // Robot normal puede moverse a cualquier ubicación
    }

    /**
     * Calcular cuánto dinero tomar de una tienda
     * @param available Dinero disponible en la tienda
     * @return Cantidad a tomar
     */
    public int calculateTakeAmount(int available) {
        return available; // Robot normal toma todo
    }

    // Getters y setters
    public int getLocation() { return location; }
    public void setLocation(int loc) { location = loc; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void addGanancia(int g) { ganancia += g; }
    public int getGanancia() { return ganancia; }
    public void resetGanancia() { ganancia = 0; }
    public int getInitialLocation() { return initialLocation; }
    public String getColor() { return color; }
    public String getTipo() { return tipo; }

    public void returnToStart() {
        setLocation(initialLocation);
    }

    public void parpadear() {
        for (int i = 0; i < 6; i++) {
            figure.makeInvisible();
            shapes.Canvas.getCanvas().wait(50);
            figure.makeVisible();
            shapes.Canvas.getCanvas().wait(50);
        }
    }

    public void changeColor(String newColor) {
        color = newColor;
        figure.changeColor(newColor);
    }

    public boolean isAtLocation(int loc) {
        return location == loc;
    }

    public int distanceTo(int targetLocation) {
        return Math.abs(location - targetLocation);
    }

    @Override
    public String toString() {
        return String.format("Robot[%s](loc=%d, ganancia=%d, pos=(%d,%d), color=%s)",
                tipo, location, ganancia, x, y, color);
    }
}

