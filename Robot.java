/**
 * Clase que representa un robot en la ruta.
 * Cada robot tiene una ubicación y una figura rectangular que lo representa visualmente.
 */
public class Robot {
    private int location;         // Posición actual del robot en la ruta
    private Rectangle figure;     // Figura gráfica que representa el robot
    private static String[] colores = { "blue", "cyan", "green", "gray", "white" };
    private static int contadorColores = 0;  // Contador para asignar colores de forma cíclica
    private int x, y;             // Coordenadas actuales en pantalla del robot

    /**
     * Constructor que inicializa un robot con ubicación y posición gráfica.
     * Asigna un color de forma cíclica para distinguir visualmente cada robot.
     * @param location Posición en la ruta.
     * @param x Coordenada horizontal para la figura visual.
     * @param y Coordenada vertical para la figura visual.
     */
    public Robot(int location, int x, int y) {
        this.location = location;
        this.x = x;
        this.y = y;
        this.figure = new Rectangle();
        int initialX = 60;  // Posición base para ajustar la figura
        int initialY = 50;
        this.figure.moveHorizontal(x - initialX);
        this.figure.moveVertical(y - initialY);
        String color = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure.changeColor(color);
    }

    /**
     * Mueve la figura del robot a nuevas coordenadas.
     * Actualiza también las coordenadas internas del robot.
     * @param newX Nueva coordenada horizontal.
     * @param newY Nueva coordenada vertical.
     */
    public void moveTo(int newX, int newY) {
        figure.moveHorizontal(newX - x);
        figure.moveVertical(newY - y);
        x = newX;
        y = newY;
    }

    /**
     * Hace visible la figura que representa el robot.
     */
    public void show() {
        figure.makeVisible();
    }

    /**
     * Hace invisible la figura que representa el robot.
     */
    public void hide() {
        figure.makeInvisible();
    }

    /**
     * Obtiene la ubicación actual del robot en la ruta.
     * @return La posición del robot.
     */
    public int getLocation() {
        return location;
    }

    /**
     * Establece una nueva ubicación para el robot en la ruta.
     * @param loc Nueva posición del robot.
     */
    public void setLocation(int loc) {
        location = loc;
    }
}


