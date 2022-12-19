

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.*;

public class time implements Runnable {
	
	
	public time() {
		
	}

	@Override
	public void run() {
		int delay = 1000; //milliseconds
		System.out.println("Time Thread Start");
		  ActionListener taskPerformer = new ActionListener() {
			  String timeStamp;
		      public void actionPerformed(ActionEvent evt) {
		    	 timeStamp = new SimpleDateFormat("hh:mm:ss aa").format(new java.util.Date());
		    	 pj3.timeText.setText(timeStamp);
		    	 
		      }
		  };
		  new Timer(delay, taskPerformer).start();
		  
		
	}
	
	
	
}