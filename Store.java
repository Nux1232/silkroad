/**
 * Clase que representa una tienda en la ruta.
 * Cada tienda tiene una ubicación, una cantidad de tenges,
 * una figura circular que la representa visualmente y un color asignado.
 */
public class Store {
    private int location;         // Posición de la tienda en la ruta
    private int tenges;           // Cantidad actual de tenges en la tienda
    private Circle figure;        // Figura gráfica que representa la tienda
    private static String[] colores = {"red", "orange", "yellow", "magenta", "pink", "darkGray", "lightGray", "black"};
    private static int contadorColores = 0;  // Contador para asignar colores de forma cíclica
    private int x, y;             // Coordenadas en pantalla para la figura
    private int initialTenges;    // Cantidad inicial de tenges para resurtido

    /**
     * Constructor que inicializa una tienda con ubicación, cantidad de tenges y posición gráfica.
     * También asigna un color de forma cíclica para distinguir visualmente cada tienda.
     * @param location Posición en la ruta.
     * @param tenges Cantidad inicial de tenges.
     * @param x Coordenada horizontal para la figura visual.
     * @param y Coordenada vertical para la figura visual.
     */
    public Store(int location, int tenges, int x, int y) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.x = x;
        this.y = y;
        this.figure = new Circle();
        int initialX = 60;  // Posición base para ajustar la figura
        int initialY = 50;
        this.figure.moveHorizontal(x - initialX);
        this.figure.moveVertical(y - initialY);
        String color = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure.changeColor(color);
    }

    /**
     * Resurtir la tienda devolviendo la cantidad de tenges a la cantidad inicial.
     */
    public void resupply() {
        this.tenges = initialTenges;
    }

    /**
     * Hace visible la figura que representa la tienda.
     */
    public void show() {
        figure.makeVisible();
    }

    /**
     * Hace invisible la figura que representa la tienda.
     */
    public void hide() {
        figure.makeInvisible();
    }

    /**
     * Obtiene la ubicación de la tienda en la ruta.
     * @return La posición de la tienda.
     */
    public int getLocation() {
        return location;
    }
}

