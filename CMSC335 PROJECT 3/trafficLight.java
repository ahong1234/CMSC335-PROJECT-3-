/*
 *  Name: Alexander Hong
 *  Instructor: Prof. Mujeye
 *  Due Date: 10-11-22
 *  Description: Class that describes a traffic light object. 
 *  8 seconds green. 3 seconds yellow. 7 seconds red.
 */




import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;

public class trafficLight implements Runnable {
	
	String[] colors = {"GREEN", "YELLOW", "RED"};
	Thread tl;
	String threadName;
	private int i = 0;
    private String currentLight = colors[i];
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    public final AtomicBoolean suspended = new AtomicBoolean(false);
    private JLabel current;
	
    // constructor
    public trafficLight(String name, JLabel j) {
		this.threadName = name;
		this.current = j;
		pj3.status.setText("Status: created " + threadName);
	}
	
    // start thread
    public void start() {
        System.out.println("Starting " + threadName);
        if(tl == null) {
            tl = new Thread(this, threadName);
            tl.start();
        }
    }
    
    public synchronized String getColor() {
        this.currentLight = colors[i];
        return this.currentLight;
    }
    public void suspend() {
        suspended.set(true);
        System.out.println("Suspending " + threadName);
    }
    
    public synchronized void resume() {
        suspended.set(false);
        notify();
        System.out.println("Resuming " + threadName);
    }
    
    
    public void stop() {
        tl.interrupt();
        isRunning.set(false);
        System.out.println("Stopping " + threadName);
    }
    
    // interrupt sleeping thread
    public void interrupt() {
        tl.interrupt();
    }
    
	@Override
	public void run() {	
		isRunning.set(true);
		while (isRunning.get()) { 
			try {
				synchronized(this) {
                    while(suspended.get()) {
                        System.out.println(threadName + " waiting");
                        wait();
                    }
                }
					switch(getColor()) {
					case "GREEN":
						//System.out.println(getColor());
						current.setText(getColor());
						// green for 8 seconds
						Thread.sleep(8000);
						i++;
						break;
					case "YELLOW":
						//System.out.println(getColor());
						current.setText(getColor());
						// yellow for 3 seconds
						Thread.sleep(3000);
						i++;
						break;
					case "RED":
						//System.out.println(getColor());
						current.setText(getColor());
						// red for 7 seconds
						Thread.sleep(7000);
						//i++;
						i = 0;
						break;
					default:
						
						break;
					}
					
			} catch (InterruptedException e) {
				suspended.set(true);
			}
		}
	}

}
