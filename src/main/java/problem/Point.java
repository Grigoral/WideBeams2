package main.java.problem;

public class Point {

    /**
     * x - координата точки
     */
    double x;
    /**
     * y - координата точки
     */
    double y;

    /**
     * Конструктор точки
     *
     * @param x         координата x
     * @param y         координата y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point() {}
    
    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }
    
    /**
     * Получить случайную точку
     *
     * @return случайная точка
     */
  /*  static Point getRandomPoint() {
        Random r = new Random();
        double nx = (double) r.nextInt(50) / 25 - 1; 
        double ny = (double) r.nextInt(50) / 25 - 1;
        return new Point(nx, ny);
    }*/
}
