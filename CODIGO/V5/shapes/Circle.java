package shapes;
import java.awt.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * Refactored to inherit from Shape class for better code reusability.
 *
 * @author Michael Kolling and David J. Barnes (Refactored for Ciclo 1-3)
 * @version 3.0 (October 2025)
 */
public class Circle extends Shape {

    public static final double PI = 3.1416;
    private int diameter;

    /**
     * Create a new circle at default position with default color.
     */
    public Circle() {
        super(20, 15, "blue"); // Call parent constructor
        diameter = 30;
    }

    /**
     * Create a new circle with specified parameters.
     * @param diameter The diameter of the circle
     * @param xPosition The x position of the circle
     * @param yPosition The y position of the circle
     * @param color The color of the circle
     */
    public Circle(int diameter, int xPosition, int yPosition, String color) {
        super(xPosition, yPosition, color); // Call parent constructor
        this.diameter = diameter;
    }

    /**
     * Draw the circle with current specifications on screen.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.geom.Ellipse2D.Double(xPosition, yPosition,
                    diameter, diameter));
            canvas.wait(10);
        }
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter) {
        if (newDiameter >= 0) {
            erase();
            diameter = newDiameter;
            draw();
        }
    }

    /**
     * Scale the circle by a factor.
     * @param factor The scaling factor (1.0 = no change, 2.0 = double size, 0.5 = half size)
     */
    @Override
    public void scale(double factor) {
        if (factor > 0) {
            changeSize((int)(diameter * factor));
        }
    }

    /**
     * Calculate the area of the circle.
     * @return The area in square pixels
     */
    @Override
    public double getArea() {
        double radius = diameter / 2.0;
        return PI * radius * radius;
    }

    /**
     * Check if a point is inside the circle.
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @return true if the point is inside the circle
     */
    @Override
    public boolean contains(int x, int y) {
        int centerX = xPosition + diameter / 2;
        int centerY = yPosition + diameter / 2;
        int radius = diameter / 2;
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return distance <= radius;
    }

    /**
     * Create a copy of this circle.
     * @return A new Circle object with the same properties
     */
    @Override
    public Circle copy() {
        Circle newCircle = new Circle(diameter, xPosition, yPosition, color);
        if (isVisible) {
            newCircle.makeVisible();
        }
        return newCircle;
    }

    // Circle-specific getters

    /**
     * Get the current diameter of the circle.
     * @return The diameter in pixels
     */
    public int getDiameter() {
        return diameter;
    }

    /**
     * Calculate the circumference of the circle.
     * @return The circumference in pixels
     */
    public double getCircumference() {
        return PI * diameter;
    }

    /**
     * Get string representation of the circle.
     * @return String with circle properties
     */
    @Override
    public String toString() {
        return String.format("Circle[diameter=%d, position=(%d,%d), color=%s, visible=%b]",
            diameter, xPosition, yPosition, color, isVisible);
    }

    /**
     * Check if two circles are equal.
     * @param obj The object to compare with
     * @return true if the circles have the same properties
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Circle circle = (Circle) obj;
        return diameter == circle.diameter;
    }

    /**
     * Generate hash code for the circle.
     * @return Hash code based on circle properties
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + diameter;
        return result;
    }
}
