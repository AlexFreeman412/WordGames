package biz.chace.wordgames;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView CountTextView;

    private Button LetterOneButton;
    private Button LetterTwoButton;
    private Button LetterThreeButton;
    private Button LetterFourButton;
    private Button LetterFiveButton;
    private Button LetterSixButton;

    private Button[] LetterButtons;

    private TextView GuessOneTextView;
    private TextView GuessTwoTextView;
    private TextView GuessThreeTextView;

    private TextView[] GuessTextViews;

    private Button DoneButton;

    private char[] lettersGenerated;

    private ArrayList<String> wordList;
    private ArrayList<String> correctGuesses;

    final Context context = this;

    private int numCorrect = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountTextView = findViewById(R.id.countTextView);

        LetterOneButton = findViewById(R.id.letterOne);
        LetterTwoButton = findViewById(R.id.letterTwo);
        LetterThreeButton = findViewById(R.id.letterThree);
        LetterFourButton = findViewById(R.id.letterFour);
        LetterFiveButton = findViewById(R.id.letterFive);
        LetterSixButton = findViewById(R.id.letterSix);

        LetterButtons = new Button[]{LetterOneButton, LetterTwoButton, LetterThreeButton,
                LetterFourButton, LetterFiveButton, LetterSixButton};

        GuessOneTextView = findViewById(R.id.guessOne);
        GuessTwoTextView = findViewById(R.id.guessTwo);
        GuessThreeTextView = findViewById(R.id.guessThree);

        GuessTextViews = new TextView[]{GuessOneTextView, GuessTwoTextView, GuessThreeTextView};

        DoneButton = findViewById(R.id.done);

        correctGuesses = new ArrayList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        StartGame();
    }

    protected void StartGame(){
        numCorrect = 0;
        correctGuesses.clear();
        CountTextView.setText(Integer.toString(numCorrect));

        do{
            lettersGenerated = GenerateRandomLetters();
            wordList = GetWords(lettersGenerated);
        } while (wordList.toArray().length < 2);

        shuffleArray(lettersGenerated);

        for(int i = 0; i < lettersGenerated.length; i++){
            LetterButtons[i].setText(Character.toString(lettersGenerated[i]));

            Button button = LetterButtons[i];
            button.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Button button = (Button)view;
                    SetNextLetter(button.getText().toString());
                    if(AllGuessTextViewsFull()){
                        char[] toSubmit = GetChars();
                        SubmitCharacters(toSubmit);
                        if(AllWordsGuessed()){
                            ShowFinishScreen();
                        }
                    }
                }
            });
        }

        for(int i = 0; i < GuessTextViews.length; i++){
            TextView guessTextView = GuessTextViews[i];
            guessTextView.setText("");
            guessTextView.setOnClickListener(new TextView.OnClickListener() {

                public void onClick(View view) {
                    TextView textView = (TextView)view;
                    textView.setText("");
                }
            });
        }

        DoneButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                ShowFinishScreen();
            }
        });


    }

    protected char[] GetChars(){
        char[] charArray = new char[GuessTextViews.length];
        for(int i = 0; i < GuessTextViews.length; i++){
                charArray[i] = GuessTextViews[i].getText().charAt(0);
        }
        return charArray;
    }

    protected void SetNextLetter(String selectedLetter){
        for(int i = 0; i < GuessTextViews.length; i++){
            if(GuessTextViews[i].getText().equals("")){
                GuessTextViews[i].setText(selectedLetter);
                return;
            }
        }
    }

    protected boolean AllGuessTextViewsFull(){
        boolean allFull = true;
        for(int i = 0; i < GuessTextViews.length; i++){
            if(GuessTextViews[i].getText().equals("")){
                allFull = false;
            }
        }
        return allFull;
    }

    protected boolean AllWordsGuessed(){
        return correctGuesses.size() == wordList.size();
    }

    protected void TestGame(){
        Random rnd = new Random();
        char[] toSubmit = new char[3];
        do{
            for(int i = 0; i < 3; i++){
                toSubmit[i] = lettersGenerated[rnd.nextInt(lettersGenerated.length)];
            }
        } while (!Words.IsThreeLetterWord(CombineCharacters(toSubmit)));

        SubmitCharacters(toSubmit);

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
            int  n;
            do{
                n = rand.nextInt(charArray.length) ;
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

    protected void SubmitCharacters(char[] lettersSubmitted){

        String word = CombineCharacters(lettersSubmitted);

        if(CheckWord(word)){
            numCorrect++;
            CountTextView.setText(Integer.toString(numCorrect));
            correctGuesses.add(word);
        }

        ClearGuessViews();
    }

    protected void ClearGuessViews(){
        for(int i = 0; i < GuessTextViews.length; i++){
            GuessTextViews[i].setText("");
        }
    }

    protected String CombineCharacters(char[] characters){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < characters.length; i++){
            sb.append(characters[i]);
        }
        return sb.toString().toLowerCase();
    }

    protected boolean CheckWord(String word){
        boolean usesRightLetters = true;
        for(char letter: word.toCharArray()){
            if(!contains(lettersGenerated,letter))
                usesRightLetters = false;
        }

        boolean isRealWord = contains(wordList.toArray(),word);

        boolean alreadyGuessed = correctGuesses.contains(word);

        return usesRightLetters & isRealWord & !alreadyGuessed;

    }

    protected void ShowFinishScreen(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Finished");

        LayoutInflater inflater= LayoutInflater.from(this);
        View wordListView=inflater.inflate(R.layout.word_list, null);

        TextView wordListTextView = wordListView.findViewById(R.id.wordlisttextview);
        wordListTextView.setText(Html.fromHtml(ListWords(),Html.FROM_HTML_MODE_LEGACY));

        // set dialog message
        alertDialogBuilder
                .setMessage(Html.fromHtml("<br>Congratulations you correctly guessed " + numCorrect + " out of the total "
                        + wordList.toArray().length + " words! You got a score of " + CalcScoreAsPercentage() + "%!<br>"
                        //+ wordListString
                        , Html.FROM_HTML_MODE_LEGACY))
                .setView(wordListView)
                .setCancelable(false)

                .setNeutralButton("Restart",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, restart
                        // the game
                        StartGame();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        alertDialog.getWindow().setLayout(1000, 1200);
    }

    protected float CalcScoreAsPercentage(){
        float score = (( (float)numCorrect / (float)wordList.toArray().length) * 100);
        return round(score, 2);
    }

    protected String ListWords(){
        StringBuilder sb = new StringBuilder();
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Word List: </b><br>&nbsp;&nbsp;&nbsp;");

        int count = 1;
        for(String word: wordList){
            if(correctGuesses != null && !correctGuesses.isEmpty()){
                if(contains(correctGuesses.toArray(), word)){
                    sb.append("&nbsp;&nbsp;<strike>" + word + "</strike>");
                    if(count == 7) {
                        sb.append("<br>&nbsp;&nbsp;&nbsp;");
                        count = 0;
                    } else {
                        count++;
                    }
                    continue;
                }
            }
            sb.append("&nbsp;&nbsp;" + word + "");
            if(count == 7) {
                sb.append("<br>&nbsp;&nbsp;&nbsp;");
                count = 0;
            } else {
                count++;
            }
        }
        return sb.toString();
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
