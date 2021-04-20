package main.java.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

//import main.java.problem.Point;
import main.java.problem.Problem;

public class Form extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 /**
     * таймер
     */
    private final Timer timer;
    
	private JPanel root2;
    private JPanel root;
    private int widthFrame=1200;
    private int heightFrame=600;
    private JButton loadFromFileBtn;
    private JButton saveToFileBtn;
    private JButton clearBtn;
    private JButton solveBtn;
    private JLabel problemText;
    private JButton addCircle;
    private JButton addWideBeam;
    private JTextField x0PointField;
    private JTextField y0PointField;
    private JTextField radiusField;
    private JButton randomCircleBtn;
    private JButton randomWideBeamBtn;
    private JTextField circleCntField;
    private JLabel xLabel;
    private JLabel yLabel;
    private JLabel radiusLabel;
    private JLabel countPoints;
    private JLabel y1Label;
    private JLabel y2Label;
    
    private JTextField apex1X;
    private JTextField apex1Y;
    private JTextField apex2X;
    private JTextField apex2Y;
    private JTextField apex3X;
    private JTextField apex3Y;
    /**
     * рисовалка OpenGL
     */
    private final RendererGL renderer;

    
	private Form() {
		
		renderer = new RendererGL();
		
		loadFromFileBtn = new JButton("Загрузить из файла");
		saveToFileBtn = new JButton("Сохранить в файл");
		clearBtn = new JButton("Очистить");
		addCircle = new JButton("Добавить окружность");
		addWideBeam = new JButton("Добавить 'широкий угол'");
		randomCircleBtn = new JButton("Добавить случайную окружность");
		randomWideBeamBtn = new JButton("Добавить случайный 'широкий угол'");
		solveBtn = new JButton("Решить");
		circleCntField = new JTextField(7);
		radiusField = new JTextField(8);
		radiusField.setText("0.5");
		x0PointField = new JTextField(10);
		x0PointField.setText("0.0");
		y0PointField = new JTextField(10);
		y0PointField.setText("0.0");
		apex1X = new JTextField(5);
		apex1X.setText("-0.6");
		apex1Y = new JTextField(5);
		apex1Y.setText("0.0");
		apex2X = new JTextField(5);
		apex2X.setText("0.0");
		apex2Y = new JTextField(5);
		apex2Y.setText("0.8");
		apex3X = new JTextField(5);
		apex3X.setText("0.6");
		apex3Y = new JTextField(5);
		apex3Y.setText("0.0");
		
		root2 =new JPanel();
		
		JFrame  frame = new JFrame(Problem.PROBLEM_CAPTION);
		frame.setLayout(new GridLayout(1,1));
		JPanel problemTextPanel = new JPanel();
		JPanel addPointsPanel = new JPanel(new FlowLayout());
		JPanel randomPoints = new JPanel(new FlowLayout());
		JPanel actions = new JPanel();
		
		problemText = new JLabel("<html>" + Problem.PROBLEM_TEXT.replaceAll("\n", "<br>"));
        problemText.setText("<html>" + Problem.PROBLEM_TEXT.replaceAll("\n", "<br>"));
        
        problemTextPanel.add(problemText);
        
        
        xLabel = new JLabel("X0");
        addPointsPanel.add(xLabel);
        addPointsPanel.add(x0PointField);
        
        yLabel = new JLabel("Y0");
        addPointsPanel.add(yLabel);
        addPointsPanel.add(y0PointField);
        
        radiusLabel = new JLabel("Радиус");
        addPointsPanel.add(radiusLabel);
        addPointsPanel.add(radiusField);
        
        addPointsPanel.add(addCircle);
       
        xLabel = new JLabel("X");
        addPointsPanel.add(xLabel);
        addPointsPanel.add(apex1X);
        
        y1Label = new JLabel("Y1");
        addPointsPanel.add(y1Label);
        addPointsPanel.add(apex1Y);
        
        y2Label = new JLabel("Y2");
        addPointsPanel.add(y2Label);
        addPointsPanel.add(apex2Y);
        
        addPointsPanel.add(addWideBeam);

        countPoints = new JLabel("Кол-во");
        circleCntField.setText("10");
        randomPoints.add(countPoints);
        randomPoints.add(circleCntField);
        randomPoints.add(randomCircleBtn);
        randomPoints.add(randomWideBeamBtn);
        
        
		actions.add(loadFromFileBtn);
		root2.setLayout(new GridLayout(4,0));
		actions.add(clearBtn);
		actions.add(saveToFileBtn);
		actions.add(clearBtn);
		actions.add(solveBtn);
		
		root2.add(problemTextPanel);
		root2.add(addPointsPanel);
		root2.add(randomPoints);
		root2.add(actions);
		
		root2.setBackground(Color.white);
		root2.setSize(400, 600);
		
		root = new JPanel();
		root.setLayout(new BorderLayout());
		root.add(renderer.getCanvas());
		
		frame.getContentPane().add(root);
		frame.getContentPane().add(root2);

		frame.setSize(getPreferredSize());
	      
        // показываем форму
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(() -> {
                    renderer.close();
                    timer.stop();
                    System.exit(0);
                }).start();
            }
        });
        // тинициализация таймера, срабатывающего раз в 100 мсек
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTime();
            }
        });
        timer.start();
        initWidgets();
    }

	  public Dimension getPreferredSize() {
          return new Dimension(widthFrame, heightFrame);
      }
	
	  /**
	     * Инициализация виджетов
	     */
	    private void initWidgets() {

	        addCircle.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	   if(x0PointField.getText().isEmpty() || y0PointField.getText().isEmpty() || radiusField.getText().isEmpty()) {
		                	JOptionPane.showMessageDialog(null, " Не все поля заполнены.");
		                }
	            	   else {
	            		   double x0 = Double.parseDouble(x0PointField.getText());
	            		   double y0 = Double.parseDouble(y0PointField.getText());
	            		   double radius = Double.parseDouble(radiusField.getText());
	             
	               
	            		   if(x0+radius>1 || x0-radius<-1 || y0+radius>1 || y0-radius<-1 )
	            			   JOptionPane.showMessageDialog(null, " Окружность выходит за границу фрейма.");
	            		   else
	            			   renderer.problem.addCircle(x0, y0, radius);
	            	   	}
	            }
	        });
	        
	        addWideBeam.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(apex1X.getText().isEmpty() || apex1Y.getText().isEmpty() || apex2Y.getText().isEmpty()) {
	                	JOptionPane.showMessageDialog(null, " Не все поля заполнены.");
	                }
	            	else {
	            		double x1 = Double.parseDouble(apex1X.getText());
	            		double y1 = Double.parseDouble(apex1Y.getText());
	            		double y2 = Double.parseDouble(apex2Y.getText());
	            		
	            		if(x1>1 || x1<-1 || y1>1 || y1<-1 || y2>1 || y2<-1)
	            			JOptionPane.showMessageDialog(null, "Вертикальная сторона 'широкого луча' выходит за границу фрейма.");
	            		else
	            			renderer.problem.addWideBeam(x1, y1, y2);
	            	}
	            }
	        });
	 
	        randomCircleBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(circleCntField.getText().isEmpty()) {
	                	JOptionPane.showMessageDialog(null, " Введите количество элементов.");
	                }
	            	else
	            		renderer.problem.addRandomCircle(Integer.parseInt(circleCntField.getText()));
	            }
	        });
	        randomWideBeamBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	             renderer.problem.addRandomWideBeam(Integer.parseInt(circleCntField.getText()));
	            }
	        });
	        loadFromFileBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                renderer.problem.loadFromFile();
	            }
	        });
	        saveToFileBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                renderer.problem.saveToFile();
	            }
	        });
	        clearBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                renderer.problem.clear();
	            }
	        });
	        solveBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                renderer.problem.solve();
	            }
	        });
	    }
	    
	    /**
	     * Событие таймера
	     */
	    private void onTime() {
	        // события по таймеру
	    }
	
	public static void main(String[] args) {
		new Form();
	}

}
