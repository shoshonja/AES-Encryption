import java.awt.EventQueue;


public class EncryptionMain {

	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowBuilderDisplay window = new WindowBuilderDisplay();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//end of window builder
    	    	
    	//EncryptionAES.readKey("key.txt");
		
    	//EncryptionAES.fileValue a nesto za read file...
		/*
    	String password = new String (EncryptionAES.fileValue);
        String passwordEnc = EncryptionAES.encrypt(password);
        String passwordDec = EncryptionAES.decrypt(passwordEnc);

        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);
        */
        
        //FileHandling.writeFile("text to code.txt", passwordEnc);
        //FileHandling.writeFile("text to code.txt", passwordDec);
        
        //FileHandling.readFolder();
    }

}
