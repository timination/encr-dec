/*
	Author: Deron Eriksson
	Taken From: http://www.avajava.com/tutorials/lessons/how-do-i-encrypt-and-decrypt-files-using-des.html
*/

package TCM;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class TheCookieMachine {


	public static void main(String[] args) {
		try {

		String key 		= "";
		String inFilePath 	= "";
		String outFilePath 	= "";
		int action 		= -1;


			if(args.length == 4)
			{
				if(args[0].equalsIgnoreCase("-e"))
				{
					action = 0;
				}
				else if(args[0].equalsIgnoreCase("-d"))
				{
					action = 1;
				}
				else
				{
					printUsage();
					System.exit(0);
				}

				inFilePath 	= args[1];
				outFilePath 	= args[2];
				key		= args[3];
			}
			else 
			{
				printUsage();
				System.exit(0);
			}

			switch(action)
			{
				case 0: //enc
					FileInputStream fis = new FileInputStream(inFilePath);
					FileOutputStream fos = new FileOutputStream(outFilePath);
					encrypt(key, fis, fos);
				break;

				case 1: //decr
					FileInputStream fis2 = new FileInputStream(inFilePath);
					FileOutputStream fos2 = new FileOutputStream(outFilePath);
					decrypt(key, fis2, fos2);
				break;
	
				default:
					printUsage();
					System.exit(0);
				break;
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void printUsage()
	{
		System.out.println("Usage: TheCookieMachine -e <file_to_encrypt> <encrypted_file_destination> <encryption_key>");
		System.out.println("Usage: TheCookieMachine -d <file_to_decrypt> <decrypted_file_destination> <encryption_key>");
	}

	public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}

	public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}

	public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}

	public static void doCopy(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
	}

}
