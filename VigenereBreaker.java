import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    FileResource fr;
    public VigenereBreaker(){
         fr = new FileResource();
    }
    
    
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder slice = new StringBuilder();
        StringBuilder currMsg = new StringBuilder(message);
        for(int i=0; i < message.length() ; i++){
            int sliceIndex = i % totalSlices ; //getting the slice number
            if(sliceIndex == whichSlice){
                slice.append(currMsg.charAt(i));
            }
        }
        return slice.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        ArrayList<String> slices = new ArrayList<String>();
        for(int i = 0 ; i < klength ; i++){
            String currSlice = sliceString(encrypted,i,klength);
            slices.add(currSlice);
        }
        for(int i=0; i < slices.size(); i++){
            CaesarCracker cc  = new CaesarCracker(mostCommon);
            key[i] = cc.getKey(slices.get(i));
        }
        return key;
    }
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> dictionary = new HashSet<String>();
        for(String word : fr.words()){
            word = word.toLowerCase();
            dictionary.add(word);
        }
        return dictionary;
    }
    
    public int countWords(HashSet<String> dictionary, String message){
        int realEnglishWords = 0 ;
        for(String word : message.split("\\W+")){
            word = word.toLowerCase();
            if(dictionary.contains(word)){
                realEnglishWords+=1;
            }
        }
        return realEnglishWords;
    }
    
    public ArrayList<Object> breakForLanguage(String encrypted, HashSet dictionary,char commonChar){
        int kLength = 1;
        int maximumEnglishWords = 0;
        int originalKLength = 1;
        String finalDecryption = "NULL";
        ArrayList<Object> allDetail = new ArrayList<Object>();
        while(kLength < 100){
            int []keys = tryKeyLength(encrypted,kLength,commonChar);
            VigenereCipher vc = new VigenereCipher(keys);
            String decryptedMsg = vc.decrypt(encrypted);
            int realEnglishWords = countWords(dictionary,decryptedMsg);
            if(realEnglishWords > maximumEnglishWords){
                maximumEnglishWords = realEnglishWords;
                finalDecryption = decryptedMsg;
                originalKLength = kLength;
            }
            kLength++;
        }
        int []originalKeys = tryKeyLength(encrypted,originalKLength,commonChar);
        /*
        System.out.println("Total English Words : " + maximumEnglishWords);
        System.out.println("Total keys : " + originalKeys.length);
        or(int i= 0; i < originalKeys.length ; i++){
            System.out.println(originalKeys[i]);
        }
        */
        allDetail.add(originalKeys);
        allDetail.add(maximumEnglishWords);
        allDetail.add(originalKeys.length);
        allDetail.add(finalDecryption);
        
        return allDetail;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        HashMap<Character,Integer> count = new HashMap<Character,Integer>();
        int maximumCount = 0;
        char mostFreqChar = ' ';
        for(String word : dictionary){
            for(char currChar : word.toCharArray()){
                if(!count.containsKey(currChar)){
                    count.put(currChar,1);
                }
                else{
                    count.put(currChar, count.get(currChar)+1);
                }
            }
        }
        for(char currChar : count.keySet()){
            int value = count.get(currChar);
            if(value > maximumCount ){
                maximumCount = value;
                mostFreqChar = currChar;
            }
        }
        
        return mostFreqChar;
    }
    
    public String breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> dictionaryMap){
        int maxRealWords = 0;
        String decryptedMsg = "Null";
        int numberOfKeys = 0;
        String originalLanguage = " ";
        for(String language : dictionaryMap.keySet()){
            HashSet<String> dictionary = dictionaryMap.get(language);
            char commonChar = mostCommonCharIn(dictionary);
            ArrayList<Object> allDetails = breakForLanguage(encrypted,dictionary,commonChar);
            int currMaxRealWords = Integer.valueOf(allDetails.get(1).toString());
            if(currMaxRealWords > maxRealWords){
                maxRealWords = currMaxRealWords;
                numberOfKeys = Integer.valueOf(allDetails.get(2).toString());
                decryptedMsg = allDetails.get(3).toString();
                originalLanguage = language;
            }
        }
        System.out.println("Number Of RealWords : " + maxRealWords);
        System.out.println("Number of keys : " + numberOfKeys);
        System.out.println("original Language : " + originalLanguage);
        return decryptedMsg;
    }
    
    public void breakVigenere() {
        String message = fr.asString();
        Scanner sc= new Scanner(System.in);
        String []fileList = {"Danish","Dutch","English","French","German","Italian",
                            "Portuguese","Spanish"};
        HashMap<String,HashSet<String>> dictionaryMap = new HashMap<String,HashSet<String>>();
        for(String lang : fileList){
            FileResource dictionaryFile = new FileResource("dictionaries/" + lang);
            HashSet<String> dictionary = readDictionary(dictionaryFile);
            dictionaryMap.put(lang,dictionary);
        }
        
        System.out.println(breakForAllLangs(message,dictionaryMap));
        
        
        
        /*
        System.out.println("Enter Key Size : ");
        int kLength = sc.nextInt();
        sc.nextLine();
        int []keys = tryKeyLength(message,kLength,'e');
        VigenereCipher vc = new VigenereCipher(keys);
        System.out.println("Keys used are mentioned below : ");
        for(int i=0; i < keys.length ; i++){
            System.out.println(keys[i]);
        }
        System.out.println(vc.decrypt(message));
        */
       
       
       
    }
    
}
