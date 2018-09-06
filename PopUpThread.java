package usmt;

import javax.swing.JOptionPane;

public class PopUpThread extends Thread {
	
	String text;
	boolean success;
	
	public PopUpThread(String notification, boolean isSuccess) {
		text = notification;
		success = isSuccess;
	}
	
	public void run(){
		if (success)
	       JOptionPane.showMessageDialog (null, text, 
       			"Notification", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog (null, text, 
	    			"ERROR", JOptionPane.ERROR_MESSAGE);
	    JOptionPane.getRootFrame().dispose();  
		this.interrupt();
	}

}


