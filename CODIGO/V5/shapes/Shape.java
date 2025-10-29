package shapes
import java.awt.*;

/**
 * Abstract base class for all shapes that can be manipulated and drawn on a canvas.
 * This class contains common behavior shared by all geometric shapes.
 * 
 * @author Refactored for Silk Road Project - Ciclo 1, 2, 3
 * @version 3.0 (October 2025)
 */
public abstract class Shape {

    // Common attributes for all shapes
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Default constructor for Shape
     */
    public Shape() {
        this.xPosition = 0;
        this.yPosition = 0;
        this.color = "blue";
        this.isVisible = false;
    }

    /**
     * Constructor with parameters for Shape
     * @param xPosition The x position of the shape
     * @param yPosition The y position of the shape
     * @param color The color of the shape
     */
    public Shape(int xPosition, int yPosition, String color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
        this.isVisible = false;
    }

    /**
     * Make this shape visible. If it was already visible, do nothing.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Make this shape invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Abstract method to draw the shape - must be implemented by subclasses
     */
    protected abstract void draw();

    /**
     * Erase the shape on screen.
     */
    protected void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    /**
     * Move the shape a few pixels to the right.
     */
    public void moveRight() {
        moveHorizontal(20);
    }

    /**
     * Move the shape a few pixels to the left.
     */
    public void moveLeft() {
        moveHorizontal(-20);
    }

    /**
     * Move the shape a few pixels up.
     */
    public void moveUp() {
        moveVertical(-20);
    }

    /**
     * Move the shape a few pixels down.
     */
    public void moveDown() {
        moveVertical(20);
    }

    /**
     * Move the shape horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the shape vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the shape horizontally.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance) {
        int delta;
        if (distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for (int i = 0; i < distance; i++) {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the shape vertically.
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance) {
        int delta;
        if (distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for (int i = 0; i < distance; i++) {
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the color.
     * @param newColor the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta", "cyan", "orange", "pink", "gray", "darkGray", "lightGray", "white", and "black".
     */
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    // Getters for common attributes

    /**
     * Get the current x position of the shape.
     * @return The x position in pixels
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Get the current y position of the shape.
     * @return The y position in pixels
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Get the current color of the shape.
     * @return The color as a string
     */
    public String getColor() {
        return color;
    }

    /**
     * Check if the shape is currently visible.
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Set the position of the shape directly.
     * @param x The new x position
     * @param y The new y position
     */
    public void setPosition(int x, int y) {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /**
     * Move the shape to a specific position.
     * @param newX The target x position
     * @param newY The target y position
     */
    public void moveTo(int newX, int newY) {
        erase();
        xPosition = newX;
        yPosition = newY;
        draw();
    }

    /**
     * Scale the shape by a factor - must be implemented by subclasses
     * @param factor The scaling factor (1.0 = no change, 2.0 = double size, 0.5 = half size)
     */
    public abstract void scale(double factor);

    /**
     * Create a copy of this shape - must be implemented by subclasses
     * @return A new Shape object with the same properties
     */
    public abstract Shape copy();

    /**
     * Calculate the area of the shape - must be implemented by subclasses
     * @return The area of the shape
     */
    public abstract double getArea();

    /**
     * Check if a point is inside the shape - must be implemented by subclasses
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @return true if the point is inside the shape
     */
    public abstract boolean contains(int x, int y);

    /**
     * Get string representation of the shape.
     * @return String with shape properties
     */
    @Override
    public String toString() {
        return String.format("Shape[position=(%d,%d), color=%s, visible=%b]",
            xPosition, yPosition, color, isVisible);
    }

    /**
     * Check if two shapes are equal in terms of position, color and visibility.
     * @param obj The object to compare with
     * @return true if the shapes have the same basic properties
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Shape shape = (Shape) obj;
        return xPosition == shape.xPosition &&
               yPosition == shape.yPosition &&
               isVisible == shape.isVisible &&
               (color != null ? color.equals(shape.color) : shape.color == null);
    }

    /**
     * Generate hash code for the shape.
     * @return Hash code based on shape properties
     */
    @Override
    public int hashCode() {
        int result = xPosition;
        result = 31 * result + yPosition;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (isVisible ? 1 : 0);
        return result;
    }
}