import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


@SuppressWarnings("restriction")

public class EncryptionAES {
	
	public static final String ALGO = "AES";
    public static byte[] keyValue = null;
    public static boolean encrypting = false;
    
    public static File[] selectedFolder;
    public static String selectedFolderLocation = null;
    public static String selectedFolderContent = null;
    
    

    static void readKey (File file) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            keyValue = new byte[(int)file.length()];
            fileInputStream.read(keyValue);
            if (keyValue.length != 16){
            	System.out.println("Invalid key length!");
            	keyValue = null;
            }
            //String s = new String(keyValue);
            //System.out.println("File content: " + s);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
    }   
    
    public static String readFile(File file) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    } 
    
        
    public static void writeFile (File fileName, String encrypted) throws IOException {    	
    	boolean writeToFileSucessful= false;
    	if (encrypting){	 
	        //writing to a file	
   	        try {
   	        	PrintWriter out = new PrintWriter(fileName);//za probu je fileName, inace je actualFile
				out.println(encrypted);
				out.close();
				writeToFileSucessful = true;
			} catch (Exception e) {
				writeToFileSucessful = false;
			}	
   	        
   	        //renaming of the coded file
   	        if (writeToFileSucessful) {
	    		String newName = "Coded-"+fileName.getName();
	    		File destinationFolder = new File (selectedFolderLocation);
	    		File actualFile = new File (destinationFolder, newName);
	            fileName.renameTo(actualFile);
   	        }
	    	
	    	//System.out.println(Paths.get(selectedFolderLocation));
	    	//Files.move(Paths.get(fileName.getPath()), Paths.get(selectedFolderContent));
    	}
    	else{      //not encryption, removing substring and writing
    		String trimmedFileName = fileName.getAbsolutePath(); 		
    		if (trimmedFileName.contains("Coded-")){   			
    			
    			try {
					PrintWriter out = new PrintWriter(fileName);//za probu je fileName, inace je newFileName
					out.println(encrypted);
					out.close();
					writeToFileSucessful = true;
				} catch (Exception e) {
					writeToFileSucessful= false;
				}
    			
    			if(writeToFileSucessful){
    				trimmedFileName = trimmedFileName.replace("Coded-", "");
        			File newFileName = new File (trimmedFileName);
        			fileName.renameTo(newFileName);
    			}
    		}
	   	}
    }
    
  
	public static String encrypt(String Data) throws Exception {
        SecretKey key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());       
		String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }
    
    
	public static String decrypt(String encryptedData) throws Exception {
		SecretKey key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    
    public static SecretKeySpec generateKey() throws Exception {
        SecretKeySpec key = (SecretKeySpec) new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}
