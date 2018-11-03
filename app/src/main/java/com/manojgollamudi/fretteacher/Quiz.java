package com.manojgollamudi.fretteacher;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import static com.manojgollamudi.fretteacher.GameList.transitionToast;


public class Quiz extends AppCompatActivity {
    //boolean to keep track of submit/next button. False = button named submit, true = named next.
    boolean submit_next = false;

    //boolean to keep track of state
    boolean state = false;

    //booleans to keep track of user preferences: sound and lefty mode
    boolean sound_bool;
    boolean lefty_bool;

    //keep track of previous indicator position
    float x_prev = 0;
    float y_prev = 0;


    //to keep track of score
    int score = 0;
    int total = 0;
    int count = 0;

    //how many frets are showing?
    int frets_shown = 3;

    //for use keeping track of which frets are being used
    int fretnos = 0;

    //variable to store user input, to be compared with fragment tags
    String note_input = "NONE";
    String textview_text = "";

    //map of string notes, used to associate equivalent notes
    Map <String, String> noteMap = new HashMap<>(24);

    boolean DoNotShowAgain;

    //map of index - note notes, used to index notes
    Map <Integer, String> indexMap = new HashMap<>(12);

    //map of name(string)-song(int) notes, used to play audio for notes
    Vector<MediaPlayer> mp_vector = new Vector<>();

    //vector of name_coordinate objects
    ArrayList<note_info> notes = new ArrayList<>();

    //strings to put in notes
    String string_e = "e";
    String string_f = "f";
    String string_fs = "fs";
    String string_g = "g";
    String string_gs = "gs";
    String string_a = "a";
    String string_as = "as";
    String string_b = "b";
    String string_c = "c";
    String string_cs = "cs";
    String string_d = "d";
    String string_ds = "ds";

    String Saved_Data = "Saved_Data";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        transitionToast.cancel();


        //Setup toolbar

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        notes.clear();

        //indicator set as invisibile for ease of development
        ImageView imgview = (ImageView)findViewById(R.id.indicator);
        imgview.setVisibility(View.VISIBLE);


        if(savedInstanceState != null){
            state = true;
        }

        final TextView textview = (TextView)findViewById(R.id.input_text);

        //assign image variables
        Intent game_intent = getIntent();
        lefty_bool = game_intent.getBooleanExtra("lefty", false);

        //populate noteMap
        //some notes technically do not exist, but must be put in to prevent crashing
        noteMap.put("as", "bf");
        noteMap.put("af", "gs");
        noteMap.put("bs", "bs");
        noteMap.put("bf", "as");
        noteMap.put("cs", "df");
        noteMap.put("cf", "cf");
        noteMap.put("ds", "ef");
        noteMap.put("df", "cs");
        noteMap.put("es", "es");
        noteMap.put("ef", "ds");
        noteMap.put("fs", "gf");
        noteMap.put("ff", "ff");
        noteMap.put("gs", "af");
        noteMap.put("gf", "fs");

        //populate indexMap
        indexMap.put(0, "e");
        indexMap.put(1, "f");
        indexMap.put(2, "fs");
        indexMap.put(3, "g");
        indexMap.put(4, "gs");
        indexMap.put(5, "a");
        indexMap.put(6, "as");
        indexMap.put(7, "b");
        indexMap.put(8, "c");
        indexMap.put(9, "cs");
        indexMap.put(10, "d");
        indexMap.put(11, "ds");

        //set up instructions
        SharedPreferences sharedpreferences = getSharedPreferences(Saved_Data, Context.MODE_PRIVATE);
        DoNotShowAgain = sharedpreferences.getBoolean("QuizDoNotShowAgain", false);

