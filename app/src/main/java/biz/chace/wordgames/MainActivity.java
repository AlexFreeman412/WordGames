package biz.chace.wordgames;

import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private String wordList[] = {"aah","aha","ate","awe","aye","eat","eta","ewe","eye","hat","haw","hay","het","hew","hey","taw","tea","tee","tet","the","thy","waw","way","wee","wet","why","wye","yah","yaw","yay","yea","yeh","yet","yew"};
    private TextView CountTextView;
    private TextView LetterDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CountTextView = (TextView)findViewById(R.id.CountTextView);
        LetterDisplay = (TextView)findViewById(R.id.LetterDisplay);

        SubmitCharacters('e', 'y', 'e');
        char[] characters = GenerateRandomLetters();
        shuffleArray(characters);
        String letters = new String(characters);
        LetterDisplay.setText(letters);
    }

    protected char[] GenerateRandomLetters(){

        char[] vowels = GetVowels();
        char[] consonants = GetConsonants();

        StringBuilder sb = new StringBuilder(64);
        sb.append(vowels);
        sb.append(consonants);

        char[] allLetters = sb.toString().toCharArray();

        return allLetters;
    }

    protected char[] GetVowels(){
        char[] allVowels = new char[]{'a','e','i','o','u','y'};
        return GetCharsFromArray(allVowels,2);
    }

    protected char[] GetConsonants(){
        char[] allConsonants = new char[]{'b','c','d','f','g','h','j','k','l','m'
                ,'n','p','q','r','s','t','v','w','x','z'};
        return GetCharsFromArray(allConsonants,4);
    }

    protected char[] GetCharsFromArray(char[] charArray, int number){

        char[] charsToReturn = new char[number];
        Random rand = new Random();
        char aRandomChar;

        for(int i = 0; i < number; i++){
            int  n = rand.nextInt(charArray.length) ;
            do{
                aRandomChar = charArray[n];
            } while (CheckIfCharIsInArray(aRandomChar,charsToReturn));

            charsToReturn[i] = aRandomChar;
        }

        return charsToReturn;
    }

    protected boolean CheckIfCharIsInArray(char character, char[] charArray){
        for (char c : charArray) {
            if (c == character) {
                return true;
            }
        }
        return false;
    }

    protected char[] RandomiseArray(char[] characters){
        Collections.shuffle(Arrays.asList(characters));
        return characters;
    }

    protected void SubmitCharacters(char one, char two, char three){

        StringBuilder sb = new StringBuilder();
        sb.append(one);
        sb.append(two);
        sb.append(three);
        String word = sb.toString().toLowerCase();

        if(CheckWord(word)){
            int currentCount = Integer.parseInt(CountTextView.getText().toString());
            CountTextView.setText(Integer.toString(currentCount+1));
        }
    }

    protected boolean CheckWord(String word){
        return contains(wordList,word);
    }

    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }

    static void shuffleArray(char[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();//ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            char a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
