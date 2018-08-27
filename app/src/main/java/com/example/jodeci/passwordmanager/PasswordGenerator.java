package com.example.jodeci.passwordmanager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by jodeci on 8/21/2018.
 */

public class PasswordGenerator {

    private int minNumberofChars;
    private int numberofCaps;
    private int numberofLower;
    private int numberofSymbols;
    private int numberofNumbers;
    private String includePhrase;

    private final int DEFAULT_CHARS = 8;
    private final int DEFAULT_CAPITALS = 2;
    private final int DEFAULT_LOWER = 2;
    private final int DEFAULT_SYMBOLS = 2;
    private final int DEFAULT_NUMBERS = 2;
    private final int MIN_PASS_LENGTH = 4;

    private final String[] LOWER = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private final String[] CAPITALS = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private final String[] SYMBOLS = {"!","@","#","$","%","^","&","*","+"};
    private final String[] NUMBERS = {"0","1","2","3","4","5","6","7","8","9"};

    public PasswordGenerator(int chars, int caps, int lower, int symbols, int numbers, String phrase){

        if (chars != -1){
            this.minNumberofChars = chars;
        } else {
            this.minNumberofChars = DEFAULT_CHARS;
        }

        if (caps != -1){
            this.numberofCaps = caps;
        } else {
            this.numberofCaps = DEFAULT_CAPITALS;
        }

        if (lower != -1){
            this.numberofLower = lower;
        } else {
            this.numberofLower = DEFAULT_LOWER;
        }

        if (symbols != -1){
            this.numberofSymbols = symbols;
        } else {
            this.numberofSymbols = DEFAULT_SYMBOLS;
        }

        if (numbers != -1){
            this.numberofNumbers = numbers;
        } else {
            this.numberofNumbers = DEFAULT_NUMBERS;
        }

        this.includePhrase = phrase.trim();
    }

    public String getRandomPassword(){
        int passwordLen;

        //calculate the lenght of the final string
        int total = numberofCaps + numberofLower + numberofSymbols + numberofNumbers;
        if (total > minNumberofChars){
            passwordLen = total;
        } else {
            passwordLen = minNumberofChars;
        }

        //minimum length check
        if(passwordLen < MIN_PASS_LENGTH){
            passwordLen = DEFAULT_CHARS;
        }

        //Allocate Extra chars
        if (passwordLen > total){
            int extra = passwordLen - total;
            while (extra > 0){
                if(extra > 0){
                    numberofLower++;
                    extra--;
                }
                if(extra > 0){
                    numberofCaps++;
                    extra--;
                }
                if(extra > 0){
                    numberofSymbols++;
                    extra--;
                }
                if(extra > 0){
                    numberofNumbers++;
                    extra--;
                }
            }
        }

        //Phrase checker
        if(includePhrase.length() > 0){
            passwordLen++;
        }

        //Allocate random items to "part" array
        String[] part = new String[passwordLen];
        Random random = new Random();
        int partPointer = 0;
        int randomListPointer;

        if (numberofCaps > 0){
            for (int i=0;i<numberofCaps;i++){
                randomListPointer = random.nextInt(CAPITALS.length);
                part[partPointer] = CAPITALS[randomListPointer];
                partPointer++;
            }
        }

        if (numberofLower > 0){
            for (int i=0;i<numberofLower;i++){
                randomListPointer = random.nextInt(LOWER.length);
                part[partPointer] = LOWER[randomListPointer];
                partPointer++;
            }
        }

        if (numberofSymbols > 0){
            for (int i=0;i<numberofSymbols;i++){
                randomListPointer = random.nextInt(SYMBOLS.length);
                part[partPointer] = SYMBOLS[randomListPointer];
                partPointer++;
            }
        }

        if (numberofNumbers > 0){
            for (int i=0;i<numberofNumbers;i++){
                randomListPointer = random.nextInt(NUMBERS.length);
                part[partPointer] = NUMBERS[randomListPointer];
                partPointer++;
            }
        }

        if(includePhrase.length() > 0){
            part[partPointer] = includePhrase;
        }

        //construct result
        String result = "";
        int index;
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(part));
        for (int i=0;i<passwordLen;i++){
            index = random.nextInt(arrayList.size());
            result = result + arrayList.get(index);
            arrayList.remove(index);
        }

        return result;

    }

}
