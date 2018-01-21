package biz.chace.wordgames;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //private String wordList[] = {"aah","aha","ate","awe","aye","eat","eta","ewe","eye","hat","haw","hay","het","hew","hey","taw","tea","tee","tet","the","thy","waw","way","wee","wet","why","wye","yah","yaw","yay","yea","yeh","yet","yew"};
    private TextView CountTextView;
    private TextView LetterDisplay;

    private char[] lettersGenerated;
    private char[] lettersSubmitted;

    private ArrayList<String> wordList;
    private ArrayList<String> correctGuesses;

    final Context context = this;

    private int numCorrect = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CountTextView = findViewById(R.id.CountTextView);
        LetterDisplay = findViewById(R.id.LetterDisplay);

        StartGame();
    }

    protected void StartGame(){
        numCorrect = 0;
        CountTextView.setText(Integer.toString(numCorrect));

        lettersGenerated = GenerateRandomLetters();
        wordList = GetWords(lettersGenerated);

        shuffleArray(lettersGenerated);
        String letters = new String(lettersGenerated);
        LetterDisplay.setText(letters);

        TestGame();
    }

    protected void TestGame(){
        Random rnd = new Random();
        char[] toSubmit = new char[3];
        do{
            for(int i = 0; i < 3; i++){
                toSubmit[i] = lettersGenerated[rnd.nextInt(lettersGenerated.length)];
            }
        } while (!Words.IsThreeLetterWord(CombineCharacters(toSubmit[0],toSubmit[1],toSubmit[2])));

        //SubmitCharacters('e', 'y', 'e');
        SubmitCharacters(toSubmit[0],toSubmit[1],toSubmit[2]);

        ShowFinishScreen();
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

    protected ArrayList<String> GetWords(char[] letters){
        ArrayList<String> validWords = new ArrayList<String>();
        for(String word: Words.threeLetterWords){
            boolean isValidWord = true;
            for(char character: word.toCharArray()){
                if(!contains(letters,character)){
                    isValidWord = false;
                    break;
                }
            }
            if(isValidWord) validWords.add(word);
        }
        return validWords;
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

    /*
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
    */

    protected void SubmitCharacters(char one, char two, char three){

        lettersSubmitted = new char[]{one, two, three};

        String word = CombineCharacters(one, two, three);

        if(CheckWord(word))
            CountTextView.setText(Integer.toString(numCorrect++));
    }

    protected String CombineCharacters(char one, char two, char three){
        StringBuilder sb = new StringBuilder();
        sb.append(one);
        sb.append(two);
        sb.append(three);
        return sb.toString().toLowerCase();
    }

    /*
    protected boolean CheckWord(String word){
        return contains(wordList,word);
    }
    */

    protected boolean CheckWord(String word){
        boolean usesRightLetters = true;
        for(char letter: word.toCharArray()){
            if(!contains(lettersGenerated,letter))
                usesRightLetters = false;
        }

        //boolean isRealWord = Words.IsThreeLetterWord(word);
        boolean isRealWord = contains(wordList.toArray(),word);

        return usesRightLetters & isRealWord;

    }

    protected void ShowFinishScreen(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Finished");

        // set dialog message
        alertDialogBuilder
                .setMessage("Congratulations you correctly guessed " + numCorrect + " out of the total "
                + wordList.toArray().length + " words! You got a score of " + CalcScoreAsPercentage() + "%!")
                .setCancelable(false)
                .setNeutralButton("Restart",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        StartGame();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    protected float CalcScoreAsPercentage(){
        float score = (( (float)numCorrect / (float)wordList.toArray().length) * 100);
        return round(score, 2);
    }

    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }

    public boolean contains(final char[] array, final char key) {
        for (final char i : array) {
            if (i == key) {
                return true;
            }
        }
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

    public static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }
}
