import java.util.ArrayList;

public class SilkRoad {
    private int length;
    private ArrayList<Store> stores;
    private ArrayList<Robot> robots;
    private Canvas canvas;
    private boolean visible;
    private int profit;

    /**
     * Constructor que dibuja la espiral cuadrada continua al crear la ruta.
     * @param lados Número de lados/segmentos de la espiral a dibujar.
     */
    public SilkRoad(int lados) {
        this.length = lados;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.canvas = Canvas.getCanvas();
        this.visible = true;
        this.profit = 0;

        // Parámetros para dibujar la espiral
        int inicioX = 120;
        int inicioY = 120;
        int grosorLinea = 15;
        int longitudInicial = 30;
        int incrementoLongitud = 30;

        mostrarCaminoEspiral(lados, inicioX, inicioY, grosorLinea, longitudInicial, incrementoLongitud);
    }

    /**
     * Dibuja una espiral cuadrada tipo “caracol” usando rectángulos finos como líneas.
     * @param lados Nº total de lados o segmentos de la espiral.
     * @param x0 Coordenada inicial horizontal.
     * @param y0 Coordenada inicial vertical.
     * @param grosor Ancho del “trazo” en píxeles.
     * @param lonInicial Longitud inicial del primer segmento.
     * @param incremento Valor con que crece la longitud tras cada dos segmentos.
     */
    public void mostrarCaminoEspiral(int lados, int x0, int y0, int grosor, int lonInicial, int incremento) {
        int x = x0;
        int y = y0;
        int dx = 1; // dirección horizontal: derecha
        int dy = 0; // dirección vertical

        int length = lonInicial;
        int dirCount = 0;

        for (int i = 0; i < lados; i++) {
            Rectangle linea = new Rectangle();
            linea.changeColor("black");

            // Selecciona orientación y posiciona segmento
            if (dx != 0) { // horizontal
                linea.changeSize(grosor, length);
                if (dx == 1) { // derecha: posición ya está bien
                    linea.moveHorizontal(x - 70);
                    linea.moveVertical(y - 15);
                    x += length;
                } else { // izquierda: corrige para iniciar desde el extremo izq
                    linea.moveHorizontal(x - length - 70);
                    linea.moveVertical(y - 15);
                    x -= length;
                }
            } else { // vertical
                linea.changeSize(length, grosor);
                if (dy == 1) { // abajo: posición ya está bien
                    linea.moveHorizontal(x - 70);
                    linea.moveVertical(y - 15);
                    y += length;
                } else { // arriba: corrige para iniciar desde el extremo sup
                    linea.moveHorizontal(x - 70);
                    linea.moveVertical(y - length - 15);
                    y -= length;
                }
            }

            linea.makeVisible();

            // Cambia dirección: derecha->abajo->izq->arriba->derecha...
            // El ciclo: (dx, dy): (1,0)→(0,1)→(-1,0)→(0,-1)
            int temp = dx;
            dx = -dy;
            dy = temp;

            // Solo suma a la longitud cada dos lados
            dirCount++;
            if (dirCount == 2) {
                dirCount = 0;
                length += incremento;
            }
        }
    }

    // Métodos complementarios para tiendas y robots

    public void agregarTienda(Store s) {
        stores.add(s);
        s.show();
    }

    public void agregarRobot(Robot r) {
        robots.add(r);
        r.show();
    }

    public int desocupacionesTienda(int idx) {
        return stores.get(idx).getDesocupaciones();
    }

    public int gananciaRobot(int idx) {
        return robots.get(idx).getGanancia();
    }

    public boolean moverRobotSinColision(Robot robot, int newX, int newY) {
        for (Robot r : robots) {
            if (r != robot && r.getX() == newX && r.getY() == newY) {
                System.out.println("Movimiento ilegal: dos robots en la misma posición.");
                return false;
            }
        }
        robot.moveTo(newX, newY);
        return true;
    }

    public void destacarRobotGanador() {
        if (robots.isEmpty()) return;
        Robot max = robots.get(0);
        for (Robot r : robots) {
            if (r.getGanancia() > max.getGanancia()) max = r;
        }
        max.parpadear();
    }
}



