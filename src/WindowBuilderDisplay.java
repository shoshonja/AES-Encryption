import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;


public class WindowBuilderDisplay {

	JFrame frame;
	private JFileChooser fileChooser;
	private File file;
	private JTextArea informationText;
	//private JComboBox encryptionSelector;
	int returnVal;
	String currentLine;
	String encrypted;
	

	public WindowBuilderDisplay() {
		initialize();
	}

	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 451, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//file chooser
		fileChooser = new JFileChooser();
		
		//labels
		JLabel lblEncryptionKey = new JLabel("Load key:");
		lblEncryptionKey.setBounds(12, 13, 131, 16);
		frame.getContentPane().add(lblEncryptionKey);
		
		
		JLabel lblFolderEncryption = new JLabel("Encryption:");
		lblFolderEncryption.setBounds(12, 67, 179, 16);
		frame.getContentPane().add(lblFolderEncryption);
		
		JLabel lblFolderDecryption = new JLabel("Decryption:");
		lblFolderDecryption.setBounds(12, 121, 179, 16);
		frame.getContentPane().add(lblFolderDecryption);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 175, 409, 277);
		frame.getContentPane().add(scrollPane);
		
		//text area
		informationText = new JTextArea();
		scrollPane.setViewportView(informationText);
		informationText.setText("This is new text area");
		informationText.setEditable(false);
		
			
		
		//buttons
		//_______________________________________________________
		//browse key gotov i radi!
		final JButton btnBrowseKey = new JButton("Browse Key");
		btnBrowseKey.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getSource() == btnBrowseKey){
						try {
							BrowseKey();
						} catch (IOException e) {
							System.out.println("Error in JButton browse key");
							e.printStackTrace();
						}
						if(EncryptionAES.keyValue != null){
							//testiranje je li key dobre duljine
							//tu ispisati neku potvrdnu poruku
							informationText.setText("Selected key: \n"+ file.toString());
							//String s = new String(EncryptionAES.keyValue);
							//txtrInfoLog.append(s);
						}
					}
				}
			});
		btnBrowseKey.setBounds(12, 29, 119, 25);
		frame.getContentPane().add(btnBrowseKey);
		
		//E-FILE
		//____________________________________________
		//ako je izabran kljuc, postavlja encrypting na true
		//poziva browse file, zatim encrypt, zatim write file te u informationText ispisuje rezultat
		final JButton btnEncryptFile = new JButton("Encrypt File");
		btnEncryptFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == btnEncryptFile){
					if(EncryptionAES.keyValue!= null){
						try {
							EncryptionAES.encrypting = true;
							BrowseFile();
							
							encrypted = EncryptionAES.encrypt(EncryptionAES.selectedFolderContent);
							EncryptionAES.writeFile(file, encrypted);
							informationText.append("\nEncrypted file: " + encrypted);
							
							
						} catch (IOException e) {
							System.out.println("Browse File error");
							e.printStackTrace();
						} catch (Exception e) {
							System.out.println("Encryption error");
							e.printStackTrace();
						}
					}else{
						System.out.println("No key selected");
						informationText.append("\nNo key selected!");
					}
				}
			}
		});
		btnEncryptFile.setBounds(12, 84, 119, 25);
		frame.getContentPane().add(btnEncryptFile);
		
		
		//D-FILE
		//____________________________________________
		//ako je izabran kljuc, postavlja encrypting na false
		//poziva browse file, zatim decrypt, zatim write file te u informationText ispisuje rezultat
		final JButton btnDecryptFile = new JButton("Decrypt File");
		btnDecryptFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == btnDecryptFile){
					if(EncryptionAES.keyValue!= null){
						try {
							EncryptionAES.encrypting = false;
							BrowseFile();
							String decrypted;
							decrypted = EncryptionAES.decrypt(EncryptionAES.selectedFolderContent);
							EncryptionAES.writeFile(file, decrypted);
							informationText.append("\nDecrypted file: " + decrypted);
							
						} catch (IOException e) {
							System.out.println("Browse File error");
							e.printStackTrace();
						} catch (Exception e) {
							System.out.println("Encryption error");
							e.printStackTrace();
						}
					}else{
						System.out.println("No key selected");
						informationText.append("\nNo key selected!");
					}
				}
			}
		});
		btnDecryptFile.setBounds(12, 137, 119, 25);
		frame.getContentPane().add(btnDecryptFile);
		
		//E-FOLDER
		//____________________________________________
		//ako je izabran kljuc, postavlja encrypting na true
		//poziva browse folder, zatim encrypt za svaki element, zatim write file te u informationText ispisuje rezultat
		//ne kriptira se file koji ima vec Coded- u imenu
		final JButton btnEncryptFolder = new JButton("Encrypt Folder ");
		btnEncryptFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == btnEncryptFolder){
					if(EncryptionAES.keyValue!= null){
						EncryptionAES.encrypting = true;
						BrowseFolder();
						
						int listIndex = 0;
						for (File file : EncryptionAES.selectedFolder) {
							String extensionChecker = FilenameUtils.getExtension(EncryptionAES.selectedFolder[listIndex].toString());
							String fileNameChecker = file.toString();
							if(extensionChecker.equals("txt") && !fileNameChecker.contains("Coded-") ){
								
								try {
									EncryptionAES.selectedFolderContent = EncryptionAES.readFile(file);
									encrypted = EncryptionAES.encrypt(EncryptionAES.selectedFolderContent);
									EncryptionAES.writeFile(file, encrypted);
									informationText.append("\nEncrypted file: " + encrypted);
									
								} catch (IOException e) {
									System.out.println("Browse File error");
									e.printStackTrace();
								} catch (Exception e) {
									System.out.println("Encryption error");
									e.printStackTrace();
								}
							}		
							listIndex++;
						}
					}else{
						System.out.println("No key selected");
						informationText.append("\nNo key selected!");
					}
				}
			}
		});
		btnEncryptFolder.setBounds(138, 84, 119, 25);
		frame.getContentPane().add(btnEncryptFolder);
	
		//D-FOLDER
		//____________________________________________
		//ako je izabran kljuc, postavlja encrypting na false
		//poziva browse folder, zatim decrypt za svaki element, zatim write file te u informationText ispisuje rezultat
		final JButton btnDecryptFolder = new JButton("Decrypt Folder");
		btnDecryptFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == btnDecryptFolder){
					if(EncryptionAES.keyValue!= null){
						EncryptionAES.encrypting = false;
						BrowseFolder();
						
						int listIndex = 0;
						for (File file : EncryptionAES.selectedFolder) {
							String extensionChecker = FilenameUtils.getExtension(EncryptionAES.selectedFolder[listIndex].toString());
							
							if(extensionChecker.equals("txt")){
								
								try {
									EncryptionAES.selectedFolderContent = EncryptionAES.readFile(file);
									encrypted = EncryptionAES.decrypt(EncryptionAES.selectedFolderContent);
									EncryptionAES.writeFile(file, encrypted);
									informationText.append("\nDecrypted file: " + encrypted);
									
								} catch (IOException e) {
									System.out.println("Browse File error");
									e.printStackTrace();
								} catch (Exception e) {
									System.out.println("Encryption error");
									e.printStackTrace();
								}
							}		
							listIndex++;
						}
						
					}else{
						System.out.println("No key selected");
						informationText.append("\nNo key selected!");
					}
				}
			}
		});
		btnDecryptFolder.setBounds(138, 137, 119, 25);
		frame.getContentPane().add(btnDecryptFolder);	
		
	}
	//pomocne funkcije
	//_________________________________________________________
	//ovo je isto dobro cini mi se
	//selectedFolder lista se napuni sa svim linkovima fajlova unutar izabranog foldera
	private void BrowseFolder(){
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnVal = fileChooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
							
				EncryptionAES.selectedFolderLocation = fileChooser.getSelectedFile().toString();		
				File folderLocation = new File(EncryptionAES.selectedFolderLocation);
				EncryptionAES.selectedFolder = folderLocation.listFiles();					
		}
	}
	
	//ovo je sada dobro
	//string selectedFolderContent dobije sadrzaj foldera
	//modificirati browse file da popuni i selectedfolder
	private void BrowseFile() throws IOException{		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Text file(*.txt)","txt"));
		returnVal = fileChooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
			EncryptionAES.selectedFolderContent = EncryptionAES.readFile(file);
			
			EncryptionAES.selectedFolderLocation = fileChooser.getCurrentDirectory().toString();
			File folderLocation = new File(EncryptionAES.selectedFolderLocation);
			EncryptionAES.selectedFolder = folderLocation.listFiles();
		}
	}
	
	//ovo je sada dobro
	//u byte keyValue se unese SADRZAJ kljuca
	private void BrowseKey() throws IOException{
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Text file(*.txt)","txt"));
		returnVal = fileChooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
			EncryptionAES.readKey(file);
		}
	}
}
