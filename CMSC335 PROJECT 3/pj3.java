/*
 *  Name: Alexander Hong
 *  Instructor: Prof. Mujeye
 *  Due Date: 10-11-22
 *  Description: Java Swing GUI to hold the time, traffic 
 *  lights, and cars. Display light color in real time as well
 *  as car position and speed. 
 */



import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

public class pj3 implements Runnable {
	static JFrame mainFrame;
	static JLabel timeText;
	static JPanel buttons;
	static JButton addCar;
	static JButton start;
	static JButton pause;
	static JButton resume;
	static JButton stop;
	static JPanel carData;
	static JLabel car1Data;
	static JLabel car2Data;
	static JLabel car3Data;
	static JLabel traffic1;
	static JLabel traffic2;
	static JLabel traffic3;
	static JLabel light1;
	static JButton addLight;
    private static boolean isRunning;
	private static final AtomicBoolean simIsRunning = new AtomicBoolean(false);
	static JLabel[] labels = new JLabel[3];
	private final int maxCars = 3;
	static JLabel status;
	static int j = 0;
	static int k = 0;
	static Thread gui;
	car c1;
	car c2;
	car c3;
	static ArrayList<car> cars = new ArrayList<car>();
	static ArrayList<trafficLight> lightList = new ArrayList<trafficLight>();
	static JPanel lights;
	JLabel trafficLightA;
    JLabel trafficLightB;
    JLabel trafficLightC;
    JLabel trafficLightAcolor;
    JLabel trafficLightBcolor;
    JLabel trafficLightCcolor;
    trafficLight a;
    trafficLight b;
    trafficLight c;
    static int numLights = 0;
	public pj3() {
		trafficLightA = new JLabel("Intersection A at position 175: ");
        trafficLightB = new JLabel("Intersection B at position 325: ");
        trafficLightC = new JLabel("Intersection C at position 475: ");
        trafficLightAcolor = new JLabel();
        trafficLightBcolor = new JLabel();
        trafficLightCcolor = new JLabel();
		addLight = new JButton("Add Light");
		lights = new JPanel();
		lights.setLayout(new GridLayout(3, 1));
		mainFrame = new JFrame("Traffic Simulator 2022");
		timeText = new JLabel();
		status = new JLabel("Status: ");
		addCar = new JButton("add car");
		start = new JButton("start");
		pause = new JButton("pause");
		resume = new JButton("resume");
		stop = new JButton("stop");	
		carData = new JPanel();
		carData.setLayout(new GridLayout(3, 1));
		buttons = new JPanel();
		mainFrame.setLayout(new GridLayout(4, 1));
		mainFrame.add(timeText);
		carData.add(status);
		buttons.add(addCar);
		buttons.add(start);
		buttons.add(pause);
		buttons.add(resume);
		buttons.add(stop);
		buttons.add(addLight);
		start.setEnabled(false);
		pause.setEnabled(false);
		resume.setEnabled(false);
		stop.setEnabled(false);
		buttons.setLayout(new GridLayout(2, 3));
		mainFrame.add(buttons);	
		carData.setLayout(new GridLayout(4, 1));
		mainFrame.add(carData);
		mainFrame.add(lights);
		initButtons();
		isRunning = Thread.currentThread().isAlive();
		
	}
	
	public void show() {
		mainFrame.setSize(500, 600);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		Thread time =  new Thread(new time());
		time.start();
		pj3 p3 = new pj3();
		p3.show();
		gui = new Thread(p3);

	}

