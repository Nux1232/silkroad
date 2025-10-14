import java.awt.*;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 * Refactored to inherit from Shape class for better code reusability.
 *
 * @author Michael Kolling and David J. Barnes (Refactored for Ciclo 1-3)
 * @version 3.0 (October 2025)
 */
public class Rectangle extends Shape {

    public static final int EDGES = 4;
    private int height;
    private int width;

    /**
     * Create a new rectangle at default position with default color.
     */
    public Rectangle() {
        super(70, 15, "magenta"); // Call parent constructor
        height = 30;
        width = 40;
    }

    /**
     * Create a new rectangle with specified parameters.
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param xPosition The x position of the rectangle
     * @param yPosition The y position of the rectangle
     * @param color The color of the rectangle
     */
    public Rectangle(int width, int height, int xPosition, int yPosition, String color) {
        super(xPosition, yPosition, color); // Call parent constructor
        this.width = width;
        this.height = height;
    }

    /**
     * Draw the rectangle with current specifications on screen.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }

    /**
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidth the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        if (newHeight >= 0 && newWidth >= 0) {
            erase();
            height = newHeight;
            width = newWidth;
            draw();
        }
    }

    /**
     * Scale the rectangle by a factor.
     * @param factor The scaling factor (1.0 = no change, 2.0 = double size, 0.5 = half size)
     */
    @Override
    public void scale(double factor) {
        if (factor > 0) {
            changeSize((int)(height * factor), (int)(width * factor));
        }
    }

    /**
     * Calculate the area of the rectangle.
     * @return The area in square pixels
     */
    @Override
    public double getArea() {
        return width * height;
    }

    /**
     * Check if a point is inside the rectangle.
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @return true if the point is inside the rectangle
     */
    @Override
    public boolean contains(int x, int y) {
        return x >= xPosition && x <= xPosition + width &&
               y >= yPosition && y <= yPosition + height;
    }

    /**
     * Create a copy of this rectangle.
     * @return A new Rectangle object with the same properties
     */
    @Override
    public Rectangle copy() {
        Rectangle newRect = new Rectangle(width, height, xPosition, yPosition, color);
        if (isVisible) {
            newRect.makeVisible();
        }
        return newRect;
    }

    // Rectangle-specific getters and methods

    /**
     * Get the current width of the rectangle.
     * @return The width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the current height of the rectangle.
     * @return The height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Calculate the perimeter of the rectangle.
     * @return The perimeter in pixels
     */
    public int getPerimeter() {
        return 2 * (width + height);
    }

    /**
     * Check if this rectangle overlaps with another rectangle.
     * @param other The other rectangle
     * @return true if the rectangles overlap
     */
    public boolean overlaps(Rectangle other) {
        return !(xPosition + width < other.xPosition ||
                other.xPosition + other.width < xPosition ||
                yPosition + height < other.yPosition ||
                other.yPosition + other.height < yPosition);
    }

    /**
     * Check if this rectangle is a square.
     * @return true if width equals height
     */
    public boolean isSquare() {
        return width == height;
    }

    /**
     * Get the center x coordinate of the rectangle.
     * @return The center x coordinate
     */
    public int getCenterX() {
        return xPosition + width / 2;
    }

    /**
     * Get the center y coordinate of the rectangle.
     * @return The center y coordinate
     */
    public int getCenterY() {
        return yPosition + height / 2;
    }

    /**
     * Set only the width of the rectangle.
     * @param newWidth The new width in pixels
     */
    public void setWidth(int newWidth) {
        if (newWidth >= 0) {
            changeSize(height, newWidth);
        }
    }

    /**
     * Set only the height of the rectangle.
     * @param newHeight The new height in pixels
     */
    public void setHeight(int newHeight) {
        if (newHeight >= 0) {
            changeSize(newHeight, width);
        }
    }

    /**
     * Get string representation of the rectangle.
     * @return String with rectangle properties
     */
    @Override
    public String toString() {
        return String.format("Rectangle[width=%d, height=%d, position=(%d,%d), color=%s, visible=%b]",
            width, height, xPosition, yPosition, color, isVisible);
    }

    /**
     * Check if two rectangles are equal.
     * @param obj The object to compare with
     * @return true if the rectangles have the same properties
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Rectangle rectangle = (Rectangle) obj;
        return width == rectangle.width && height == rectangle.height;
    }

    /**
     * Generate hash code for the rectangle.
     * @return Hash code based on rectangle properties
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}