import java.awt.Color;

/**
 * Barra de progreso para mostrar la ganancia actual vs máxima posible
 * Requisito de usabilidad #4: Sin números, solo barra visual
 */
public class ProgressBar {
    private Rectangle background;
    private Rectangle fill;
    private int x;
    private int y;
    private int width;
    private int height;
    private int maxValue;
    private int currentValue;
    private boolean isVisible;
    
    /**
     * Constructor de la barra de progreso
     * @param x Posición X
     * @param y Posición Y  
     * @param width Ancho de la barra
     * @param height Alto de la barra
     */
    public ProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxValue = 100;
        this.currentValue = 0;
        this.isVisible = false;
        
        // Crear fondo de la barra (gris claro)
        background = new Rectangle();
        background.changeSize(height, width);
        background.moveHorizontal(x - 60);
        background.moveVertical(y - 50);
        background.changeColor("lightGray");
        
        // Crear relleno de la barra (verde para progreso)
        fill = new Rectangle();
        fill.changeSize(height - 4, 2); // Empieza vacío
        fill.moveHorizontal(x - 58);
        fill.moveVertical(y - 48);
        fill.changeColor("green");
    }
    
    /**
     * Actualizar el valor máximo de la barra
     * @param max Valor máximo posible
     */
    public void setMaxValue(int max) {
        if (max > 0) {
            this.maxValue = max;
            updateFill();
        }
    }
    
    /**
     * Actualizar el valor actual de la barra
     * @param current Valor actual de ganancia
     */
    public void setCurrentValue(int current) {
        this.currentValue = Math.max(0, current);
        updateFill();
    }
    
    /**
     * Actualizar ambos valores (máximo y actual)
     * @param current Valor actual
     * @param max Valor máximo
     */
    public void updateValues(int current, int max) {
        if (max > 0) {
            this.maxValue = max;
        }
        this.currentValue = Math.max(0, current);
        updateFill();
    }
    
    /**
     * Actualizar el tamaño del relleno según el progreso
     */
    private void updateFill() {
        if (maxValue <= 0) return;
        
        // Calcular porcentaje
        double percentage = (double) currentValue / maxValue;
        percentage = Math.min(1.0, percentage); // No más del 100%
        
        // Calcular nuevo ancho del relleno
        int fillWidth = (int) ((width - 4) * percentage);
        fillWidth = Math.max(2, fillWidth); // Mínimo 2 pixels
        
        // Actualizar relleno
        fill.changeSize(height - 4, fillWidth);
        
        // Cambiar color según el progreso
        if (percentage < 0.33) {
            fill.changeColor("red");
        } else if (percentage < 0.66) {
            fill.changeColor("yellow");
        } else {
            fill.changeColor("green");
        }
    }
    
    /**
     * Hacer visible la barra de progreso
     */
    public void makeVisible() {
        background.makeVisible();
        fill.makeVisible();
        isVisible = true;
    }
    
    /**
     * Hacer invisible la barra de progreso
     */
    public void makeInvisible() {
        background.makeInvisible();
        fill.makeInvisible();
        isVisible = false;
    }
    
    /**
     * Verificar si la barra es visible
     * @return true si es visible
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    /**
     * Obtener valor actual
     * @return Valor actual
     */
    public int getCurrentValue() {
        return currentValue;
    }
    
    /**
     * Obtener valor máximo
     * @return Valor máximo
     */
    public int getMaxValue() {
        return maxValue;
    }
    
    /**
     * Mover la barra a una nueva posición
     * @param newX Nueva posición X
     * @param newY Nueva posición Y
     */
    public void moveTo(int newX, int newY) {
        int deltaX = newX - x;
        int deltaY = newY - y;
        
        background.moveHorizontal(deltaX);
        background.moveVertical(deltaY);
        fill.moveHorizontal(deltaX);
        fill.moveVertical(deltaY);
        
        this.x = newX;
        this.y = newY;
    }
}
