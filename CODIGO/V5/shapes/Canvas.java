package shapes
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
public class Canvas {

    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("SilkRoad Shapes Demo - Refactored", 1920, 1080, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    // ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /**
     * Create a Canvas.
     * @param title title to appear in Canvas Frame
     * @param width the desired width for the canvas
     * @param height the desired height for the canvas
     * @param bgColour the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<>();
        shapes = new HashMap<>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible boolean value representing the desired visibility of
     * the canvas (true or false)
     */
    public void setVisible(boolean visible) {
        if (graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
        if (visible) {
            frame.toFront();
        }
    }

    /**
     * Draw a given shape onto the canvas.
     * Enhanced to work with both legacy shapes and new Shape hierarchy.
     * @param referenceObject an object to define identity for this shape
     * @param color the color of the shape
     * @param shape the shape object to be drawn on the canvas
     */
    public void draw(Object referenceObject, String color, java.awt.Shape shape) {
        objects.remove(referenceObject); // just in case it was already there
        objects.add(referenceObject); // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Enhanced draw method that works directly with our refactored Shape objects.
     * This method automatically extracts the geometric shape and color.
     * @param shapeObject The Shape object from our hierarchy (Circle, Rectangle, Triangle)
     */
    public void draw(Shape shapeObject) {
        if (shapeObject == null) return;

        // Create the appropriate Java AWT Shape based on our Shape type
        java.awt.Shape awtShape = createAWTShape(shapeObject);
        if (awtShape != null) {
            draw(shapeObject, shapeObject.getColor(), awtShape);
        }
    }

    /**
     * Create an AWT Shape from our custom Shape objects.
     * @param shapeObject Our custom Shape object
     * @return Corresponding AWT Shape for drawing
     */
    private java.awt.Shape createAWTShape(Shape shapeObject) {
        if (shapeObject instanceof Circle) {
            Circle circle = (Circle) shapeObject;
            return new java.awt.geom.Ellipse2D.Double(
                circle.getXPosition(), 
                circle.getYPosition(),
                circle.getDiameter(), 
                circle.getDiameter()
            );
        } else if (shapeObject instanceof Rectangle) {
            Rectangle rect = (Rectangle) shapeObject;
            return new java.awt.Rectangle(
                rect.getXPosition(), 
                rect.getYPosition(),
                rect.getWidth(), 
                rect.getHeight()
            );
        } else if (shapeObject instanceof Triangle) {
            Triangle triangle = (Triangle) shapeObject;
            int[] vertices = triangle.getVertices();
            int[] xpoints = {vertices[0], vertices[2], vertices[4]};
            int[] ypoints = {vertices[1], vertices[3], vertices[5]};
            return new Polygon(xpoints, ypoints, 3);
        }
        return null;
    }

    /**
     * Erase a given shape from the screen.
     * @param referenceObject the shape object to be erased
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * Enhanced version with support for more colors and case-insensitive matching.
     * @param colorString the new colour for the foreground of the Canvas
     */
    public void setForegroundColor(String colorString) {
        if (graphic != null) {
            graphic.setColor(getColorFromString(colorString));
        }
    }

    /**
     * Convert a color string to a Color object.
     * Enhanced with more color options and case-insensitive matching.
     * @param colorString The color name as string
     * @return The corresponding Color object
     */
    private Color getColorFromString(String colorString) {
        if (colorString == null) return Color.black;
        String color = colorString.toLowerCase().trim();
        switch (color) {
            case "red": return Color.red;
            case "black": return Color.black;
            case "blue": return Color.blue;
            case "yellow": return Color.yellow;
            case "green": return Color.green;
            case "magenta": return Color.magenta;
            case "cyan": return Color.cyan;
            case "white": return Color.white;
            case "orange": return Color.orange;
            case "pink": return Color.pink;
            case "gray": case "grey": return Color.gray;
            case "darkgray": case "darkgrey": return Color.darkGray;
            case "lightgray": case "lightgrey": return Color.lightGray;
            case "purple": return new Color(128, 0, 128);
            case "brown": return new Color(139, 69, 19);
            case "navy": return new Color(0, 0, 128);
            case "maroon": return new Color(128, 0, 0);
            case "olive": return new Color(128, 128, 0);
            case "lime": return new Color(0, 255, 0);
            case "aqua": return new Color(0, 255, 255);
            case "silver": return new Color(192, 192, 192);
            case "gold": return new Color(255, 215, 0);
            case "turquoise": return new Color(64, 224, 208);
            case "indigo": return new Color(75, 0, 130);
            case "violet": return new Color(238, 130, 238);
            case "crimson": return new Color(220, 20, 60);
            case "coral": return new Color(255, 127, 80);
            default: return Color.black;
        }
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param milliseconds the number of milliseconds to wait
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // ignoring exception at the moment
        }
    }

    /**
     * Get the current size of the canvas.
     * @return Dimension object with width and height
     */
    public Dimension getSize() {
        return canvas.getSize();
    }

    /**
     * Clear the entire canvas.
     */
    public void clear() {
        objects.clear();
        shapes.clear();
        redraw();
    }

    /**
     * Set the background color of the canvas.
     * @param colorString The new background color
     */
    public void setBackgroundColor(String colorString) {
        backgroundColour = getColorFromString(colorString);
        redraw();
    }

    /**
     * Get the number of shapes currently on the canvas.
     * @return Number of shapes
     */
    public int getShapeCount() {
        return objects.size();
    }

    /**
     * Check if the canvas is currently visible.
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return frame.isVisible();
    }

    /**
     * Get all shapes currently on the canvas.
     * @return List of all shape objects
     */
    public List<Object> getAllShapes() {
        return new ArrayList<>(objects);
    }

    /**
     * Remove all shapes of a specific type.
     * @param shapeType The class type to remove
     */
    public void removeShapesByType(Class<?> shapeType) {
        objects.removeIf(obj -> obj.getClass().equals(shapeType));
        shapes.entrySet().removeIf(entry -> entry.getKey().getClass().equals(shapeType));
        redraw();
    }

    /**
     * Highlight a specific shape by drawing a border around it.
     * @param referenceObject The shape to highlight
     * @param borderColor The color of the border
     */
    public void highlightShape(Object referenceObject, String borderColor) {
        ShapeDescription desc = shapes.get(referenceObject);
        if (desc != null && graphic != null) {
            graphic.setStroke(new BasicStroke(3.0f));
            graphic.setColor(getColorFromString(borderColor));
            graphic.draw(desc.shape);
            graphic.setStroke(new BasicStroke(1.0f)); // Reset to default
            canvas.repaint();
        }
    }

    /**
     * Add text to the canvas at specified coordinates.
     * @param text The text to display
     * @param x X coordinate
     * @param y Y coordinate
     * @param color Text color
     */
    public void drawText(String text, int x, int y, String color) {
        if (graphic != null) {
            graphic.setColor(getColorFromString(color));
            graphic.setFont(new Font("Arial", Font.BOLD, 14));
            graphic.drawString(text, x, y);
            canvas.repaint();
        }
    }

    /**
     * Draw a grid on the canvas to help with positioning.
     * @param spacing Grid spacing in pixels
     * @param color Grid color
     */
    public void drawGrid(int spacing, String color) {
        if (graphic != null) {
            Dimension size = canvas.getSize();
            graphic.setColor(getColorFromString(color));
            graphic.setStroke(new BasicStroke(0.5f));

            // Vertical lines
            for (int x = 0; x < size.width; x += spacing) {
                graphic.drawLine(x, 0, x, size.height);
            }

            // Horizontal lines
            for (int y = 0; y < size.height; y += spacing) {
                graphic.drawLine(0, y, size.width, y);
            }

            graphic.setStroke(new BasicStroke(1.0f)); // Reset to default
            canvas.repaint();
        }
    }

    /**
     * Redraw all shapes currently on the Canvas.
     */
    private void redraw() {
        if (graphic != null) {
            erase();
            for (Object obj : objects) {
                ShapeDescription desc = shapes.get(obj);
                if (desc != null) {
                    desc.draw(graphic);
                }
            }
            canvas.repaint();
        }
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase() {
        if (graphic != null) {
            Color original = graphic.getColor();
            graphic.setColor(backgroundColour);
            Dimension size = canvas.getSize();
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(original);
        }
    }

    /**
     * Close the canvas window.
     */
    public void close() {
        frame.setVisible(false);
        frame.dispose();
        canvasSingleton = null;
    }

    /**
     * Set the title of the canvas window.
     * @param title New title for the window
     */
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /**
     * Resize the canvas to new dimensions.
     * @param width New width
     * @param height New height
     */
    public void resize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
        // Recreate the image buffer
        canvasImage = canvas.createImage(width, height);
        graphic = (Graphics2D) canvasImage.getGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.setColor(backgroundColour);
        graphic.fillRect(0, 0, width, height);
        redraw();
    }

    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        @Override
        public void paint(Graphics g) {
            if (canvasImage != null) {
                g.drawImage(canvasImage, 0, 0, null);
            }
        }
    }

    /************************************************************************
     * Inner class ShapeDescription - stores information about a shape
     * including its geometry and color.
     */
    private class ShapeDescription {
        private java.awt.Shape shape;
        private String colorString;

        public ShapeDescription(java.awt.Shape shape, String color) {
            this.shape = shape;
            this.colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.fill(shape);
            graphic.setColor(Color.black);
            graphic.draw(shape);
        }
    }
}