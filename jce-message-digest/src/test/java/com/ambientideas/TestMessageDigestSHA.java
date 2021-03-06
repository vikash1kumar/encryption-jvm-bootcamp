package com.ambientideas;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * Digest a string message via SHA1.
 * 
 * Demonstrate that very similar messages
 * have radically different hashes.
 *
 * Digest algorithms provided by the JRE are listed here:
 * http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#MessageDigest
 */
public class TestMessageDigestSHA 
{
  public static void main( String[] args )
    throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    //Set up the message to be encoded
    final String message1 = "Four score and seven years ago";
    final String message2 = "Four score and seven tears ago";
	  
    
    System.out.println("Message1 SHA1 digest: "
      + shaAndBase64Encode(message1));
    System.out.println("Message2 SHA1 digest: "
      + shaAndBase64Encode(message2));
    System.out.println("\r\n");
    
    //Original account creation
    Login loginSavedToDB = shaAndRandomSaltNewAccount("12345");
    System.out.println("Hash: "+ loginSavedToDB.hash);
    System.out.println("Salt: " + loginSavedToDB.salt);
    
    //Attempt to re-login
    System.out.println("Rehashed: " + shaWithKnownSalt("12345", loginSavedToDB.salt));
  }
  
  /**
   * Helper function to both SHA-1 hash and
   * base64 encode the resulting bytes to a String 
   * @throws UnsupportedEncodingException 
   */
  public static String shaAndBase64Encode(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    
    byte[] digest = sha.digest(message.getBytes("UTF8"));
    return Base64.encodeBase64String(digest);
  }
  
  /**
   * Derivative of the sha encoding function that allows for a known salt to
   * be used in combination with the password
   * @param message
   * @param salt
   * @return
   * @throws NoSuchAlgorithmException
   * @throws UnsupportedEncodingException 
   */
  public static String shaWithKnownSalt(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    return shaAndBase64Encode(password + salt);
  }
  
  public static Login shaAndRandomSaltNewAccount(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    Login acct = new Login();
    
    acct.salt = new String("" + new java.util.Random().nextInt());
    acct.hash = shaWithKnownSalt(password, acct.salt);
    
    return acct;
  }
  
  private static class Login {
    public String hash;
    public String salt;
  }
}
