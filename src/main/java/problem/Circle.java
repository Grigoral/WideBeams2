package main.java.problem;


import javax.media.opengl.GL2;

public class Circle {

	
	private Point center;
	private double r;//радиус
	boolean isSolution = false;
	
	public Circle(Point center, double r) {
		this.center = center;
		this.r = r;
	}
	
	public Circle() {}
	
	 /**
     * Получить случайную окружность
     *
     * @return случайная окружность
     */
    static Circle getRandomCircle() {
        double radius  = Math.random();
        double centerX  = Math.random()*(2-2*radius)-1+radius;
        double centerY  = Math.random()*(2-2*radius)-1+radius;
   
        
        return new Circle(new Point(centerX,centerY), radius);
    }
	
    /**
     * Рисование треугольника
     *
     * @param gl переменная OpenGl для рисования
     */
    void render(GL2 gl) {
    	if(!isSolution)
            gl.glColor3d(0.0, 0.0, 1.0);
    	else   
    		gl.glColor3d(0.0, 1.0, 0.0);
            gl.glBegin(GL2.GL_LINE_LOOP);
            for(int i = 0; i < 30; i++)
            {
            double angle = 2.0 * 3.1415926 * i / 30;
            double dx = r * Math.cos(angle);
            double dy = r * Math.sin(angle);
            gl.glVertex2d(center.getX()+dx,center.getY()+ dy);
            }
             
            gl.glEnd(); 
            gl.glLineWidth(1);
    }
	
	
	
	
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point center) {
		this.center = center;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	
	
}
