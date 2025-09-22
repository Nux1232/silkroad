public class Robot {
    private int location;
    private Rectangle figure;
    private static String[] colores = { "blue", "cyan", "green", "gray", "white" };
    private static int contadorColores = 0;
    private int x, y;
    private int ganancia = 0;

    public Robot(int location, int x, int y) {
        this.location = location;
        this.x = x;
        this.y = y;
        this.figure = new Rectangle();
        int initialX = 60;
        int initialY = 50;
        this.figure.moveHorizontal(x - initialX);
        this.figure.moveVertical(y - initialY);
        this.figure.changeColor(colores[contadorColores % colores.length]);
        contadorColores++;
    }

    public void moveTo(int newX, int newY) {
        figure.moveHorizontal(newX - x);
        figure.moveVertical(newY - y);
        x = newX; y = newY;
    }

    public void show() { figure.makeVisible(); }
    public void hide() { figure.makeInvisible(); }
    public int getLocation() { return location; }
    public void setLocation(int loc) { location = loc; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void addGanancia(int g) { ganancia += g; }
    public int getGanancia() { return ganancia; }

    public void parpadear() {
        for (int i = 0; i < 6; i++) {
            figure.makeInvisible();
            Canvas.getCanvas().wait(50);
            figure.makeVisible();
            Canvas.getCanvas().wait(50);
        }
    }
}



