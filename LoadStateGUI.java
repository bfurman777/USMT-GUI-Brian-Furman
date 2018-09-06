package usmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingWorker;
import javax.swing.border.Border;

public class LoadStateGUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private static FileOutputStream fos;
	private static DataOutputStream dos;
	private static File file;
	public static ArrayList <String> selectedUsers;
	public static ArrayList <String> selectedUserIndexes;
	private static ArrayList <String> failedUsers, failCodes;
	private static String userName = "";
	private static String errorCode = "LoadState return code: null";
	private JPanel firstScreen, selectionScreen1, selectionScreen2, allScreenForIceScreen;
	private static JPanel interPanel, innerPanel;
	private final static int SCREEN_WIDTH = 700;
	private final int SCREEN_HEIGHT = 350;
	private JButton allButton, oneButton, secret, searchButton, backButton, selectsButton, selectAllButton, backAllButton;
	private TextField input;
	private JLabel introLabel, topLabel;
	private Font font1, font2, font3, font5;
	private static ArrayList <JButton> buttons, allButtons;
	private static Color defaultButtonColor;
	private Border emptyBorder;
	private static boolean severalSelects = false;
	
	private static Action action = new AbstractAction("New Background Thread") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			  runBatchFileAndCheckForError(false);
		  }
		};
	
	public LoadStateGUI() throws IOException {
		
		selectedUsers = new ArrayList<String>();
		selectedUserIndexes = new ArrayList<String>();
		failedUsers = new ArrayList<String>();
		failCodes = new ArrayList<String>();
		emptyBorder = BorderFactory.createEmptyBorder();
		
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
		
		font5 = new Font("Dialog", Font.BOLD, 13);
		allButton = new JButton();
		allButton.setFont(font5);
		defaultButtonColor = allButton.getBackground();
		allButton.setText("Load All Users");
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
		oneButton.setText("Load Select Users");
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
		introLabel.setText("How do you want to load your user(s)?");
		introLabel.setFont(font2);
		firstScreen.add(introLabel);
		introLabel.setBounds(135, 20, 500, 35);
		
		remove(firstScreen);
	}
	
	public void initSelectionScreen1() {	//top part of page 2
		setLayout(new GridLayout(2, 1, 0, 0));
		
		selectionScreen1 = new JPanel();
		add(selectionScreen1);
		selectionScreen1.setFocusable(true);
		selectionScreen1.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT/2));
		
		selectionScreen1.setLayout(null);
		
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
	    selectsButton.setText("Load Selected Users Below");
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
		
		int length = ScanStateGUI.succeededUsers.size();
	
	    innerPanel = new JPanel(new GridLayout(length, 1, 0, 0));
	    buttons = new ArrayList<JButton>();
	    
	    selectedUsers.clear();
	    selectedUserIndexes.clear();
	   
	    for(int i = 0; i < length; i++) {
	    	
            buttons.add(new JButton(ScanStateGUI.succeededUsers.get(i)));
            innerPanel.add(buttons.get(i));
            final int buttonIndex = i;
            buttons.get(i).addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent ae2) {
           			if (!selectedUserIndexes.contains(buttonIndex + "")) {
           				buttons.get(buttonIndex).setBackground(Color.GREEN);
           				selectedUsers.add(ScanStateGUI.succeededUsers.get(buttonIndex));
           				selectedUserIndexes.add(buttonIndex + "");
           			}
           			else {
           				buttons.get(buttonIndex).setBackground(defaultButtonColor);
           				selectedUsers.remove(ScanStateGUI.succeededUsers.get(buttonIndex));
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
		
    	allScreenForIceScreen = new JPanel(new BorderLayout());
		add(allScreenForIceScreen);
		allScreenForIceScreen.setFocusable(true);
		allScreenForIceScreen.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		

	    JPanel topPanel = new JPanel();
	    topPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, 150));
	    allScreenForIceScreen.add(topPanel, BorderLayout.NORTH);
	    topPanel.setLayout(null);
	    
		JLabel text = new JLabel();
		text.setText("All copied \"All User\" groups will appear below.");
		text.setFont(font2);
		topPanel.add(text);
		text.setBounds(121, 40, 600, 35);
		
	    JLabel text2 = new JLabel();
	    text2.setText("Click one below to load it.");
	    text2.setFont(font2);
	    text2.setBounds(200, 85, 600, 35);
	    topPanel.add(text2);
	    
	    backAllButton = new JButton();
	    topPanel.add(backAllButton);
	    backAllButton.setBounds(10, 10, 63, 63);
	    backAllButton.addActionListener(this);
	    backAllButton.setIcon(new ImageIcon(getClass().getResource("resources/back.png")));
	    backAllButton.setContentAreaFilled(false);
	    backAllButton.setBorder(emptyBorder);
	    backAllButton.setRolloverIcon(new ImageIcon(getClass().getResource("resources/s-back.png")));
 
	    int length = ScanStateGUI.succeededAllUsers.size();
	
	    interPanel = new JPanel(new GridLayout(length, 1, 0, 0));
	    allButtons = new ArrayList<JButton>();
	    
	    for(int i = 0; i < length; i++) {
	    	String name = ScanStateGUI.succeededAllUsers.get(i);
	  
	    	allButtons.add(new JButton(name));
	    	allButtons.get(i).setPreferredSize(new Dimension(SCREEN_WIDTH - 25, 77));
	    	
	    	interPanel.add(allButtons.get(i));
            final int buttonIndex = i;
            allButtons.get(i).addActionListener(new ActionListener() {
            	
               public void actionPerformed(ActionEvent ae2) {
            	   
            	   int reply = JOptionPane.showConfirmDialog(null, "Do you want to load " + ScanStateGUI.succeededAllUsers.get(buttonIndex) + "?",
   						"Confirmation", JOptionPane.YES_NO_OPTION);
          	        if (reply == JOptionPane.YES_OPTION) {
          	        	userName = ScanStateGUI.succeededAllUsers.get(buttonIndex);
          	        	try {
          	        		batFileOne(ScanStateGUI.succeededAllUsers.get(ScanStateGUI.succeededAllUsers.size() - 1).replace(" ", "_"));
          	        	} catch (IOException e) {
          	        		e.printStackTrace();
          	        		JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
			    					  "Error!", JOptionPane.ERROR_MESSAGE);
          	        		}
          	        }
               }
            });
        }
	    JScrollPane scrollPanel = new JScrollPane(interPanel);
	    allScreenForIceScreen.add(scrollPanel, BorderLayout.CENTER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        
		remove(allScreenForIceScreen);
	}
	
	public boolean userIsFound() {
		
		for (int i = 0; i < ScanStateGUI.succeededUsers.size(); i++)
			if (userName.equalsIgnoreCase(ScanStateGUI.succeededUsers.get(i)))
				return true;
		return false;
	}

	public static void updateButtonNames() {
		
		//select users panel
		innerPanel.setLayout(new GridLayout(ScanStateGUI.succeededUsers.size(), 1, 0, 0));
		innerPanel.removeAll();
		buttons.clear();
		
	    for(int i = 0; i < ScanStateGUI.succeededUsers.size(); i++) {
            JButton b = new JButton();
            buttons.add(b);
            b.setText(ScanStateGUI.succeededUsers.get(i));
            final int buttonIndex = i;
            b.addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent ae2) {
           			if (!selectedUserIndexes.contains(buttonIndex + "")) {
           				buttons.get(buttonIndex).setBackground(Color.GREEN);
           				selectedUsers.add(ScanStateGUI.succeededUsers.get(buttonIndex));
           				selectedUserIndexes.add(buttonIndex + "");
           			}
           			else {
           				buttons.get(buttonIndex).setBackground(defaultButtonColor);
           				selectedUsers.remove(ScanStateGUI.succeededUsers.get(buttonIndex));
           				selectedUserIndexes.remove(buttonIndex + "");
           			}
               }  
            });
	    	b.setPreferredSize(new Dimension(SCREEN_WIDTH - 25, 77));
	    	innerPanel.add(b);
        }
    	innerPanel.setVisible(false);
    	innerPanel.setVisible(true);

		//allPanel
		interPanel.setLayout(new GridLayout(ScanStateGUI.succeededAllUsers.size(), 1, 0, 0));
		interPanel.removeAll();
			
		for (int i = 0; i < ScanStateGUI.succeededAllUsers.size(); i++) {
			JButton b = new JButton();
			b.setText(ScanStateGUI.succeededAllUsers.get(i));
			final int buttonIndex = i;
    		b.addActionListener(new ActionListener() {
	    			
            	public void actionPerformed(ActionEvent ae2) {
	            		
	            	int reply = JOptionPane.showConfirmDialog(null, "Do you want to load " + ScanStateGUI.succeededAllUsers.get(buttonIndex) + "?",
	    					"Confirmation", JOptionPane.YES_NO_OPTION);
	                if (reply == JOptionPane.YES_OPTION) {
	       	        	userName = ScanStateGUI.succeededAllUsers.get(buttonIndex);
	       	        	try {
          	        		batFileOne(ScanStateGUI.succeededAllUsers.get(ScanStateGUI.succeededAllUsers.size() - 1).replace(" ", "_"));
	       	        	} catch (IOException e) {
	       	        		e.printStackTrace();
	       	        		JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
			    					  "Error!", JOptionPane.ERROR_MESSAGE);
	       	        		}
	                }
	            }  
	         });
	    	b.setPreferredSize(new Dimension(SCREEN_WIDTH - 25, 77));
	    	interPanel.add(b);
		} 
    	interPanel.setVisible(false);
    	interPanel.setVisible(true);	
	}
	
	public static void runBatchFileAndCheckForError(final boolean isPlural) {
		
			  new SwingWorker<Void, Void>() {
			    {
			      // Disable action until task is complete to prevent concurrent tasks.
			      action.setEnabled(false);
			      
			      System.out.println(selectedUsers + " " + selectedUsers.size());
			      
			    }

			    // Called on the Swing thread when background task completes.
			    protected void done() {
			      action.setEnabled(true);
			      MainGUI.inProgress = false;
			      System.out.println(selectedUsers.size());

			      try {
			        // No result but calling get() will propagate any exceptions onto Swing thread.
			        get();
			      } catch(Exception ex) {
			        ex.printStackTrace();
			      	}

			      if (selectedUsers.size() > 0) {
			    	  selectedUsers.remove(0);
			    	  if (severalSelects == true && selectedUsers.size() == 0) {
			    		  severalSelects = false;
			    		  JOptionPane.showMessageDialog (null, "All selected users have been loaded.",
		    					  "Done", JOptionPane.INFORMATION_MESSAGE);
			    		  JOptionPane.getRootFrame().dispose();
			    	  }
			      }
			      if (selectedUsers.size() > 0) {
			    	  severalSelects = true;
			    	  userName = selectedUsers.get(0);
			    	  try {
						batFileOne(userName);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
						}
			      }
			    }

			    // Called on background thread
			    protected Void doInBackground() throws Exception {
			    	
			    	if (!new File(MainGUI.usbDrive + "\\USMT\\x86\\loadstate.exe").exists()) {	//if loadstate.exe is missing
			    		JOptionPane.showMessageDialog (null, "loadstate.exe could not be found at:\n"
			    				+ MainGUI.usbDrive + "\\USMT\\x86\\loadstate.exe\nPlease check the file path.", 
			        			"Missing loadstate.exe", JOptionPane.ERROR_MESSAGE);
			    		return null;
			    	}
			    	if (MainGUI.inProgress) {	//prevent multitasking
			    		JOptionPane.showMessageDialog (null, "Another migration tool is already active.\nTry again later.", 
			        			"Too many tasks", JOptionPane.ERROR_MESSAGE);
			    		return null;
			    	}
			    	MainGUI.inProgress = true;
			    	if (!userName.contains("All Users")) 
			    		buttons.get(ScanStateGUI.succeededUsers.indexOf(userName)).setText(userName + "           (in progress...)");
			    	
					CMD cmd = new CMD(userName, "LoadState");
			    	
			    	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "temp.bat").redirectErrorStream(true);
			        builder.directory(new File(MainGUI.usbDrive + "\\USMT\\Resources\\"));
			        Process process = builder.start();
			       
			        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			        String line;
			        while ((line = input.readLine()) != null) {
			        	
			            if (line.contains("LoadState return code:"))
			            	errorCode = line;
			            
			            cmd.write(line);
			        }
			        input.close();
			        try {
						process.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog (null, "There was an error reading the cmd.",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
						} 
			        
			        boolean isSuccess = false;
			        
			        if (errorCode.contains("LoadState return code: 0"))
			        	isSuccess = true;
			        
			        String grammers = "is";
			        if (isPlural)
			        	grammers = "are";
			        
			        if (isSuccess) {
			        	PopUpThread thread = new PopUpThread(userName + " " + grammers + " loaded.", true);
			        	thread.start();
			        }
			        
			        grammers = "HAS";
			        if (isPlural)
			        	grammers = "HAVE";
			        
			        if (!isSuccess) {
			        	failedUsers.add(userName);
			        	failCodes.add(errorCode);
			        	PopUpThread thread = new PopUpThread(userName + " " + grammers + " NOT BEEN LOADED.\n" + errorCode, false);
			        	thread.start();
			        }
			        
			       if (!userName.contains("All Users"))
			    	   updateButtonNames();
			        
			      return null; // No result so simply return null.
			    }
			  }.execute();
			}
		
	public static void batFileOne(String fileName) throws IOException {	//for one user
		
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
				+ "loadstate.exe "
				+ MainGUI.usbDrive
				+ "\\USMT\\"
				+ fileName
				+ "\\statestore /ue:"
				+ "*\\* /ui:admdom\\* /i:miguser.xml /config:config.xml /v:13 /l:"
				+ MainGUI.usbDrive
				+ "\\USMT\\Resources\\Loadlog.txt");
		
		runBatchFileAndCheckForError(false);
		
		JOptionPane.showMessageDialog (null, userName + " is being loaded. Do not start another migration tool.",
				"Warning", JOptionPane.WARNING_MESSAGE);
	} 

	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(allButton)) {
			
			for (String i : selectedUserIndexes)	//clean selected users
	        		buttons.get(Integer.parseInt(i)).setBackground(defaultButtonColor);
	        selectedUserIndexes.clear();
	        selectedUsers.clear();
			
	        firstScreen.setVisible(false);
			remove(firstScreen);
			setLayout(new GridLayout(1, 1, 0, 0));
			allScreenForIceScreen.setVisible(true);
			add(allScreenForIceScreen);
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
	        		buttons.get(Integer.parseInt(i)).setBackground(defaultButtonColor);
	        	selectedUserIndexes.clear();
	        	selectedUsers.clear();
			
			String temp = input.getText().toLowerCase();
			if (temp != null)
				userName = temp;
			else
				userName = "";
			if (userIsFound()) {
				int reply = JOptionPane.showConfirmDialog(null, "Do you want to load the user \""
       					+ userName + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);
       	        if (reply == JOptionPane.YES_OPTION) {
       	        	try {
       	        		batFileOne(userName);
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
				int reply = JOptionPane.showConfirmDialog(null, "Do you want to load the selected users?",
						"Confirmation", JOptionPane.YES_NO_OPTION);
       	        if (reply == JOptionPane.YES_OPTION) {
       	        	try {
       	        		userName = selectedUsers.get(0);
       	        		batFileOne(userName);
       	        	} catch (IOException z) {
       	        		z.printStackTrace();
       	        		JOptionPane.showMessageDialog (null, "Could not write batch file at " + MainGUI.usbDrive + "\\USMT\\Resources\\temp.bat",
		    					  "Error!", JOptionPane.ERROR_MESSAGE);
       	        		}
       	        	for (String i : selectedUserIndexes) 
       	        		buttons.get(Integer.parseInt(i)).setBackground(defaultButtonColor);
       	        	selectedUserIndexes.clear();
       	        	//selectedUsers.clear();	//runBatchFileAndCheckForError uses it
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
		if (e.getSource().equals(selectAllButton)) {
			
			selectedUsers.clear();
			selectedUserIndexes.clear();
			
			innerPanel.setLayout(new GridLayout(ScanStateGUI.succeededUsers.size(), 1, 0, 0));
			innerPanel.removeAll();
			buttons.clear();
			
		    for(int i = 0; i < ScanStateGUI.succeededUsers.size(); i++) {
	            JButton b = new JButton();
	            buttons.add(b);
	            b.setText(ScanStateGUI.succeededUsers.get(i));
	            final int buttonIndex = i;
	            b.addActionListener(new ActionListener() {
	 
	               public void actionPerformed(ActionEvent ae2) {
	           			if (!selectedUserIndexes.contains(buttonIndex + "")) {
	           				buttons.get(buttonIndex).setBackground(Color.GREEN);
	           				selectedUsers.add(ScanStateGUI.succeededUsers.get(buttonIndex));
	           				selectedUserIndexes.add(buttonIndex + "");
	           			}
	           			else {
	           				buttons.get(buttonIndex).setBackground(defaultButtonColor);
	           				selectedUsers.remove(ScanStateGUI.succeededUsers.get(buttonIndex));
	           				selectedUserIndexes.remove(buttonIndex + "");
	           			}
	               }  
	            });
	            //selecting
   				buttons.get(i).setBackground(Color.GREEN);
   				selectedUsers.add(ScanStateGUI.succeededUsers.get(i));
   				selectedUserIndexes.add(i + "");
   				
		    	b.setPreferredSize(new Dimension(SCREEN_WIDTH - 25, 77));
		    	innerPanel.add(b);
	        }
	    	innerPanel.setVisible(false);
	    	innerPanel.setVisible(true);	
		}	
	}
}


