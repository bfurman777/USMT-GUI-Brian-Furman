package usmt;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class SavedLog {
	
	private File file;
	private FileOutputStream fos;
	private DataOutputStream dos;
	private Scanner scan;
	
	public SavedLog() throws IOException {	
		
		try {
			file = new File(MainGUI.usbDrive + "\\USMT\\Resources\\savedUsers.txt");
		}
		finally{
			if (!new File(MainGUI.usbDrive + "\\USMT\\Resources").exists()) {
				JOptionPane.showMessageDialog (null, "Can not find " + MainGUI.usbDrive + "\\USMT\\Resources!\n"
					+ "Check if the USMT folder and USMT.bat is on the USB stick.\nProgram will close.", "Drive not found", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		if (!new File(MainGUI.usbDrive + "\\USMT\\Resources\\savedUsers.txt").exists()) {
			file = new File(MainGUI.usbDrive + "\\USMT\\Resources\\savedUsers.txt");
			fos = new FileOutputStream(file); 
			dos = new DataOutputStream(fos);
		}
		
		scan = new Scanner (file);
		while (scan.hasNext()) {
			String temp = scan.nextLine();
			System.out.println(temp);
			
			if (temp.contains("All Users"))
				ScanStateGUI.succeededAllUsers.add(temp);
			else
				ScanStateGUI.succeededUsers.add(temp);
		}
		rewrite();
		
		System.out.println(ScanStateGUI.succeededUsers);
		System.out.println(ScanStateGUI.succeededAllUsers);
	}
	
	public void rewrite() throws IOException {
		
		file = new File(MainGUI.usbDrive + "\\USMT\\Resources\\savedUsers.txt");
		fos = new FileOutputStream(file); 
		dos = new DataOutputStream(fos);
		
		for (String s : ScanStateGUI.succeededUsers)
			dos.writeBytes(s + "\n");
		for (String s : ScanStateGUI.succeededAllUsers)
			dos.writeBytes(s + "\n");
	}

}


