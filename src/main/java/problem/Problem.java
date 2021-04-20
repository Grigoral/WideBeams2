package main.java.problem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.opengl.GL2;

public class Problem {

	/**
     * текст задачи
     */
    public static final String PROBLEM_TEXT = "ПОСТАНОВКА ЗАДАЧИ:\n" +
            "На плоскости задано множество 'широких лучей' и множество окружностей.\n" +
            "Найти такую пару 'широкий луч' - окружность, что фигура, находящаяся внутри \n" + 
            "'широкого луча' и окружности имеет максимальную площадь.\n";

    /**
     * заголовок окна
     */
    public static final String PROBLEM_CAPTION = "Итоговый проект ученика ??????";

    /**
     * путь к файлам
     */
    private static final String FILE_NAME_CIRCLE = "circles.txt";
    private static final String FILE_NAME_WIDEBEAM = "widebeams.txt";

    /**
     * список точек
     */
    ArrayList<Circle> circles;
    ArrayList<WideBeam> wideBeams;
    
    public Problem() {
    	circles=new ArrayList<>();
    	wideBeams = new ArrayList<>();
    }
    
    /**
     * Решить задачу
     */
    public void solve() {
    	
    	for(int i=0; i<wideBeams.size(); i++) {
    		wideBeams.get(i).isSolution=false;
    	}
    	for(int i=0; i<circles.size(); i++) {
    		circles.get(i).isSolution=false;
    	}
    	double max=0;
    	int totalI=-1,totalJ=-1;
    	
    	 for(int i=0; i<wideBeams.size(); i++) {
         	for(int j=0; j<circles.size(); j++) {
         	
         		Point leftUpperCorner = new Point();
         		if(wideBeams.get(i).getApex1().getY() > wideBeams.get(i).getApex2().getY())
         		  leftUpperCorner= wideBeams.get(i).getApex1();
         		  else
         			  leftUpperCorner= wideBeams.get(i).getApex2();
         	
         	double	vertLineX = leftUpperCorner.getX();
         	double	upLineY = leftUpperCorner.getY();
         	double height =  Math.abs(wideBeams.get(i).getApex1().getY() - wideBeams.get(i).getApex2().getY());
         	double	x0 =circles.get(j).getCenter().getX();
         	double	y0 =circles.get(j).getCenter().getY();
         	double	radius =circles.get(j).getR();
         	
         	if(x0+radius>=vertLineX) {//центр минус  радиус правее короткой стороны прямоугольника+
         	
         		// 1. если центр окружности ниже нижней стороны треугольника и вершина окружности внутри прямоугольника
         		if(y0<=upLineY-height
         				&& y0+radius<=upLineY) {
         			if(squareSegment(radius, x0,y0,upLineY-height, true)>max) {
         				max= squareSegment(radius, x0,y0,upLineY-height, true);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//2. если центр окружности выше верхей стороны треугольника и вершина окружности внутри прямоугольника
         		if(y0<=upLineY
         				&& y0+radius<=upLineY +height 
         				) {
         			if(Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY, true)>max) {
         				max=Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY, true);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//3. если центр окружности внутри прямоугольника и ближе к вержней строне
         		if(y0<=upLineY
         				&& y0-radius>=upLineY-height
         				) {
         			if(Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY, true)>max) {
         				max=Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY, true);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//4. если центр окружности внутри прямоугольника и ближе к нижней строне
         		if(y0>=upLineY-height && y0<=upLineY
         				&& y0+radius<=upLineY) {
         			if(Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY-height, true)>max) {
         				max=Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY-height, true);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//5. центр внутри прямоугольника, но окружжность вылазиет с двух сторон
         		if(y0>=upLineY-height && y0<=upLineY
         				&& y0-radius<upLineY-height && y0+radius>upLineY) {
         			if(Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY+height, true)- squareSegment(radius, x0,y0,upLineY, true)>max) {
         				max=Math.PI*radius*radius- squareSegment(radius, x0,y0,upLineY+height, true)- squareSegment(radius, x0,y0,upLineY, true);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         			//6. окружность целиком внутри прямоугольника
         		if(
         				 y0-radius>=upLineY-height && y0+radius<=upLineY) {
         			if(Math.PI*radius*radius>max) {
         				max=Math.PI*radius*radius;
         			     totalI=i; 
      	                totalJ=j;
      				}
         			}
         	 }
         	
         	//если центр лежит левее вертикальной стороны угла, центр плюс радиус правее
         	if(x0<vertLineX && x0+radius>vertLineX) {
         		
         		//7. центр лежит выше верхней стороны, а центр плюс радиус между сторонами 
         		if(y0>=upLineY && y0-radius<upLineY 
         				&& y0-radius>=upLineY-height) {
         			           if(squareSector(radius,x0,y0,vertLineX,upLineY)>max) {
         			        	   max=squareSector(radius,x0,y0,vertLineX,upLineY);
         			        	     totalI=i; 
         				             totalJ=j;
         							}
         		}
         		//8. центр лежит ниже нижней стороны, а центр плюс радиус между сторонами 
         		if(y0<=upLineY-height && y0+radius<=upLineY 
         				&& y0-radius>=upLineY-height) {
         			           if(squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         			        	   max=squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			        	     totalI=i; 
         				             totalJ=j;
         							}
         		}
         		//9. Центр выше верхней, а центр плюс радиус ниже нижней
         		if(y0>=upLineY && y0-radius<=upLineY-height) {
			           if(squareSector(radius,x0,y0,vertLineX,upLineY)-squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
			        	   max=squareSector(radius,x0,y0,vertLineX,upLineY)-squareSector(radius,x0,y0,vertLineX,upLineY-height);
			        	     totalI=i; 
				             totalJ=j;
							}
         		} 
         		//10. Центр ниже нижней, а центр минус радиус выше верхней
         		if(y0<=upLineY-height && y0+radius>=upLineY) {
			           if(squareSector(radius,x0,y0,vertLineX,upLineY-height)
			        		   -squareSector(radius,x0,y0,vertLineX,upLineY)>max) {
			        	   max=squareSector(radius,x0,y0,vertLineX,upLineY-height)-
			        			   squareSector(radius,x0,y0,vertLineX,upLineY);
			        	     totalI=i; 
				                totalJ=j;
							}
         		} 
         		//11. центр между бесконечными сторонами, окружность пересекает только короткую сторону
         		if(y0-radius>=upLineY-height && y0+radius<=upLineY) {
         			if(squareSegment(radius, x0,y0,vertLineX, false)>max) {
         				max=squareSegment(radius, x0,y0,vertLineX, false);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//12. центр между бесконечными сторонами, окружность пересекает только длинные стороны
         		if(y0<=upLineY && y0>=upLineY-height
         				&& y0-radius<=upLineY-height && y0+radius>=upLineY) {
         			if(squareSegment(radius, x0,y0,vertLineX, false)
         					-squareSector(radius,x0,y0,vertLineX,upLineY)
         					-squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         				max=squareSegment(radius, x0,y0,vertLineX, false)
             					-squareSector(radius,x0,y0,vertLineX,upLineY)
             					-squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//13. центр между бесконечными сторонами, окружность пересекает нижнюю и короткую
         		if(y0<=upLineY && y0>=upLineY-height
         				&& y0-radius<=upLineY-height && y0+radius<=upLineY) {
         			if(squareSegment(radius, x0,y0,vertLineX, false)
         					-squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         				max=squareSegment(radius, x0,y0,vertLineX, false)
             					-squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//14. центр между бесконечными сторонами, окружность пересекает верхнюю и короткую
         		if(y0<=upLineY && y0>=upLineY-height
         				&& y0-radius>=upLineY-height && y0+radius>=upLineY) {
         			if(squareSegment(radius, x0,y0,vertLineX, false)
         					-squareSector(radius,x0,y0,vertLineX,upLineY)>max) {
         				max=squareSegment(radius, x0,y0,vertLineX, false)
             					-squareSector(radius,x0,y0,vertLineX,upLineY);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         	
         	
         	}
         	if(x0>=vertLineX && x0-radius<=vertLineX) {//если центр лежит правее вертикальной стороны угла, центр минус радиус левее
         		//15 и 19. центр выше верхней, окружность пересекает (может касаться нижней) верхнюю и короткую
         		if(y0>=upLineY
         				&& y0-radius<=upLineY && y0-radius>=upLineY-height) {
         			if(squareSegment(radius, x0,y0,upLineY, true)
         					-squareSector(radius,x0,y0,vertLineX,upLineY)>max) {
         				max=squareSegment(radius, x0,y0,vertLineX, true)
             					-squareSector(radius,x0,y0,vertLineX,upLineY);
         			     totalI=i; 
      	                 totalJ=j;
      				}
         		}
         		
         		//16 и 20. центр ниже нижней, окружность пересекает (может касаться верхней) нижнюю и короткую
         		if(y0<=upLineY-height
         				&& y0+radius>=upLineY-height && y0+radius<=upLineY) {
         			if(squareSegment(radius, x0,y0,upLineY-height, true)
         					-squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         				max=squareSegment(radius, x0,y0,upLineY-height, true)
             					-squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         	
         		//17 и 21. центр выше верхней, окружность пересекает обе длинные
         		if(y0>=upLineY
         				&&  y0-radius<upLineY-height) {
         			if(squareSegment(radius, x0,y0,upLineY, true)-
         					squareSegment(radius, x0,y0,upLineY-height, true)
         					-squareSector(radius,x0,y0,vertLineX,upLineY)
         					+squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         				max=squareSegment(radius, x0,y0,upLineY, true)-
             					squareSegment(radius, x0,y0,upLineY-height, true)
             					-squareSector(radius,x0,y0,vertLineX,upLineY)
             					+squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		
         		//18 и 22. центр ниже нижней, окружность пересекает обе длинные
         		if(y0<=upLineY-height
         				&&  y0+radius>=upLineY) {
         			if(squareSegment(radius, x0,y0,upLineY-height, true)-
         					squareSegment(radius, x0,y0,upLineY, true)
         					-squareSector(radius,x0,y0,vertLineX,upLineY-height)
         					+squareSector(radius,x0,y0,vertLineX,upLineY)>max) {
         				max=squareSegment(radius, x0,y0,upLineY-height, true)-
             					squareSegment(radius, x0,y0,upLineY, true)
             					-squareSector(radius,x0,y0,vertLineX,upLineY-height)
             					+squareSector(radius,x0,y0,vertLineX,upLineY);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         	
         		//23, 24, 25 и 26. центр между длинными, окружность пересекает длинные и может персечь короткую
         		if(y0<=upLineY && y0>=upLineY-height
         				&&  y0+radius>=upLineY &&  y0-radius<=upLineY-height) {
         			if(Math.PI*radius*radius-  squareSegment(radius, x0,y0,upLineY, true)
         					-  squareSegment(radius, x0,y0,upLineY-height, true)
         					-  squareSegment(radius, x0,y0,vertLineX, false)
         					+squareSector(radius,x0,y0,vertLineX,upLineY)
         					+squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         				max=Math.PI*radius*radius-  squareSegment(radius, x0,y0,upLineY, true)
             					-  squareSegment(radius, x0,y0,upLineY-height, true)
             					-  squareSegment(radius, x0,y0,vertLineX, false)
             					+squareSector(radius,x0,y0,vertLineX,upLineY)
             					+squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//27, 29 и 32. Центр между длинными, окружность пересекает или касается верхней (нижней) и пересекает короткую
         		if(y0<=upLineY && y0>=upLineY-height 
         				&& y0+radius>=upLineY && y0-radius<=upLineY-height) {
         			if(Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY, true)
         					-  squareSegment(radius, x0,y0,vertLineX, false)>max) {
         				max=Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY, true)
             					-  squareSegment(radius, x0,y0,vertLineX, false);
         			     totalI=i; 
      	                 totalJ=j;
      				}
         		}
         		//28 и 33. Центр между длинными, окружность пересекает короткую и нижнюю (может касаться)
         		if(y0<=upLineY && y0>=upLineY+height 
         				&& y0+radius<=upLineY && y0-radius<=upLineY-height) {
         			if(Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY-height, true)
         					-  squareSegment(radius, x0,y0,vertLineX, false)>max) {
         				max=Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY-height, true)
             					-  squareSegment(radius, x0,y0,vertLineX, false);
         			     totalI=i; 
      	                 totalJ=j;
      				}
         		}
         		//30. Центр между длинными, окружность пересекает короткую и нижнюю и касается верхней
         		if(y0<upLineY && y0>=upLineY-height 
         				&& y0+radius==upLineY && y0-radius<upLineY) {
         			if(Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY-height, true)
         					-  squareSegment(radius, x0,y0,vertLineX, false)
         					+squareSector(radius,x0,y0,vertLineX,upLineY-height)>max) {
         				max=Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY-height, true)
             					-  squareSegment(radius, x0,y0,vertLineX, false)
             					+squareSector(radius,x0,y0,vertLineX,upLineY-height);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         		//31. Центр между длинными, окружность пересекает короткую и верхнюю и касается нижней
         		if(y0<upLineY && y0>upLineY-height 
         				&& y0+radius>upLineY && y0-radius==upLineY-height) {
         			if(Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY, true)
         					-  squareSegment(radius, x0,y0,vertLineX, false)
         					+squareSector(radius,x0,y0,vertLineX,upLineY)>max) {
         				max=Math.PI*radius*radius-squareSegment(radius, x0,y0,upLineY, true)
             					-  squareSegment(radius, x0,y0,vertLineX, false)
             					+squareSector(radius,x0,y0,vertLineX,upLineY);
         			     totalI=i; 
      	                totalJ=j;
      				}
         		}
         	}
           }
         }
    	if(totalI>=0 && totalJ>=0) {
    		
    		wideBeams.get(totalI).isSolution=true;
    		circles.get(totalJ).isSolution=true;
    	}
    }
    
    public double squareSector(double radius, double x0, double y0, double x1, double y1) {
    	
    	double x2=0;
    	double y2=0;
    	//находим точки персечения с окружностью
    	if(x1>x0 && y1<y0) {
    		x2=x0+Math.sqrt(radius*radius-(y1-y0)*(y1-y0));
    		y2=y0+Math.sqrt(radius*radius-(x1-x0)*(x1-x0));
    	}
    	
    	if(x1<x0 && y1<y0) {
    		x2=x0-Math.sqrt(radius*radius-(y1-y0)*(y1-y0));
    		y2=y0+Math.sqrt(radius*radius-(x1-x0)*(x1-x0));
    	}
    	
    	if(x1<x0 && y1>y0 ) {
    		x2=x0-Math.sqrt(radius*radius-(y1-y0)*(y1-y0));
    		y2=y0-Math.sqrt(radius*radius-(x1-x0)*(x1-x0));
    	}
    	
    	if(x1>x0 && y1>y0) {
    		x2=x0+Math.sqrt(radius*radius-(y1-y0)*(y1-y0));
    		y2=y0-Math.sqrt(radius*radius-(x1-x0)*(x1-x0));
    	}
    	double centerX=(x2+x1)/2;
    	double centerY=(y2+y1)/2;
    	double sinA=0;
    	sinA=Math.sqrt((x2-centerX)*(x2-centerX)+(y2-centerY)*(y2-centerY))/radius;
    	//площадь сектора будет площадь сегмента плюс площадь треугольника
    	double square=0.5*(Math.asin(sinA)-sinA)*radius*radius+0.5*Math.abs(y2-y1)*Math.abs(x2-x1);
    	return square;
    }
	
    //находим площадь  сегмента
    	public double squareSegment(/*WideBeam wideBeam,*/ double radius, double x0, double y0, double rectangleSideCoord, boolean horizontal ) {
    	//находим точки пересечения  стороны прямоугольника и круга
    		double[] array=new double[2];
    	if(horizontal) {
    		array[0]= x0+Math.sqrt(radius*radius-(rectangleSideCoord-y0)*(rectangleSideCoord-y0));
    		array[1]  =      x0-Math.sqrt(radius*radius-(rectangleSideCoord-y0)*(rectangleSideCoord-y0));
    	}
    	else {
    	array[0]= y0+Math.sqrt(radius*radius-(rectangleSideCoord-x0)*(rectangleSideCoord-x0));
    	array[1]= y0-Math.sqrt(radius*radius-(rectangleSideCoord-x0)*(rectangleSideCoord-x0));
    	}
    	
    	double center = (array[0]+array[1])/2;
    	
    	double sinA=(array[0]-center)/radius;
    	
    	double square=0.5*(Math.asin(sinA)-sinA)*radius*radius;
    	
    	return square;
    }
    

    
	 /**
     * Загрузить задачу из файла
     */
    public void loadFromFile() {
    	circles.clear();
    	wideBeams.clear();
        try {
            File file = new File(FILE_NAME_CIRCLE);
            Scanner sc = new Scanner(file);
            // пока в файле есть непрочитанные строки
            while (sc.hasNextLine()) {
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                double r = sc.nextDouble();
                //int setVal = sc.nextInt();
                if(sc.hasNextLine())
                	sc.nextLine();
                Circle circle = new Circle(new Point(x,y), r);
                circles.add(circle);
            }
            sc.close();
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из файла: " + ex);
        }
        
        try {
            File file = new File(FILE_NAME_WIDEBEAM);
            Scanner sc = new Scanner(file);
            // пока в файле есть непрочитанные строки
            while (sc.hasNextLine()) {
                double x1 = sc.nextDouble();
                double y1 = sc.nextDouble();
                double y2 = sc.nextDouble();
                //int setVal = sc.nextInt();
                if(sc.hasNextLine())
                	sc.nextLine();
                WideBeam wideBeam = new WideBeam(new Point(x1,y1), new Point(x1,y2));
                wideBeams.add(wideBeam);
            }
            sc.close();
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из файла: " + ex);
        }
    }

    /**
     * Сохранить задачу в файл
     */
    public void saveToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME_CIRCLE));
            for (Circle circle : circles) {
                out.printf("%.2f %.2f %.2f\n", circle.getCenter().getX(), circle.getCenter().getY(), circle.getR() );
            }
            out.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи в файл: " + ex);
        }
        
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME_WIDEBEAM));
            for (WideBeam wideBeam : wideBeams) {
                out.printf("%.2f %.2f %.2f\n", wideBeam.getApex1().getX(), wideBeam.getApex1().getY(), wideBeam.getApex2().getY() );
            }
            out.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи в файл: " + ex);
        }
    }
    
    /**
     * Добавить окружность
     *
     * @param x      координата X 
     * @param y      координата Y 
     */
    public void addCircle(double x, double y, double r) {
    	Point center = new Point(x,y);
        Circle circle = new Circle(center, r);
        circles.add(circle);
    }
    
    /**
     * Добавить 'широкий угол'
     *
     * @param x1      координата X1 
     * @param y1      координата Y1 
     * @param x2      координата X2 
     * @param y2      координата Y2 
     */
    public void addWideBeam(double x1, double y1,  double y2) {
    	wideBeams.add(new WideBeam(new Point(x1,y1), new Point(x1,y2)));
    }
    
    /**
     * Добавить случайную окружность
     *
     */
    public void addRandomCircle(int n) {
    	
    	  for (int i = 0; i < n; i++) {
              Circle p = Circle.getRandomCircle();
              circles.add(p);
          }
    }
    
    /**
     * Добавить случайный широкий угол
     *
     */
    public void addRandomWideBeam(int n) {
    	
    	  for (int i = 0; i < n; i++) {
    		  WideBeam p = WideBeam.getRandomWideBeam();
    		  wideBeams.add(p);
          }
    }
    
    /**
     * Очистить задачу
     */
    public void clear() {
    	circles.clear();
    	wideBeams.clear();
    }
	
	
	/**
     * Нарисовать задачу
     *
     * @param gl переменная OpenGL для рисования
     */
    public void render(GL2 gl) {
    	 for (Circle circle : circles) {
    		 circle.render(gl);
         }
    	 for (WideBeam wideBeam : wideBeams) {
    		 wideBeam.render(gl);
         }
    }
}
