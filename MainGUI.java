//Brian A. Furman 7/28/2016

package usmt;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel scanstatePanel, loadstatePanel;
	public static boolean inProgress;
	public static String usbDrive = "E:";
	public static ArrayList<String> userList;
	private String[] exceptionList = {"All Users", "Default", "Default User", "Public", "desktop.ini", "altinstall.admdom"};
	
	public MainGUI() {	//CHANGE BACK INSIDE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("User State Migration Tool (USMT)");
		setResizable(false);
		
		setIconImage(new ImageIcon(getClass().getResource("resources/Harper.png")).getImage());
		
		try {	//gets "E:" part of jar running path
			usbDrive = MainGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString().substring(1, 3);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			usbDrive = "E:";
			JOptionPane.showMessageDialog (null, "Could not get usb drive letter.\nDefaulting to " + usbDrive, "Error!", JOptionPane.ERROR_MESSAGE);
			}
		
		usbDrive = "F:";	//TEMPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP, REMOVE WHEN SAVED TO USB !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		getUsers();
		
		add(tabbedPanel());
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		if (isAdmin()) {
			JOptionPane.showMessageDialog (null, "You are using this program as an administrator.\nYou can use ScanState and LoadState.", 
					"Is Admin", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog (null, "You are NOT using this program as an administrator.\nYou will have errors!\n"
					+ "Please try running the USMT.bat as a different user.", "Is Not Admin", JOptionPane.ERROR_MESSAGE);
			//System.exit(0);
		}
	}
	
	public JPanel tabbedPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(700, 384));
		
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(700, 384));
		
		scanstatePanel = new JPanel();
		try {
			scanstatePanel.add(new ScanStateGUI());
		} catch (IOException e) {
			e.printStackTrace();
			}
	    tabbedPane.addTab("ScanState", null, scanstatePanel,
	                null);
	         
	    loadstatePanel = new JPanel();
	    try {
			loadstatePanel.add(new LoadStateGUI());
		} catch (IOException e) {
			e.printStackTrace();
			}
	    tabbedPane.addTab("LoadState", null, loadstatePanel,
	                null);
	    
	    tabbedPane.addChangeListener(new ChangeListener() {

	        public void stateChanged(ChangeEvent e) {
	        	
	            if (tabbedPane.getSelectedIndex() == 0) {
	                ScanStateGUI.updateButtonNames();
	            }
	            if (tabbedPane.getSelectedIndex() == 1) {
	                LoadStateGUI.updateButtonNames();
	                LoadStateGUI.selectedUsers.clear();
	                LoadStateGUI.selectedUserIndexes.clear();
	            }
	        }
	    });
	    mainPanel.add(tabbedPane);
	         
	    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    
	    return mainPanel;
	}

    public static boolean isAdmin() {	//ERROR BUG (still works)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String groups[] = (new com.sun.security.auth.module.NTSystem()).getGroupIDs();
        for (String group : groups) {
            if (group.equals("S-1-5-32-544"))
                return true;
        }
        return false;
    }
    
	public void getUsers() { //finds all folders and removes exceptions
		
		File file = new File("C:\\Users");
		String[] names = file.list();
		userList = new ArrayList<String>();

		for(String name : names) {
			if (!userIsAnException(name))
				userList.add(name);
		}
	}
	
	private boolean userIsAnException(String userNameToCheck) {
		for (int i = 0; i < exceptionList.length; i++)
			if (userNameToCheck.equals(exceptionList[i]))
				return true;
		return false;
	}
       
    public static void main(String args[]) {
    	new MainGUI();
    }
}



