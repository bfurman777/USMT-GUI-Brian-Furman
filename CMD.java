package usmt;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class CMD extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel cmdPanel;
	private JTextArea cmdText;
	
	public CMD(String userName, String operation) {	//String userName?
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("CMD Window - User: " + userName + " - " + operation + " USMT");
		setResizable(false);
		setLayout(new GridLayout(1, 1, 0, 0));
		
		initCMDScreen();
		pack();
		
		setVisible(true);
	}
	
	private void initCMDScreen() {
		cmdPanel = new JPanel();
		add(cmdPanel);

		cmdText = new JTextArea(40, 70);
		cmdText.setEditable(false);
		   
		cmdPanel.add(cmdText);

		pack();
		setVisible(true);
	  
	    JScrollPane scrollPanel = new JScrollPane(cmdText);
	    cmdPanel.add(scrollPanel, BorderLayout.CENTER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        
        DefaultCaret caret = (DefaultCaret)cmdText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

	}
	
	public void write (String line) {
		cmdText.append(line + "\n");
	}
	

}


