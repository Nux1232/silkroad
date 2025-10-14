 /**
 * Sistema de coordenadas en espiral para el simulador SilkRoad
 * Esta clase calcula las posiciones exactas siguiendo el patrón de espiral cuadrada
 */
public class SpiralPath {

    private int[] spiralX;  // Coordenadas X de cada posición en la espiral
    private int[] spiralY;  // Coordenadas Y de cada posición en la espiral
    private int length;     // Número total de posiciones en la espiral
    private int startX;     // Punto de inicio X
    private int startY;     // Punto de inicio Y
    private int segmentSize; // Tamaño de cada segmento
    private int increment;   // Incremento entre segmentos

    /**
     * Constructor que calcula todas las posiciones de la espiral
     * @param length Número de posiciones en la espiral
     * @param startX Coordenada X inicial
     * @param startY Coordenada Y inicial
     * @param segmentSize Tamaño inicial del segmento
     * @param increment Incremento entre segmentos
     */
    public SpiralPath(int length, int startX, int startY, int segmentSize, int increment) {
        this.length = length;
        this.startX = startX;
        this.startY = startY;
        this.segmentSize = segmentSize;
        this.increment = increment;

        this.spiralX = new int[length];
        this.spiralY = new int[length];

        calculateSpiralPositions();
    }

    /**
     * Calcula todas las posiciones siguiendo el patrón de espiral cuadrada
     */
    private void calculateSpiralPositions() {
        int x = startX;
        int y = startY;
        int dx = 1; // dirección horizontal: 1=derecha, -1=izquierda
        int dy = 0; // dirección vertical: 1=abajo, -1=arriba
        int currentLength = segmentSize;
        int dirCount = 0;
        int positionIndex = 0;

        // Posición inicial (centro de la espiral)
        spiralX[0] = x;
        spiralY[0] = y;
        positionIndex++;

        while (positionIndex < length) {
            // Recorrer el segmento actual
            for (int step = 0; step < currentLength && positionIndex < length; step++) {
                x += dx * (segmentSize / 2); // Avanzar en la dirección actual
                y += dy * (segmentSize / 2);

                spiralX[positionIndex] = x;
                spiralY[positionIndex] = y;
                positionIndex++;
            }

            // Cambiar dirección (rotar 90° a la izquierda)
            int temp = dx;
            dx = -dy;
            dy = temp;

            // Incrementar longitud cada dos cambios de dirección
            dirCount++;
            if (dirCount == 2) {
                dirCount = 0;
                currentLength += increment;
            }
        }
    }

    /**
     * Obtener la coordenada X de una posición específica
     * @param position Posición en la espiral (0-based)
     * @return Coordenada X
     */
    public int getX(int position) {
        if (position >= 0 && position < length) {
            return spiralX[position];
        }
        return startX; // Retornar posición inicial si está fuera de rango
    }

    /**
     * Obtener la coordenada Y de una posición específica
     * @param position Posición en la espiral (0-based)
     * @return Coordenada Y
     */
    public int getY(int position) {
        if (position >= 0 && position < length) {
            return spiralY[position];
        }
        return startY; // Retornar posición inicial si está fuera de rango
    }

    /**
     * Obtener las coordenadas como array [x, y]
     * @param position Posición en la espiral
     * @return Array con [x, y]
     */
    public int[] getCoordinates(int position) {
        return new int[]{getX(position), getY(position)};
    }

    /**
     * Obtener el número total de posiciones
     * @return Longitud de la espiral
     */
    public int getLength() {
        return length;
    }

    /**
     * Verificar si una posición es válida
     * @param position Posición a verificar
     * @return true si es válida
     */
    public boolean isValidPosition(int position) {
        return position >= 0 && position < length;
    }

    /**
     * Obtener la siguiente posición válida
     * @param currentPosition Posición actual
     * @return Siguiente posición (o la misma si es la última)
     */
    public int getNextPosition(int currentPosition) {
        if (currentPosition < length - 1) {
            return currentPosition + 1;
        }
        return currentPosition; // Quedarse en la última posición
    }

    /**
     * Obtener la posición anterior válida
     * @param currentPosition Posición actual
     * @return Posición anterior (o la misma si es la primera)
     */
    public int getPreviousPosition(int currentPosition) {
        if (currentPosition > 0) {
            return currentPosition - 1;
        }
        return currentPosition; // Quedarse en la primera posición
    }

    /**
     * Calcular la distancia entre dos posiciones en la espiral
     * @param pos1 Primera posición
     * @param pos2 Segunda posición
     * @return Distancia euclidiana
     */
    public double getDistance(int pos1, int pos2) {
        if (!isValidPosition(pos1) || !isValidPosition(pos2)) {
            return 0.0;
        }

        int dx = spiralX[pos2] - spiralX[pos1];
        int dy = spiralY[pos2] - spiralY[pos1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Obtener todas las posiciones como string para debugging
     * @return String con todas las coordenadas
     */
    public String getAllPositions() {
        StringBuilder sb = new StringBuilder();
        sb.append("Spiral Path Positions:\n");
        for (int i = 0; i < length; i++) {
            sb.append(String.format("Pos %d: (%d, %d)\n", i, spiralX[i], spiralY[i]));
        }
        return sb.toString();
    }

    /**
     * Crear una copia del path con diferente longitud
     * @param newLength Nueva longitud
     * @return Nuevo SpiralPath
     */
    public SpiralPath resize(int newLength) {
        return new SpiralPath(newLength, startX, startY, segmentSize, increment);
    }
}