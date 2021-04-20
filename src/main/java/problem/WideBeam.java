package main.java.problem;

import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class WideBeam {

	private Point apex1;
	private Point apex2;
	boolean isSolution = false;
	
	public WideBeam(Point apex1, Point apex2) {
		this.apex1 = apex1;
		this.apex2 = apex2;
	}
	
	public WideBeam() {}
	
	
	 /**
     * Получить случайный 'широкий угол'
     *
     * @return случайный 'широкий угол'
     */
    static WideBeam getRandomWideBeam() {
        Random r = new Random();
        double apex1x = (double) r.nextInt(50) / 25 - 1; 
        double apex1y = (double) r.nextInt(50) / 25 - 1;
        double apex2y = (double) r.nextInt(50) / 25 - 1;
        
        return new WideBeam(new Point(apex1x,apex1y), new Point(apex1x,apex2y));
    }
    
    /**
     * Рисование 'широкого угла'
     *
     * @param gl переменная OpenGl для рисования
     */
    void render(GL2 gl) {
    	if(!isSolution)
    		gl.glColor3d(1.0,0.0,0.0); 
    	else   
    		gl.glColor3d(0.0, 1.0, 0.0);
            gl.glBegin(GL.GL_LINE_LOOP);
            if(apex1!=null && apex2!=null) {
            	gl.glVertex2d(apex1.x,apex1.y);
            	gl.glVertex2d(2.0,apex1.y);
            	gl.glVertex2d(2.0,apex2.y);
            	gl.glVertex2d(apex1.x,apex2.y);
            	
            }
            gl.glEnd();
            gl.glLineWidth(1);
    }
	
	
	
	
	public Point getApex1() {
		return apex1;
	}
	public void setApex1(Point apex1) {
		this.apex1 = apex1;
	}
	public Point getApex2() {
		return apex2;
	}
	public void setApex2(Point apex2) {
		this.apex2 = apex2;
	}
}
