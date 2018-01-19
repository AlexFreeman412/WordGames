package biz.chace.wordgames;

import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String wordList[] = {"aah","aha","ate","awe","aye","eat","eta","ewe","eye","hat","haw","hay","het","hew","hey","taw","tea","tee","tet","the","thy","waw","way","wee","wet","why","wye","yah","yaw","yay","yea","yeh","yet","yew"};
    private TextView CountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CountTextView = (TextView)findViewById(R.id.CountTextView);
        SubmitCharacters('e', 'y', 'e');
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
}
