public class Store {
    private int location;
    private int tenges;
    private Circle figure;
    private static String[] colores = {"red", "orange", "yellow", "magenta", "pink", "darkGray", "lightGray", "black"};
    private static int contadorColores = 0;
    private int x, y;
    private int initialTenges;
    private int desocupaciones = 0;
    private String colorBase;

    public Store(int location, int tenges, int x, int y) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.x = x;
        this.y = y;
        this.figure = new Circle();
        int initialX = 60;
        int initialY = 50;
        this.figure.moveHorizontal(x - initialX);
        this.figure.moveVertical(y - initialY);
        colorBase = colores[contadorColores % colores.length];
        contadorColores++;
        this.figure.changeColor(colorBase);
    }

    public void resupply() {
        this.tenges = initialTenges;
        this.figure.changeColor(colorBase);
    }

    public void show() { figure.makeVisible(); }
    public void hide() { figure.makeInvisible(); }
    public int getLocation() { return location; }

    public void marcarDesocupada() {
        this.figure.changeColor("gray");
        desocupaciones++;
    }

    public int getDesocupaciones() { return desocupaciones; }
}