        if(!DoNotShowAgain){
            final Dialog dialog = new Dialog(Quiz.this);
            dialog.setContentView(R.layout.quiz_instructions_layout);
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.show();
            Button declineButton = (Button) dialog.findViewById(R.id.gotit_quiz);
            declineButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        //setting up game based on which frets user chose
        fretnos = game_intent.getIntExtra("fretnos", -1);
        sound_bool = game_intent.getBooleanExtra("sound", false);
        lefty_bool = game_intent.getBooleanExtra("lefty", false);

        //set overview image
        ImageView overview = (ImageView) findViewById(R.id.overview);
        RelativeLayout fretboard = (RelativeLayout) findViewById(R.id.indicator_layout);

        if(fretnos == 3){
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview34_l);
            }
            else{
                overview.setImageResource(R.drawable.overview34);
            }
        }
        if(fretnos == 5){
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview56_l);
            }
            else{
                overview.setImageResource(R.drawable.overview56);
            }
        }
        if(fretnos == 7){
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview78_l);
            }
            else{
                overview.setImageResource(R.drawable.overview78);
            }
        }
        if(fretnos == 9){
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview911_l);
            }
            else{
                overview.setImageResource(R.drawable.overview911);
            }
        }

        //set fret number indicator
        TextView number_indicator = (TextView)findViewById(R.id.fretnumber);
        String fret_number = "" + fretnos;
        number_indicator.setText(fret_number);
        Resources res = getResources();


        //move fret number indicator if lefty mode
        if(lefty_bool){
            //convert 250 dp to px
            float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, res.getDisplayMetrics());
            int margin_int = (int) margin;
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) number_indicator
                    .getLayoutParams();

            mlp.setMargins(margin_int, 0, 0, 0);

        }

        //if activity is being restarted, recover previous state
        if(savedInstanceState != null){
            repopulate(savedInstanceState);
        }
        //if not, populate notes vector and handle media
        else{
            String note = indexMap.get(fretnos);
            int notecounter = fretnos;
            int fretnumber = fretnos;
            MediaPlayer mp;
            //loop through six strings
            for(int j = 5; j >= 0; --j){
                //if lefty mode is not on
                if(!lefty_bool) {
                    //loop through number of frets shown, going from left to right
                    for (int i = 0; i <= frets_shown - 1; ++i) {
                        //50 IS AN ARBITRARY NUMBER; ADJUST ACCORDINGLY
                        //each fret length is 100 dp
                        float x_pos = (100 * i);
                        float y_pos = 0;
                        //distance between strings depends on string width -
                        if(j == 0){
                            y_pos = 43;
                        }
                        if(j == 1){
                            y_pos = 71;
                        }
                        if(j == 2){
                            y_pos = 97;
                        }
                        if(j == 3){
                            y_pos = 124;
                        }
                        if(j == 4){
                            y_pos = 153;
                        }
                        if(j == 5){
                            y_pos = 180;
                        }
                        //convert to sp
                        x_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x_pos, res.getDisplayMetrics());
                        y_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y_pos, res.getDisplayMetrics());

                        StringBuilder str = new StringBuilder(indexMap.get(notecounter % 12));
                        str.append(fretnumber);
                        //double fretnumber for high e string, naming convention
                        if(j == 0){
                            str.append(fretnumber);
                        }
                        Integer resId = res.getIdentifier(str.toString(), "raw", this.getPackageName());
                        mp = MediaPlayer.create(this, resId);
                        note_info note_temp = new note_info(x_pos, y_pos, str.toString(), mp, 0);
                        notes.add(note_temp);
                        ++notecounter;
                        ++fretnumber;
                    }
                }
                if(lefty_bool){
                    //loop through number of frets shown, going from right to left
                    for (int i = frets_shown - 1; i > -1; --i) {
                        //make coordinates for indicator based on note location on fretboard
                        float x_pos = (100 * i);
                        float y_pos = 0;
                        //distance between strings depends on string width -
                        if(j == 0){
                            y_pos = 43;
                        }
                        if(j == 1){
                            y_pos = 71;
                        }
                        if(j == 2){
                            y_pos = 97;
                        }
                        if(j == 3){
                            y_pos = 124;
                        }
                        if(j == 4){
                            y_pos = 153;
                        }
                        if(j == 5){
                            y_pos = 180;
                        }
                        //convert to sp
                        x_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x_pos, res.getDisplayMetrics());
                        y_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y_pos, res.getDisplayMetrics());

                        StringBuilder str = new StringBuilder(indexMap.get(notecounter % 12));
                        str.append(fretnumber);
                        //double fretnumber for high e string, naming convention
                        if(j == 0){
                            str.append(fretnumber);
                        }
                        Integer resId = res.getIdentifier(str.toString(), "raw", this.getPackageName());
                        mp = MediaPlayer.create(this, resId);
                        note_info note_temp = new note_info(x_pos, y_pos, str.toString(), mp, 0);
                        notes.add(note_temp);
                        ++notecounter;
                        ++fretnumber;
                    }
                }
                //go back to original string note
                notecounter = notecounter - frets_shown;
                //guitar strings separated by 5 half steps, except for G and B
                if(j == 2){
                    notecounter = notecounter + 4;
                }
                else{
                    notecounter = notecounter + 5;
                }
                //reset fretnumber
                fretnumber = fretnos;
            }

            //shuffle notes
            if(savedInstanceState == null) {
                Collections.shuffle(notes, new Random(System.nanoTime()));
                total = notes.size();
            }


            ImageView indicator = (ImageView)findViewById(R.id.indicator);
            ObjectAnimator xAnim = ObjectAnimator.ofFloat(indicator, "translationX", x_prev , notes.get(0).get_x());
            ObjectAnimator yAnim = ObjectAnimator.ofFloat(indicator, "translationY", y_prev, notes.get(0).get_y());
            xAnim.start();
            yAnim.start();


            //play first note's sound
            if(!state && sound_bool){
                notes.get(0).get_mp().start();
            }

            Button submit_next_btn = (Button) findViewById(R.id.next_button);
            submit_next_btn.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Button next_submit_view = (Button)findViewById(R.id.next_button);

                    //work as the submit button
                    if(!submit_next){
                        submit_next = true;
                        next_submit_view.setText("Next");
                        check_value(notes.get(0));
                    }
                    //work as next button
                    else {
                        submit_next = false;
                        //update prev_x and prev_y positions
                        x_prev = notes.get(0).get_x();
                        y_prev = notes.get(0).get_y();
                        //remove previous note from notes
                        notes.remove(0);
                        //if done, clean up and go to end screen
                        if(notes.size() == 0){
                            //release mediaplayers
                            if(sound_bool) {
                                for(int i = 0; i < mp_vector.size(); ++i){
                                    mp_vector.get(i).release();
                                }
                            }

                            //go to end screen
                            Chronometer timer = (Chronometer)findViewById(R.id.timer);
                            timer.stop();
                            String time = timer.getText().toString();

                            Intent end_intent = new Intent(v.getContext(), End.class);
                            end_intent.putExtra("score_value", score);
                            end_intent.putExtra("total_value", total);
                            end_intent.putExtra("lefty_bool", lefty_bool);
                            end_intent.putExtra("sound_bool", sound_bool);
                            end_intent.putExtra("fretnos", fretnos);
                            startActivity(end_intent);
                            finish();

                            //next.setText("Return to Main Menu");
                        }
                        //if not done, continue
                        else {
                            next_submit_view.setText("Submit");
                            next(savedInstanceState);
                        }
                    }
                }
            });
        }
        Chronometer timer = (Chronometer)findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }



    //if the A button is clicked, set textview_text to A and change textview display
    public void setA(View v) {
        textview_text = "A";
        note_input = "a";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("A");
    }

    //if the B button is clicked, set textview_text to B and change textview display
    public void setB(View v) {

        textview_text = "B";
        note_input = "b";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("B");
    }

    //if the C button is clicked, set textview_text to C and change textview display
    public void setC(View v) {

        textview_text = "C";
        note_input = "c";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("C");
    }

    //if the D button is clicked, set textview_text to D and change textview display
    public void setD(View v) {

        textview_text = "D";
        note_input = "d";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("D");
    }

    //if the E button is clicked, set textview_text to E and change textview display
    public void setE(View v) {

        textview_text = "E";
        note_input = "e";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("E");
    }

    //if the F button is clicked, set textview_text to F and change textview display
    public void setF(View v) {

        textview_text = "F";
        note_input = "f";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("F");
    }

    //if the G button is clicked, set textview_text to G and change textview display
    public void setG(View v) {

        textview_text = "G";
        note_input = "g";
        TextView textview = (TextView)findViewById(R.id.input_text);
        textview.setText("G");
    }

    //if the sharp button is clicked, add a sharp to textview_text and change display
    public void addSharp(View v){
        if(textview_text.length() == 1) {
            textview_text = textview_text + "♯";
            note_input = note_input + "s";
            TextView textview = (TextView) findViewById(R.id.input_text);
            textview.setText(textview_text);
        }
    }

    public void addFlat(View v){
        if(textview_text.length() == 1) {
            textview_text = textview_text + "♭";
            note_input = note_input + "f";
            TextView textview = (TextView) findViewById(R.id.input_text);
            textview.setText(textview_text);
        }
    }

    public void backspace(View v){
        if(textview_text.length() > 0) {
            textview_text = textview_text.substring(0, textview_text.length() - 1);
            note_input = note_input.substring(0, note_input.length() - 1);
            TextView textview = (TextView)findViewById(R.id.input_text);
            textview.setText(textview_text);
        }
    }

    //check what the user inputted against actual value, called when SUBMIT is clicked
    public void check_value(note_info note){
        //make sure there is a valid input

            //get name of note, get rid of fret number at end
            String correct_value = note.get_name();
            correct_value = correct_value.replaceAll("[0-9]", "");
            TextView textview = (TextView)findViewById(R.id.input_text);
            //if no answer is submitted
            if(note_input.equals("NONE") || note_input.equals(" ")){
                textview.setBackgroundResource(R.drawable.border_tan_wrong);
                //put up correct answer
                convertString(correct_value);
            }
            //for accidentals
            else if(note_input.length() > 1){
                //use noteMap
                String note1 = note_input;
                String note2 = noteMap.get(note_input);
                //if user is correct
                if(note1.equals(correct_value) || note2.equals(correct_value)){
                    correct_value = note_input;
                    textview.setBackgroundResource(R.drawable.border_tan_correct);
                    //put up correct answer
                    convertString(correct_value);
                    if(count < total) {
                        ++score;
                    }
                }
                //if user is incorrect
                else{
                    textview.setBackgroundResource(R.drawable.border_tan_wrong);
                    //put up correct answer
                    convertString(correct_value);

                }
            }
            //for diatonic
            else {
                //if user is correct
                if(note_input.equals(correct_value)){
                    textview.setBackgroundResource(R.drawable.border_tan_correct);
                    //put up correct answer
                    convertString(correct_value);
                    if(count < total){
                        ++score;
                    }
                }
                //if user is incorrect
                else{
                    textview.setBackgroundResource(R.drawable.border_tan_wrong);
                    //put up correct answer
                    convertString(correct_value);
                }

            }

        //}

    }

    //prepare and execute next note, called when NEXT button is clicked
    public void next(Bundle savedInstanceState){
        TextView textview = (TextView)findViewById(R.id.input_text);
        TextView textcorrect = (TextView)findViewById(R.id.correct_text);
        TextView current_score = (TextView)findViewById(R.id.current_score);
        //move indicator
        move_indicator();
        //play note
        if(sound_bool) {
            notes.get(0).get_mp().start();
        }
        //clear variables
        note_input = " ";
        textview_text = "";
        //clear textview
        textview.setText("");
        textview.setBackgroundResource(R.drawable.border_tan);
        textcorrect.setText("");
        ++count;
        String current_str =  score + " / " + count;
        current_score.setText(current_str);

    }
    //used to convert strings to string with unicode accidental symbols and update correct_text
    public void convertString(String correct_value){
        TextView textcorrect = (TextView)findViewById(R.id.correct_text);

        //if correct answer is an accidental
        if(correct_value.length() > 1){
            //display correct answer in other textview
            String note = correct_value.substring(0,1).toUpperCase();
            String accidental = correct_value.substring(correct_value.length() - 1);
            if(accidental.equals("s")){

                note = note + "♯";
                textcorrect.setText(note);
            }
            else{
                note = note + "♭";
                textcorrect.setText(note);
            }
        }
        else{
            String note = correct_value.toUpperCase();
            textcorrect.setText(note);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //array list for use with bundles and recovering/saving data. Check onSavedInstanceState and
        //repopulate functions
        String name_list[] = new String[notes.size()];
        double x_list[] = new double[notes.size()];
        double y_list[] = new double[notes.size()];

        //Take all notes remaining and put ids in arraylist, to be saved
        for(int i = 0; i < notes.size(); ++i){
            name_list[i] = notes.get(i).get_name();
            x_list[i] = notes.get(i).get_x();
            y_list[i] = notes.get(i).get_y();
        }
        outState.putStringArray("names", name_list);
        outState.putDoubleArray("x_pos", x_list);
        outState.putDoubleArray("y_pos", y_list);
        outState.putInt("score", score);
        outState.putInt("total", total);
        outState.putInt("count", count);
        super.onSaveInstanceState(outState);
    }

    //used to repopulate vectors and maps if onCreate must be called again
    public void repopulate(Bundle savedInstanceState){
        MediaPlayer mp;
        Resources res = getResources();

        //set scorekeeping
        score = savedInstanceState.getInt("score");
        total = savedInstanceState.getInt("total");
        count = savedInstanceState.getInt("count");

        String name_list[] = savedInstanceState.getStringArray("names");
        int x_list[] = savedInstanceState.getIntArray("x_pos");
        int y_list[] = savedInstanceState.getIntArray("y_pos");

        //reconstruct note_info objects and repopulate notes
        notes.clear();
        for(int i = 0; i < name_list.length; ++i){
            Integer resId = res.getIdentifier(name_list[i], "raw", this.getPackageName());
            mp = MediaPlayer.create(this, resId);
            notes.add(i, new note_info(x_list[i], y_list[i], name_list[i], mp, 0));
        }

    }
    public void move_indicator(){
        ImageView indicator = (ImageView)findViewById(R.id.indicator);
        ObjectAnimator xAnim = ObjectAnimator.ofFloat(indicator, "translationX", x_prev , notes.get(0).get_x());
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(indicator, "translationY", y_prev, notes.get(0).get_y());
        xAnim.start();
        yAnim.start();

    }

    public void ShowAgainCheckbox(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        SharedPreferences sharedpreferences = getSharedPreferences(Saved_Data, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.Donotshow_quiz:
                if(checked){
                    editor.putBoolean("QuizDoNotShowAgain", true);
                    editor.apply();
                }
                else{
                    editor.putBoolean("QuizDoNotShowAgain", false);
                    editor.apply();
                }
                break;
        }
    }

}
