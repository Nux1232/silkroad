package shapes
import java.awt.*;

public class Triangle extends Shape {

    public static final int VERTICES = 3;
    private int height;
    private int width;

    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle() {
        super(140, 15, "green"); // Call parent constructor
        height = 30;
        width = 40;
    }

    /**
     * Create a new triangle with specified parameters.
     * @param width The base width of the triangle
     * @param height The height of the triangle
     * @param xPosition The x position of the triangle
     * @param yPosition The y position of the triangle
     * @param color The color of the triangle
     */
    public Triangle(int width, int height, int xPosition, int yPosition, String color) {
        super(xPosition, yPosition, color); // Call parent constructor
        this.width = width;
        this.height = height;
    }

    /**
     * Draw the triangle with current specifications on screen.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
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
     * Scale the triangle by a factor.
     * @param factor The scaling factor (1.0 = no change, 2.0 = double size, 0.5 = half size)
     */
    @Override
    public void scale(double factor) {
        if (factor > 0) {
            changeSize((int)(height * factor), (int)(width * factor));
        }
    }

    /**
     * Calculate the area of the triangle.
     * @return The area in square pixels
     */
    @Override
    public double getArea() {
        return (width * height) / 2.0;
    }

    /**
     * Check if a point is inside the triangle (rough approximation).
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @return true if the point is approximately inside the triangle
     */
    @Override
    public boolean contains(int x, int y) {
        // Simple bounding box check for approximation
        int leftBound = xPosition - width/2;
        int rightBound = xPosition + width/2;
        int topBound = yPosition;
        int bottomBound = yPosition + height;
        return x >= leftBound && x <= rightBound && y >= topBound && y <= bottomBound;
    }

    /**
     * Create a copy of this triangle.
     * @return A new Triangle object with the same properties
     */
    @Override
    public Triangle copy() {
        Triangle newTriangle = new Triangle(width, height, xPosition, yPosition, color);
        if (isVisible) {
            newTriangle.makeVisible();
        }
        return newTriangle;
    }

    // Triangle-specific getters and methods

    /**
     * Get the current width of the triangle.
     * @return The base width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the current height of the triangle.
     * @return The height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Calculate the perimeter of the triangle (assuming isosceles triangle).
     * @return The approximate perimeter in pixels
     */
    public double getPerimeter() {
        // For an isosceles triangle with base = width and height = height
        double sideLength = Math.sqrt(Math.pow(width/2.0, 2) + Math.pow(height, 2));
        return width + 2 * sideLength;
    }

    /**
     * Check if this triangle is equilateral (rough check).
     * @return true if width and height suggest an equilateral triangle
     */
    public boolean isEquilateral() {
        // Rough approximation: for equilateral triangle, height ≈ width * √3/2
        double expectedHeight = width * Math.sqrt(3) / 2.0;
        return Math.abs(height - expectedHeight) < 5; // 5 pixel tolerance
    }

    /**
     * Get the center x coordinate of the triangle.
     * @return The center x coordinate
     */
    public int getCenterX() {
        return xPosition;
    }

    /**
     * Get the center y coordinate of the triangle.
     * @return The center y coordinate
     */
    public int getCenterY() {
        return yPosition + height / 2;
    }

    /**
     * Set only the width of the triangle.
     * @param newWidth The new width in pixels
     */
    public void setWidth(int newWidth) {
        if (newWidth >= 0) {
            changeSize(height, newWidth);
        }
    }

    /**
     * Set only the height of the triangle.
     * @param newHeight The new height in pixels
     */
    public void setHeight(int newHeight) {
        if (newHeight >= 0) {
            changeSize(newHeight, width);
        }
    }

    /**
     * Rotate the triangle (visual approximation by changing orientation).
     * @param degrees Rotation angle in degrees (currently only supports 180°)
     */
    public void rotate(int degrees) {
        if (degrees == 180) {
            // Flip the triangle by moving it down by its height
            moveVertical(height);
        }
    }

    /**
     * Get the coordinates of the three vertices.
     * @return Array of [x1, y1, x2, y2, x3, y3] coordinates
     */
    public int[] getVertices() {
        return new int[]{
            xPosition, yPosition, // Top vertex
            xPosition + width/2, yPosition + height, // Bottom right
            xPosition - width/2, yPosition + height // Bottom left
        };
    }

    /**
     * Get string representation of the triangle.
     * @return String with triangle properties
     */
    @Override
    public String toString() {
        return String.format("Triangle[width=%d, height=%d, position=(%d,%d), color=%s, visible=%b]",
            width, height, xPosition, yPosition, color, isVisible);
    }

    /**
     * Check if two triangles are equal.
     * @param obj The object to compare with
     * @return true if the triangles have the same properties
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Triangle triangle = (Triangle) obj;
        return width == triangle.width && height == triangle.height;
    }

    /**
     * Generate hash code for the triangle.
     * @return Hash code based on triangle properties
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}