	public void initButtons() {
		
		// you cannot press start until at least one car has been added
		addCar.addActionListener((ActionEvent e) -> {
			if (!simIsRunning.get()) {
			if (j == 0) {
				start.setEnabled(true);
				pause.setEnabled(true);
				resume.setEnabled(true);
				car1Data = new JLabel("car 1 X-value: 50 Speed: 0km/hour");
				c1 = new car("car 1", 50);
				labels[0] = car1Data;
				cars.add(c1);
				carData.add(car1Data);
				j++;
			}
			else if (j == 1) {
				
				car2Data = new JLabel("car 2 X-value: 250 Speed: 0km/hour");
				labels[1] = car1Data;
				c2 = new car("car 2", 250);
				cars.add(c2);
				carData.add(car2Data);
				j++;
			}
			else if (j == 2) {
				
				car3Data = new JLabel("car 3 X-value: 400 Speed: 0km/hour");
				labels[2] = car1Data;
				c3 = new car("car 3", 400);
				cars.add(c3);
				carData.add(car3Data);
				j++;
			}
			
			else {
				status.setText("Status: max cars added");
			}
			
			}
			
		});
		
		// add up to three traffic lights
		addLight.addActionListener((ActionEvent e)  -> {
			if (!simIsRunning.get()) {
				if (k == 0) { 
				    a = new trafficLight("intersection 1", trafficLightAcolor);
					lights.add(trafficLightA);
					lights.add(trafficLightAcolor);
					lightList.add(a);
					k++;
					numLights++;
				}
				else if (k == 1) {
					b = new trafficLight("intersection 2", trafficLightBcolor);
					lights.add(trafficLightB);
					lights.add(trafficLightBcolor);
					lightList.add(b);
					k++;
					numLights++;
				}
				else if (k == 2) {
					c = new trafficLight("intersection 3", trafficLightCcolor);
					lights.add(trafficLightC);
					lights.add(trafficLightCcolor);
					lightList.add(c);
					k++;
					numLights++;
				}
				
				else {
					status.setText("Status: max lights added");
				}
				}
		});
		
		// once the simulation starts you cannot add any more lights or cars
		start.addActionListener((ActionEvent e) -> {
			stop.setEnabled(true);
			addLight.setEnabled(false);
			addCar.setEnabled(false);
			if (!simIsRunning.get()) {
				for (car i : cars) {
					i.start();
				}
				for (trafficLight l : lightList) {
					l.start();
				}
				gui.start();
				status.setText("Status: sim running");
				simIsRunning.set(true);
			}
			start.setEnabled(false);
		});
		
		// suspend all threads
		pause.addActionListener((ActionEvent e) -> {
			stop.setEnabled(false);
			
			if (simIsRunning.get()) {
				for (car i : cars) {
					i.pause();
				}
				for(trafficLight t: lightList) {
                   
                    t.interrupt();
                    t.suspend();
                }
				status.setText("Status: sim paused");
				simIsRunning.set(false);
			}
			
		});
		
		// resume all threads
		resume.addActionListener((ActionEvent e) -> {
				stop.setEnabled(true);
				if (simIsRunning.get() == false) {
				for (car i : cars) {
					i.resume();
				}
				for(trafficLight t: lightList) {
                   //t.interrupt();
                    t.resume();
                }
				status.setText("Status: sim resumed");
				simIsRunning.set(true);
				}
			
		});
		
		// end the simulation and disable all buttons
		stop.addActionListener((ActionEvent e) -> {
			if (simIsRunning.get()) {
				for (car i : cars) {
					
						i.stop();
					
				}
				for(trafficLight t: lightList) {
                    
                    t.stop();
                }
				start.setEnabled(false);
				pause.setEnabled(false);
				resume.setEnabled(false);
				status.setText("sim ended");
			}
		});
	}
	
	 private void getData() {
	        if(simIsRunning.get()) {
	        switch(a.getColor()) {
	            case "RED":
	                for(car i: cars) {
	                   // if car is 50 meters away from position 175 then stop
	                    if(i.showPos()>125 && i.showPos()<175) {
	                        i.atLight.set(true);
	                    }
	                }
	                break;
	            case "GREEN":
	                for(car i:cars) {
	                    if(i.atLight.get()) {
	                    	status.setText("Status: " + i.getName() + " resuming at green light");
	                        i.resume();
	                    }
	                }
	                break;
	        }
	        if (numLights == 2) {
	        switch(b.getColor()) {
            case "RED":
                for(car i: cars) {
                    // if car is 50 meters away from position 325 then stop
                    if(i.showPos()>275 && i.showPos()<325) {
                        i.atLight.set(true);
                    }
                }
                break;
	        
	        
            case "GREEN":
                for(car i:cars) {
                    if(i.atLight.get()) {
                    	status.setText("Status: " + i.getName() + " resuming at green light");
                        i.resume();
                    }
                }
                break;
        }}
	       
	       if (numLights == 3)
	        switch(c.getColor()) {
            case "RED":
                for(car i: cars) {
                    // if car is 50 meters away from position 475 then stop
                    if(i.showPos()>425 && i.showPos()<475) {
                        i.atLight.set(true);
                    }
                }
                break;
            case "GREEN":
                for(car i:cars) {
                    if(i.atLight.get()) {
                    	status.setText("Status: " + i.getName() + " resuming at green light");
                        i.resume();
                    }
                }
                break;
        }
	       
	        }}
	
	@Override
	public void run() {
		while (isRunning) {
		if (simIsRunning.get()) {
			if (cars.size() == 1) {
			car1Data.setText("Car 1 X-value: " + c1.showPos() + " Speed: " + c1.getSpeed() + " km/h");
			}
			else if (cars.size() == 2) {
				car1Data.setText("Car 1 X-value: " + c1.showPos() + " Speed: " + c1.getSpeed() + " km/h");
				car2Data.setText("Car 2 X-value: " + c2.showPos() + " Speed: " + c2.getSpeed() + " km/h");
			}
			else {
				car1Data.setText("Car 1 X-value: " + c1.showPos() + " Speed: " + c1.getSpeed() + " km/h");	
				car2Data.setText("Car 2 X-value: " + c2.showPos() + " Speed: " + c2.getSpeed() + " km/h");
				car3Data.setText("Car 3 X-value: " + c3.showPos() + " Speed: " + c3.getSpeed() + " km/h");
			}
			getData();
		}
			
		}
		
	}

}
