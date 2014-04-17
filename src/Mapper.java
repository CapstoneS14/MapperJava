import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Mapper {

	private static Properties properties = new Properties();
	private static String propertyFile = "config/config.properties";
	private static String htmlFilePath = "webcontent/index.html";
	private static final String KEY_PATH_CHROME_EXE = "path.chrome.exe";
	private static final String ERR_MSG_TITLE_FILE_LOAD = "File Load Error !";
	private static final String ERR_MSG_FILE_LOAD = "Error loading the property file \"config.properties\" \n Application will shut down!";
	private static final String ERR_MSG_TITLE_FILE_SAVE = "File SaveError !";
	private static final String ERR_MSG_FILE_SAVE = "Error updating the property file \"config.properties\" \n Application will shut down!";
	private static final String WARN_MSG_TITLE_CHROME_SETUP = "Choose Chrome.exe Location";
	private static final String WARN_MSG_CHROME_SETUP = "Valid chrome.exe not found ! In the following step, please select the location where chrome.exe can be found";
	private static JOptionPane warningWindow = new JOptionPane();

	public static void main(String args[]) {
		setupChrome();
		initApp();
	}

	private static void setupChrome() {
		loadProperties();
		File chromeExe = null;
		try {
			String chromeExePath = properties.getProperty(KEY_PATH_CHROME_EXE);
			if (!isValidChrome(chromeExePath)) {
				try {
					JOptionPane.showMessageDialog(warningWindow,
							WARN_MSG_CHROME_SETUP, WARN_MSG_TITLE_CHROME_SETUP,
							JOptionPane.WARNING_MESSAGE);

					while ((chromeExe = chooseFile()) != null
							&& !isValidChrome(chromeExe.getAbsolutePath())) {

						JOptionPane.showMessageDialog(warningWindow,
								WARN_MSG_CHROME_SETUP,
								WARN_MSG_TITLE_CHROME_SETUP,
								JOptionPane.WARNING_MESSAGE);
					}

					String key = KEY_PATH_CHROME_EXE;
					String value = chromeExe.getAbsolutePath();
					updateProperties(key, value);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				} 

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void loadProperties() {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(propertyFile);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException iOException) {
			iOException.printStackTrace();
			JOptionPane.showMessageDialog(new JOptionPane(), ERR_MSG_FILE_LOAD,
					ERR_MSG_TITLE_FILE_LOAD, JOptionPane.ERROR_MESSAGE);
			try {
				inputStream.close();
			} catch (IOException ioException2) {
				JOptionPane.showMessageDialog(new JOptionPane(),
						ERR_MSG_FILE_LOAD, ERR_MSG_TITLE_FILE_LOAD,
						JOptionPane.ERROR_MESSAGE);
			}
			System.exit(0);
			
		}
	}

	private static File chooseFile() {
		File chosenFile = null;
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(false);
			int option = chooser.showOpenDialog(chooser);
			if (option == JFileChooser.APPROVE_OPTION) {
				chosenFile = chooser.getSelectedFile();
			} else {
				chosenFile = null;
			}
		} catch (Exception e) {
			chosenFile = null;
			e.printStackTrace();
			System.exit(0);
		}
		return chosenFile;
	}

	private static void updateProperties(String key, String value) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(propertyFile);
			properties.setProperty(key, value);
			properties.store(outputStream, null);
			outputStream.close();
		} catch (IOException iOException) {
			iOException.printStackTrace();
			JOptionPane.showMessageDialog(new JOptionPane(), ERR_MSG_FILE_SAVE,
					ERR_MSG_TITLE_FILE_SAVE, JOptionPane.ERROR_MESSAGE);
			try {
				outputStream.close();
			} catch (IOException ioException2) {
				JOptionPane.showMessageDialog(new JOptionPane(),
						ERR_MSG_FILE_SAVE, ERR_MSG_TITLE_FILE_SAVE,
						JOptionPane.ERROR_MESSAGE);

			}
			System.exit(0);
		}
	}

	private static boolean isValidChrome(String path) {
		return path != null && !path.isEmpty()
				&& path.toLowerCase().endsWith("chrome.exe")
				&& (new File(path)).exists();
	}

	private static void initApp(){
		
		try {
			String commands[] = new String[2]; 
			String browser = properties.getProperty(KEY_PATH_CHROME_EXE);
			File htmlFile = new File(htmlFilePath);
			commands[0] = browser;
			commands[1] = htmlFile.getAbsolutePath();
	  
			Process browserProcess = Runtime.getRuntime().exec(commands);
			browserProcess.waitFor();
			//File htmlFile = new File(relativefilePath); 
			//Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (Exception e) { 
			e.printStackTrace();
			System.exit(0);
		}	 		
	}
}
