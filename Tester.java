import java.util.*;
import edu.duke.*;
/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tester {
    public void testCaesarCipher(){
        /*CaesarCipher cc = new CaesarCipher(1);
        char testChar = 'a';
        char encryptedLetter = cc.encryptLetter(testChar); 
        System.out.println(encryptedLetter);
        System.out.println(cc.decryptLetter(encryptedLetter));*/
        
        
        FileResource fr = new FileResource();
        String message = fr.asString();
        CaesarCracker cracker = new CaesarCracker('a');
        int key = cracker.getKey(message);
        System.out.println(key);
        CaesarCipher cc = new CaesarCipher(key);
        StringBuilder messageString = new StringBuilder(message);
        StringBuilder decryptedMsg = new StringBuilder("");
        for(int i=0 ; i < messageString.length() ; i++){
            char currChar = messageString.charAt(i);
            char decryptedChar = cc.decryptLetter(currChar);
            decryptedMsg.append(decryptedChar);
        }
        System.out.println(decryptedMsg.toString());
    }
    
    public void testVigenereCipher(){
        FileResource fr = new FileResource();
        String message = fr.asString();
        int []keys = {17,14,12,4};
        VigenereCipher vc = new VigenereCipher(keys);
        String encryptedMsg = vc.encrypt(message);
        System.out.println(encryptedMsg);
        System.out.println(vc.decrypt(encryptedMsg));   
    }
    
    public void testVigenereBreaker(){
        VigenereBreaker vb = new VigenereBreaker();
        vb.breakVigenere();
        /*Scanner sc = new Scanner(System.in);
        VigenereBreaker vb = new VigenereBreaker();
        System.out.println("Enter message : ");
        String msg = sc.nextLine();
        System.out.println("Enter whichSlice : ");
        int whichSlice = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter totalSlice : ");
        int totalSlice = sc.nextInt();
        sc.nextLine();
        System.out.println(vb.sliceString(msg,whichSlice,totalSlice));*/
    }
}
