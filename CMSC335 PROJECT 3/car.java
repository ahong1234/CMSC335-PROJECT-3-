/*
 *  Name: Alexander Hong
 *  Instructor: Prof. Mujeye
 *  Due Date: 10-11-22
 *  Description: Class that describes a car object.
 *  It can pause and restart. Returns speed in km/h
 */



import java.util.concurrent.atomic.AtomicBoolean;

public class car implements Runnable {
	Thread thread;
	String threadName;
	int xPos;
	int speed;
	private static final AtomicBoolean simIsRunning = new AtomicBoolean(false);
	public final AtomicBoolean suspended = new AtomicBoolean(false);
	public final AtomicBoolean atLight = new AtomicBoolean(false);
	
	// constructor with name and starting position
	public car(String name, int xPos) {
		this.threadName = name;
		this.xPos = xPos;
		pj3.status.setText("Status: created " + threadName);
	}
	
	public String getName() {
		return this.threadName;
	}
	
	// return the car's current position
	public synchronized int showPos() {
		return xPos;
	}
	
	// start travelling along the x axis
	public void start() {
		if(thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
            simIsRunning.set(true);
        }
	}
	
	public synchronized void pause() {
		suspended.set(true);;
	}
	
	public synchronized void resume() {
		if (suspended.get() || atLight.get()) {
		atLight.set(false);
		suspended.set(false);
		notify();
}
	}

	public void stop() {
		
		thread.interrupt();
		simIsRunning.set(false);
		speed = 0;
        System.out.println("Stopping " + threadName);
    }
	
	public int getSpeed() {
		return this.speed;
	}
	
	@Override
	public void run() {
		if (simIsRunning.get()) {
			try {
				while (simIsRunning.get()) {
				while (xPos < 500) {
					synchronized(this) {
					while (suspended.get() || atLight.get()) {
						if (atLight.get()) {
						pj3.status.setText(threadName + " stopped at red light");
						}
						speed = 0;
						wait();
					}}
					if (simIsRunning.get()) {
						// 50 meters a second = 180 kilometers per hour
						
						speed = 180;
						xPos += 5;
					//System.out.println(xPos);
					Thread.sleep(100);
					}
				}
				xPos = 0;
				}
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
