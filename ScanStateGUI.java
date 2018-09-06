package usmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

public class ScanStateGUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private FileOutputStream fos;
	private DataOutputStream dos;
	private File file;
	private static ArrayList <String> userList;
	private ArrayList <String> selectedUsers;
	private ArrayList <String> selectedUserIndexes;
	private static ArrayList <String> failedUsers;
	private static ArrayList <String> failCodes;
	public static ArrayList <String> succeededUsers, succeededAllUsers;
	private String userName = "", daysString = "";
	private String errorCode = "ScanState return code: null";
	private JPanel firstScreen, selectionScreen1, selectionScreen2, allScreenForIceScreen;
	private final int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 350;
	private JButton allButton, oneButton, secret, searchButton, backButton, selectsButton, selectAllButton, saveAllButton, saveAllByDateButton, saveAllByDaysSinceLogOnButton, backAllButton, deleteButton;
	private TextField input;
	private JLabel introLabel, topLabel, topAllLabel;
	private Font font1, font2, font3, font4, font5;
	private static JButton[] buttons;
	private Color defaultButtonColor;
	public SavedLog saveLog;
	private boolean overriding = false, severalSelects = false;
	private Border emptyBorder;
	
	private Action action = new AbstractAction("New Background Thread") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			  runBatchFileAndCheckForError(false);
		  }
		};
	
	public ScanStateGUI() throws IOException {
		selectedUsers = new ArrayList<String>();
		selectedUserIndexes = new ArrayList<String>();
		failedUsers = new ArrayList<String>();
		failCodes = new ArrayList<String>();
		succeededUsers = new ArrayList<String>();
		succeededAllUsers = new ArrayList<String>();
		saveLog = new SavedLog();
		emptyBorder = BorderFactory.createEmptyBorder();
		
		userList = MainGUI.userList;
		initfirstScreen();
		initSelectionScreen1();
		initSelectionScreen2();
		initAllUsersScreen();
		
		setLayout(new GridLayout(1, 1, 0, 0));
		add(firstScreen);
		
		setVisible(true);
	}
	
	public void initfirstScreen() {
		setLayout(new GridLayout(1, 1, 0, 0));
		firstScreen = new JPanel();
		add(firstScreen);
		
		firstScreen.setFocusable(true);
		firstScreen.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		firstScreen.setLayout(null);
		
		firstScreen.setBackground(Color.WHITE);
		
		font5 = new Font("Dialog", Font.BOLD, 13);
		allButton = new JButton();
		allButton.setFont(font5);
		defaultButtonColor = allButton.getBackground();
		allButton.setText("Copy All Users   (or by date)");
		firstScreen.add(allButton);
		allButton.setBounds(50, 90, 250, 200);	//(x, y, width, height)
		allButton.addActionListener(this);
		allButton.setIcon(new ImageIcon(getClass().getResource("resources/pill-big.png")));
		allButton.setHorizontalTextPosition(JButton.CENTER);
		allButton.setContentAreaFilled(false);
		allButton.setBorder(emptyBorder);
		allButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-pill-big.png")));
		
		oneButton = new JButton();
		oneButton.setFont(font5);
		oneButton.setText("Copy Select Users");
		firstScreen.add(oneButton);
		oneButton.setBounds(400, 90, 250, 200);
		oneButton.addActionListener(this);	
		oneButton.setIcon(new ImageIcon(getClass().getResource("resources/pill-big.png")));
		oneButton.setHorizontalTextPosition(JButton.CENTER);
		oneButton.setContentAreaFilled(false);
		oneButton.setBorder(emptyBorder);	
		oneButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-pill-big.png")));
		
		font2 = new Font("SansSerif", Font.ITALIC, 25);
		introLabel = new JLabel();
		introLabel.setText("How do you want to copy your user(s)?");
		introLabel.setFont(font2);
		firstScreen.add(introLabel);
		introLabel.setBounds(135, 20, 500, 35);
		
		deleteButton = new JButton();
		deleteButton.setText("Delete existing usb saves");
		deleteButton.setForeground(Color.WHITE);
		firstScreen.add(deleteButton);
		deleteButton.setBounds(262, 300, 175, 41);
		deleteButton.addActionListener(this);
		deleteButton.setIcon(new ImageIcon(getClass().getResource("resources/pill-red.png")));
		deleteButton.setHorizontalTextPosition(JButton.CENTER);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setBorder(emptyBorder);	
		deleteButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-pill-red.png")));
		
		remove(firstScreen);
	}
	
	public void initSelectionScreen1() {	//top part of page 2
		setLayout(new GridLayout(2, 1, 0, 0));
		
		selectionScreen1 = new JPanel();
		add(selectionScreen1);
		selectionScreen1.setFocusable(true);
		selectionScreen1.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT/2));
		selectionScreen1.setLayout(null);
		
		selectionScreen1.setBackground(Color.WHITE);
		
		font1 = new Font("SansSerif", Font.PLAIN, 25);
	    input = new TextField(1);
	    input.setBounds(145, 55, 300, 35);
	    input.setFont(font1);
	    selectionScreen1.add(input);
	    input.addActionListener(this);
	    
	    searchButton = new JButton();
	    searchButton.setText("Search");
		selectionScreen1.add(searchButton);
		searchButton.setBounds(475, 55, 100, 35);	//(x, y, width, height)
		searchButton.addActionListener(this);

		font2 = new Font("SansSerif", Font.ITALIC, 22);
	    topLabel = new JLabel();
	    topLabel.setText("Enter a Username");
	    topLabel.setFont(font2);
	    selectionScreen1.add(topLabel);
	    topLabel.setBounds(230, 15, 250, 32);
	    
		secret = new JButton();
		selectionScreen1.add(secret);
		secret.setBounds(0, 0, 3, 1);
		secret.addActionListener(this);
	    
	    backButton = new JButton();
	    selectionScreen1.add(backButton);
	    backButton.setBounds(10, 10, 63, 63);
	    backButton.addActionListener(this);
	    backButton.setIcon(new ImageIcon(getClass().getResource("resources/back.png")));
	    backButton.setContentAreaFilled(false);
	    backButton.setBorder(emptyBorder);
	    backButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-back.png")));
	    
	    font3 = new Font("SansSerif", Font.ITALIC, 20);
	    selectsButton = new JButton();
	    selectsButton.setFont(font3);
	    selectsButton.setText("Copy Selected Users Below");
		selectionScreen1.add(selectsButton);
		selectsButton.setBounds(190, 120, 300, 50);
		selectsButton.addActionListener(this);
		
		selectAllButton = new JButton();
		selectAllButton.setText("Select All");
		selectionScreen1.add(selectAllButton);
		selectAllButton.setBounds(50, 131, 100, 25);
		selectAllButton.addActionListener(this);
		
		remove(selectionScreen1);
	}
	
	private void initSelectionScreen2() {	//bottom part of page 2
		selectionScreen2 = new JPanel(new BorderLayout());
		add(selectionScreen2);
		selectionScreen2.setFocusable(true);
		selectionScreen2.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT/2));
		
		int userLength = userList.size();
	
	    JPanel innerPanel = new JPanel(new GridLayout(userLength, 1, 0, 0));
	    
	    innerPanel.setBackground(Color.WHITE);
	    
	    buttons = new JButton[userLength];
	    selectedUsers.clear();
	    selectedUserIndexes.clear();
	   
	    for(int i = 0; i < userLength; i++) {
	    	
	    	String name = userList.get(i);
	    	
	    	if (succeededUsers.contains(userList.get(i)))	//new File(MainGUI.usbDrive + "\\USMT\\" + name + "\\statestore\\USMT\\USMT.MIG").exists()
	    		name = userList.get(i) + "           (saved)";
	    	
            buttons[i] = new JButton(name);
            innerPanel.add(buttons[i]);
            final int buttonIndex = i;
            
            buttons[i].addActionListener(new ActionListener() {
            	
               public void actionPerformed(ActionEvent ae2) {
           			if (!selectedUserIndexes.contains(buttonIndex + "")) {
           				buttons[buttonIndex].setBackground(Color.GREEN);
           				selectedUsers.add(userList.get(buttonIndex));
           				selectedUserIndexes.add(buttonIndex + "");
           			}
           			else {
           				buttons[buttonIndex].setBackground(defaultButtonColor);
           				selectedUsers.remove(userList.get(buttonIndex));
           				selectedUserIndexes.remove(buttonIndex + "");
           			}
               }  
            });
        }
	    
	    JScrollPane scrollPanel = new JScrollPane(innerPanel);
        selectionScreen2.add(scrollPanel, BorderLayout.CENTER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        
        remove(selectionScreen2);
	}
	
	public void initAllUsersScreen() {
		setLayout(new GridLayout(1, 1, 0, 0));
		allScreenForIceScreen = new JPanel();
		add(allScreenForIceScreen);
		
		allScreenForIceScreen.setFocusable(true);
		allScreenForIceScreen.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		allScreenForIceScreen.setLayout(null);
		allScreenForIceScreen.setBackground(Color.WHITE);
		
		font4 = new Font("SansSerif", Font.ITALIC, 26);
	    topAllLabel = new JLabel();
	    topAllLabel.setText("Copy All Users Logged On:");
	    topAllLabel.setFont(font4);
	    allScreenForIceScreen.add(topAllLabel);
	    topAllLabel.setBounds(195, 45, 325, 32);
		
		saveAllButton = new JButton();
		saveAllButton.setFont(font5);
		saveAllButton.setText("Copy All Users");
		allScreenForIceScreen.add(saveAllButton);
		saveAllButton.setBounds(225, 220, 250, 115);	//(x, y, width, height)
		saveAllButton.addActionListener(this);
		saveAllButton.setIcon(new ImageIcon(getClass().getResource("resources/pill-small.png")));
		saveAllButton.setHorizontalTextPosition(JButton.CENTER);
		saveAllButton.setContentAreaFilled(false);
		saveAllButton.setBorder(emptyBorder);
		saveAllButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-pill-small.png")));
		
		saveAllByDateButton = new JButton();
		saveAllByDateButton.setFont(font5);
		saveAllByDateButton.setText("Since A Specific Date");
		allScreenForIceScreen.add(saveAllByDateButton);
		saveAllByDateButton.setBounds(400, 100, 250, 115);	//(x, y, width, height)
		saveAllByDateButton.addActionListener(this);
		saveAllByDateButton.setIcon(new ImageIcon(getClass().getResource("resources/pill-small.png")));
		saveAllByDateButton.setHorizontalTextPosition(JButton.CENTER);
		saveAllByDateButton.setContentAreaFilled(false);
		saveAllByDateButton.setBorder(emptyBorder);
		saveAllByDateButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-pill-small.png")));
		
		saveAllByDaysSinceLogOnButton = new JButton();
		saveAllByDaysSinceLogOnButton.setFont(font5);
		saveAllByDaysSinceLogOnButton.setText("Since A Number Of Days");
		allScreenForIceScreen.add(saveAllByDaysSinceLogOnButton);
		saveAllByDaysSinceLogOnButton.setBounds(50, 100, 250, 115);	//(x, y, width, height)
		saveAllByDaysSinceLogOnButton.addActionListener(this);
		saveAllByDaysSinceLogOnButton.setIcon(new ImageIcon(getClass().getResource("resources/pill-small.png")));
		saveAllByDaysSinceLogOnButton.setHorizontalTextPosition(JButton.CENTER);
		saveAllByDaysSinceLogOnButton.setContentAreaFilled(false);
		saveAllByDaysSinceLogOnButton.setBorder(emptyBorder);
		saveAllByDaysSinceLogOnButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-pill-small.png")));
		
		backAllButton = new JButton();
		allScreenForIceScreen.add(backAllButton);
	    backAllButton.setBounds(10, 10, 63, 63);
	    backAllButton.addActionListener(this);
	    backAllButton.setIcon(new ImageIcon(getClass().getResource("resources/back.png")));
	    backAllButton.setContentAreaFilled(false);
	    backAllButton.setBorder(emptyBorder);
	    backAllButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-back.png")));
		
		remove(allScreenForIceScreen);
	}
	
	public boolean userIsFound() {
		
		for (int i = 0; i < userList.size(); i++)
			if (userName.equalsIgnoreCase(userList.get(i)))
				return true;
		return false;
	}

	public static void updateButtonNames() {
		for (int i = 0; i < userList.size(); i++) {
			
			if (succeededUsers.contains(userList.get(i)))	
	    		buttons[i].setText(userList.get(i) + "           (saved)");
			
			else
				buttons[i].setText(userList.get(i));
			
			if (failedUsers.contains(userList.get(i)))
				buttons[i].setText(userList.get(i) + "        FAILED: " + failCodes.get(failedUsers.indexOf(userList.get(i))) + "");
		}
	}
	
	public static void deleteFolder (String folderName) {
    	File file = new File(MainGUI.usbDrive + "\\USMT\\" + folderName + "\\statestore\\USMT\\USMT.MIG");
    	if(!file.delete())
    		JOptionPane.showMessageDialog (null, "Could not delete item at:\n" + file,
					  "Error!", JOptionPane.ERROR_MESSAGE);
    	
    	file = new File(MainGUI.usbDrive + "\\USMT\\" + folderName + "\\statestore\\USMT");
    	if(!file.delete())
    		JOptionPane.showMessageDialog (null, "Could not delete item at:\n" + file,
					  "Error!", JOptionPane.ERROR_MESSAGE);
    	
    	file = new File(MainGUI.usbDrive + "\\USMT\\" + folderName + "\\statestore");
    	if(!file.delete())
    		JOptionPane.showMessageDialog (null, "Could not delete item at:\n" + file,
					  "Error!", JOptionPane.ERROR_MESSAGE);
    	
    	file = new File(MainGUI.usbDrive + "\\USMT\\" + folderName);
    	if(!file.delete())
    		JOptionPane.showMessageDialog (null, "Could not delete item at:\n" + file,
					  "Error!", JOptionPane.ERROR_MESSAGE);
    	
    	String user = folderName.replace("_", " ");
    	
    	if (!succeededAllUsers.contains(user) || !succeededUsers.contains(user)) {
    		if (folderName.contains("All_Users"))
    			succeededAllUsers.remove(succeededAllUsers.indexOf(user));
    		else
    			succeededUsers.remove(succeededUsers.indexOf(user));
    	}
	}
	
	public void runBatchFileAndCheckForError(final boolean isPlural) {
		
			  new SwingWorker<Void, Void>() {
			    {
			      // Disable action until task is complete to prevent concurrent tasks.
			      action.setEnabled(false);
			    }

			    // Called on the Swing thread when background task completes.
			    protected void done() {
			      action.setEnabled(true);
			      MainGUI.inProgress = false;

			      try {
			        // No result but calling get() will propagate any exceptions onto Swing thread.
			        get();
			      } catch(Exception ex) {
			        ex.printStackTrace();
			      	}
			      
			      if (!overriding) {
			    	  if (selectedUsers.size() > 0) {
			    		  selectedUsers.remove(0);
			    		  if (severalSelects == true && selectedUsers.size() == 0) {
				    		  severalSelects = false;
				    		  JOptionPane.showMessageDialog (null, "All selected users have been copied.",
			    					  "Done", JOptionPane.INFORMATION_MESSAGE);
				    		  JOptionPane.getRootFrame().dispose();
				    		  //clear button highlighting/selecting
			       	        	for (String i : selectedUserIndexes) 
			       	        		buttons[Integer.parseInt(i)].setBackground(defaultButtonColor);
			       	        	selectedUserIndexes.clear();
			    		  }
			    	  }
			    	  if (selectedUsers.size() > 0) {
			    		  severalSelects = true;
			    		  userName = selectedUsers.get(0);
			    		  try {
			    			  batFileOne();
			    		  } catch (IOException e) {
			    			  e.printStackTrace();
			    			  JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
			    					  "Error!", JOptionPane.ERROR_MESSAGE);
			    		  }
			    	  }
			      }
			      if (overriding)
			    	  runBatchFileAndCheckForError(isPlural);
			      overriding = false;
			    }

			    // Called on background thread
			    protected Void doInBackground() throws Exception {
			    	
			    	if (!new File(MainGUI.usbDrive + "\\USMT\\x86\\scanstate.exe").exists()) {	//if scanstate.exe is missing
			    		JOptionPane.showMessageDialog (null, "scanstate.exe could not be found at:\n"
			    				+ MainGUI.usbDrive + "\\USMT\\x86\\scanstate.exe\nPlease check the file path.", 
			        			"Missing scanstate.exe", JOptionPane.ERROR_MESSAGE);
			    		return null;
			    	}
			    	
			    	if (MainGUI.inProgress) {	//prevent multitasking
			    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
			        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
			    		return null;
			    	}
			    	
			    	MainGUI.inProgress = true;
			    	
			    	if (!userName.contains("All Users")) 
			    		buttons[userList.indexOf(userName)].setText(userName + "           (in progress...)");
			    	
					CMD cmd = new CMD(userName, "ScanState");
			    	
			    	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "temp.bat").redirectErrorStream(true);
			        builder.directory(new File(MainGUI.usbDrive + "\\USMT\\Resources"));
			        Process process = builder.start();
			       
			        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			        String line;
			        while ((line = input.readLine()) != null) {
			        	
			            if (line.contains("ScanState return code:"))
			            	errorCode = line;
			            
			            cmd.write(line);
			        }
			        input.close();
			        try {
						process.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog (null, "There was an error with reading the cmd.",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
						} 
			        
			        boolean isSuccess = false;
			        
			        if (errorCode.contains("ScanState return code: 0"))
			        	isSuccess = true;
			        
			        String grammers = "is";
			        if (isPlural)
			        	grammers = "are";
			        
			        if (isSuccess) {
			        	PopUpThread thread = new PopUpThread(userName + " " + grammers + " saved.", true);
			        	thread.start();
					       if (!userName.contains("All Users")) {
					    	   if (!succeededUsers.contains(userName))	//if not saved already
					    		   succeededUsers.add(userName);
					       }
			        }
			        
			        if (errorCode.contains("ScanState return code: 27")) {
			        	overriding = true;
			        	isSuccess = false;
			        	int reply = JOptionPane.showConfirmDialog(null, userName + " already exists as a save (may be old).\n"
			        			+ "Do you want to override it?",
			    				"Save Exists", JOptionPane.YES_NO_OPTION);
			        	if (reply == JOptionPane.YES_OPTION) {
			        		deleteFolder(userName.replace(" ", "_"));
			        	}
			        	if (reply == JOptionPane.NO_OPTION) {
			        		isSuccess = true;
			        		overriding = false;
			        	}
			        	//JOptionPane.getRootFrame().dispose();	//this lines clears all JOptionPanes, but breaks the "selected users" process under runBatchFileAndCheckForError
			        }
			        
			        grammers = "HAS";
			        if (isPlural)
			        	grammers = "HAVE";
			        
			        if (!isSuccess && !overriding) {
			        	failedUsers.add(userName);
			        	failCodes.add(errorCode);
			        	PopUpThread thread = new PopUpThread(userName + " " + grammers + " NOT BEEN SAVED.\n" + errorCode, false);
			        	thread.start();
			        }
			        
			       if (!userName.contains("All Users")) 
			    	   updateButtonNames();
			       
			       else 
			    	   if (!succeededAllUsers.contains(userName) && isSuccess)	//if not saved already
			    		   succeededAllUsers.add(userName);
			       
			       saveLog.rewrite();
			        
			      return null;
			    }
			  }.execute();
			}
		
	public void batFileOne() throws IOException {	//for one user
		
		file = new File(MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat");
		fos = new FileOutputStream(file); 
		dos = new DataOutputStream(fos); 
		dos.writeBytes("start \"\" cmd /c "
				+ "\n"
				+ "cd.."
				+ "\n"
				+ MainGUI.usbDrive
				+ "\n"
				+ "cd x86"
				+ "\n"
				+ "scanstate.exe "
				+ MainGUI.usbDrive
				+ "\\usmt\\"
				+ userName
				+ "\\statestore /ue:"
				+ "*\\* /ui:admdom\\"	//Change to ui:*\\bfurman for testing, ORIGINALLY: "\\statestore /ue:*\\* /ui:admdom\\"
				+ userName
				+ " /i:miguser.xml /config:config.xml /v:13 /l:"
				+ MainGUI.usbDrive
				+ "\\usmt\\Resources\\Scanlog.txt");
		
		runBatchFileAndCheckForError(false);
		
		JOptionPane.showMessageDialog (null, userName + " is being copied. Do not start another migration tool.",
				"Warning", JOptionPane.WARNING_MESSAGE);
	} 
	
	public void batFileAll() throws IOException { 
		
		file = new File(MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat");
		fos = new FileOutputStream(file); 
		dos = new DataOutputStream(fos); 
		dos.writeBytes("start \"\" cmd /c "
				+ "\n"
				+ "cd.."
				+ "\n"
				+ MainGUI.usbDrive
    			+ "\n"
    			+ "cd x86"
    			+ "\n"
    			+ "scanstate.exe "
    			+ MainGUI.usbDrive
    			+ "\\usmt\\"
    			+ "All_Users"
    			+ "\\statestore /ue:"
    			+ "*\\* "
    			+ "/ui:admdom\\* /i:miguser.xml /config:config.xml "
    			+ "/v:13 /l:"
    			+ MainGUI.usbDrive
    			+ "\\usmt\\Resources\\Scanlog.txt");
		
		userName = "All Users";
		
		runBatchFileAndCheckForError(true);
		
		JOptionPane.showMessageDialog (null, "All Users are being copied. Do not start another migration tool.",
				"Warning", JOptionPane.WARNING_MESSAGE);
	} 
	
	public void batFileDate(String timedInfo, String folderName) throws IOException {
		
		System.out.println("timedInfo: " + timedInfo + " folderName: " + folderName);
		
		file = new File(MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat");
		fos = new FileOutputStream(file); 
		dos = new DataOutputStream(fos);
		dos.writeBytes("start \"\" cmd /c "
				+ "\n"
				+ "cd.."
				+ "\n"
				+ MainGUI.usbDrive
    			+ "\n"
    			+ "cd x86"
    			+ "\n"
    			+ "scanstate.exe " 
    			+ MainGUI.usbDrive 
    			+ "\\usmt\\"
    			+ folderName
    			+ "\\statestore "
    			+ "/uel:"
    			+ timedInfo
    			+ " /ui:admdom\\* /i:miguser.xml /config:config.xml "
    			+ "/v:13 /l:" 
    			+ MainGUI.usbDrive 
    			+ "\\usmt\\Resources\\Scanlog.txt");
		System.out.println("timedInfo: " + timedInfo + " folderName: " + folderName);
		userName = folderName.replace("_", " ");
		
		runBatchFileAndCheckForError(true);
		
		JOptionPane.showMessageDialog (null, userName + " are being copied. Do not start another migration tool.",
				"Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(saveAllButton)) {
			if (MainGUI.inProgress) {	//prevent multitasking
	    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
	        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
			for (String i : selectedUserIndexes)	//clean selected users
	        		buttons[Integer.parseInt(i)].setBackground(defaultButtonColor);
	        	selectedUserIndexes.clear();
	        	selectedUsers.clear();
			
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to copy All Users?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	        	try {
					batFileAll();
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
	    					  "Error!", JOptionPane.ERROR_MESSAGE);
					}
	        }
		}
		if (e.getSource().equals(oneButton)) {
			firstScreen.setVisible(false);
			remove(firstScreen);
			setLayout(new GridLayout(2, 1, 0, 0));
			selectionScreen1.setVisible(true);
			selectionScreen2.setVisible(true);
			add(selectionScreen1);	
			add(selectionScreen2);
		}
		if (e.getSource().equals(searchButton) || e.getSource().equals(input)) {
			if (MainGUI.inProgress) {	//prevent multitasking
	    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
	        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
			
			for (String i : selectedUserIndexes)	//clean selected users when not selecting
	        		buttons[Integer.parseInt(i)].setBackground(defaultButtonColor);
	        	selectedUserIndexes.clear();
	        	selectedUsers.clear();
			
			String temp = input.getText().toLowerCase();
			if (temp != null)
				userName = temp;
			else
				userName = "";
			if (userIsFound()) {
				int reply = JOptionPane.showConfirmDialog(null, "Do you want to copy the user \""
       					+ userName + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);
       	        if (reply == JOptionPane.YES_OPTION) {
       	        	try {
       	        		batFileOne();
       	        	} catch (IOException z) {
       	        		z.printStackTrace();
       	        		JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
       	        		}
       	        }
			}
			else
				JOptionPane.showMessageDialog (null, "User \"" + userName
						+ "\" could not be found.\nPlease check the spelling of the username.", 
						"Not found", JOptionPane.WARNING_MESSAGE);
		}
		if (e.getSource().equals(secret)) {
			String[] options = new String[] {"Fantastic", "OK", "Stressed Out"};
		    int response = JOptionPane.showOptionDialog(null, "How are you feeling today?", "SECRET - Easter Egg!",
		        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		        null, options, options[0]);
		    if (response == 0)
		    	JOptionPane.showMessageDialog (null, "Today, you can sacrifice some of your happiness for a healthy sub.\n"
		    			+ "For lunch, let's go to Subway, and go light on the dressing.", 
						"SECRET - Easter Egg!", JOptionPane.WARNING_MESSAGE);
		    if (response == 1)
		    	JOptionPane.showMessageDialog (null, "Today you can go to Wendy's for lunch.\n"
		    			+ "It tastes good and isn't too bad for you.", 
						"SECRET - Easter Egg!", JOptionPane.WARNING_MESSAGE);
		    if (response == 2)
		    	JOptionPane.showMessageDialog (null, "You require some real greasy fast food.\n"
		    			+ "Time to go to McDonalds for a Big Mac with large fries.", 
						"SECRET - Easter Egg!", JOptionPane.WARNING_MESSAGE);
		}
		if (e.getSource().equals(selectsButton)) {
			
			if (MainGUI.inProgress) {	//prevent multitasking
	    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
	        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
			
			if (selectedUsers.size() <= 0)
				JOptionPane.showMessageDialog (null, "Please select at least one user.", 
						"Make a selection", JOptionPane.WARNING_MESSAGE);
			
			if (selectedUsers.size() > 0) {
				int reply = JOptionPane.showConfirmDialog(null, "Do you want to copy the selected users?",
						"Confirmation", JOptionPane.YES_NO_OPTION);
       	        if (reply == JOptionPane.YES_OPTION) {
       	        	try {
       	        		userName = selectedUsers.get(0);
       	        		batFileOne();
       	        	} catch (IOException z) {
       	        		z.printStackTrace();
       	        		JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
       	        		}
       	        }
			}
		}
		if (e.getSource().equals(backButton)) {
			selectionScreen1.setVisible(false);
			selectionScreen2.setVisible(false);
			remove(selectionScreen1);
			remove(selectionScreen2);
			setLayout(new GridLayout(1, 1, 0, 0));
			firstScreen.setVisible(true);
			add(firstScreen);	
		}
		if (e.getSource().equals(backAllButton)) {
			allScreenForIceScreen.setVisible(false);
			remove(allScreenForIceScreen);
			setLayout(new GridLayout(1, 1, 0, 0));
			firstScreen.setVisible(true);
			add(firstScreen);	
		}
		if (e.getSource().equals(allButton)) {
			firstScreen.setVisible(false);
			remove(firstScreen);
			setLayout(new GridLayout(1, 1, 0, 0));
			allScreenForIceScreen.setVisible(true);
			add(allScreenForIceScreen);
		}
		if (e.getSource().equals(saveAllByDaysSinceLogOnButton)) {
			if (MainGUI.inProgress) {	//prevent multitasking
	    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
	        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
			daysString = "" + JOptionPane.showInputDialog("Save all users logged in since this many days:");
			if (daysString.length() < 1 || daysString.equalsIgnoreCase("null")) {
				return;
			}
			for (int i = 0; i < daysString.length(); i++)
				if (daysString.charAt(i) < 48 || daysString.charAt(i) > 57) {
					JOptionPane.showMessageDialog (null, "Please enter a valid positive integer.", 
							"Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to save the user \""
   					+ "All Users Since " + daysString + " Days"
   					+ "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);
   	        if (reply == JOptionPane.YES_OPTION) {
   				try {
   					batFileDate(daysString, "All_Users_Since_" + daysString + "_Days");
   				} catch (IOException e1) {
   					e1.printStackTrace();
   					JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
	    					  "Error!", JOptionPane.ERROR_MESSAGE);
   					}
   	        }
		}
		if (e.getSource().equals(saveAllByDateButton)) {
			if (MainGUI.inProgress) {	//prevent multitasking
	    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
	        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	        final JTextField field1 = new JTextField("YYYY");
	        field1.addMouseListener(new MouseAdapter(){
	            public void mouseClicked(MouseEvent e){
	                field1.setText("");
	            }
	        });
	        final JTextField field2 = new JTextField("MM");
	        field2.addMouseListener(new MouseAdapter(){
	            public void mouseClicked(MouseEvent e){
	                field2.setText("");
	            }
	        });
	        final JTextField field3 = new JTextField("DD");
	        field3.addMouseListener(new MouseAdapter(){
	            public void mouseClicked(MouseEvent e){
	                field3.setText("");
	            }
	        });
	        JPanel panel = new JPanel(new GridLayout(0, 1));
	        panel.add(new JLabel("Save all users logged in since this date:"));
	        panel.add(new JLabel("Year:"));
	        panel.add(field1);
	        panel.add(new JLabel("Month:"));
	        panel.add(field2);
	        panel.add(new JLabel("Day:"));
	        panel.add(field3);
	        int result = JOptionPane.showConfirmDialog(null, panel, "Save by date",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	        	
	            String f1 = field1.getText();	//year
	            String f2 = field2.getText();	//month
	            String f3 = field3.getText();	//day
	            
	            if (f1.length() != 4) {
	            	JOptionPane.showMessageDialog (null, "Enter a valid year.", 
		        			"Error", JOptionPane.ERROR_MESSAGE);
	            	return;
	            }
	            if (f2.length() > 2 || f2.length() < 1) {
	            	JOptionPane.showMessageDialog (null, "Enter a valid month.", 
		        			"Error", JOptionPane.ERROR_MESSAGE);
	            	return;
	            }
	            if (f3.length() > 2 || f3.length() < 1) {
	            	JOptionPane.showMessageDialog (null, "Enter a valid day.", 
		        			"Error", JOptionPane.ERROR_MESSAGE);
	            	return;
	            }
	            for (int i = 0; i < f1.length(); i++)
					if (f1.charAt(i) < 48 || f1.charAt(i) > 57) {
						JOptionPane.showMessageDialog (null, "Enter a valid year.", 
			        			"Error", JOptionPane.ERROR_MESSAGE);
		            	return;
					}
	            for (int i = 0; i < f2.length(); i++)
					if (f2.charAt(i) < 48 || f2.charAt(i) > 57) {
						JOptionPane.showMessageDialog (null, "Enter a valid month.", 
			        			"Error", JOptionPane.ERROR_MESSAGE);
		            	return;
					}
	            for (int i = 0; i < f3.length(); i++)
					if (f3.charAt(i) < 48 || f3.charAt(i) > 57) {
						JOptionPane.showMessageDialog (null, "Enter a valid day.", 
			        			"Error", JOptionPane.ERROR_MESSAGE);
		            	return;
					} 
	            if (Integer.parseInt(f2) > 12) {
	            	JOptionPane.showMessageDialog (null, "Enter a valid month.", 
		        			"Error", JOptionPane.ERROR_MESSAGE);
	            	return;
	            }
	            if (Integer.parseInt(f3) > 31) {
	            	JOptionPane.showMessageDialog (null, "Enter a valid day.", 
		        			"Error", JOptionPane.ERROR_MESSAGE);
	            	return;
	            }
	            String date = f1 + "/" + f2 + "/" + f3;
	            
				int reply = JOptionPane.showConfirmDialog(null, "Do you want to save the user \""
       					+ "All Users Since " + f2 + "-" + f3 + "-" + f1
       					+ "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);
       	        if (reply == JOptionPane.YES_OPTION) {
    	            try {
    					batFileDate(date, "All_Users_Since_" + f2 + "-" + f3 + "-" + f1);
    				} catch (IOException e1) {
    					e1.printStackTrace();
    					JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
    					}
       	        }
	        } 
		}
		if (e.getSource().equals(deleteButton)) {
			if (MainGUI.inProgress) {	//prevent multitasking
	    		JOptionPane.showMessageDialog (null, "A migration tool is active.\nTry again later.", 
	        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to delete all existing copied users on the " + MainGUI.usbDrive + " usb drive?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
   	        if (reply == JOptionPane.YES_OPTION) {
   	        	String path = MainGUI.usbDrive + "\\USMT\\";
   	        	File file = new File(path);
   	        	String[] files = file.list();
   	        	for (String fileName : files)
   	        		if (!fileName.equals("Resources") && !fileName.equals("x64") && !fileName.equals("x86"))
   	        			if (new File(MainGUI.usbDrive + "\\USMT\\" + fileName + "\\statestore\\USMT\\USMT.MIG").exists())
   	        				deleteFolder(fileName);
   	    		try {
					saveLog.rewrite();
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog (null, "Could not rewrite saveLog text file at " + MainGUI.usbDrive + "\\USMT\\Resources\\savedUsers.txt",
	    					  "Error!", JOptionPane.ERROR_MESSAGE);
					}
   	    		updateButtonNames();
   	        	JOptionPane.showMessageDialog (null, "All saved users on the " + MainGUI.usbDrive + " usb drive have been deleted.", 
	        			"Done", JOptionPane.INFORMATION_MESSAGE);
   	        }
		}
		if (e.getSource().equals(selectAllButton)) {
			
			selectedUsers.clear();
			selectedUserIndexes.clear();
			
			for (int i = 0; i < userList.size(); i++) {
				buttons[i].setBackground(Color.GREEN);
				selectedUsers.add(userList.get(i));
				selectedUserIndexes.add(i + "");
			}
		}
	}
}
