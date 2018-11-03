package com.manojgollamudi.fretteacher;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.manojgollamudi.fretteacher.gameList.transitionToast;


public class flashcards extends AppCompatActivity {
    //boolean to keep track of state
    boolean state = false;

    //booleans to keep track of user preferences: sound and lefty mode
    boolean sound_on = false;
    boolean is_lefty;

    boolean range_of_frets;

    //keep track of when the flashcard is "flipped"
    boolean is_flipped = false;

    //keep track of previous indicator position
    float x_prev = 0;
    float y_prev = 0;

    //number of notes
    int num_notes = 6;

    boolean DoNotShowAgain;

    //to keep track of score
    int score = 0;
    int total = 0;
    int count = 0;

    //for use keeping track of which frets are being used
    int fretnos = 0;

    //map of string notes, used to associate equivalent notes
    Map <String, String> noteMap = new HashMap<>(24);

    //map of index - note notes, used to index notes
    Map <Integer, String> indexMap = new HashMap<>(12);

    //vector of name_coordinate objects
    ArrayList<noteInfo> notes = new ArrayList<>();

    //index for looping through notes
    int notes_index = 0;

    CardView card_top;

    String Saved_Data = "Saved_Data";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcards_layout);

        //Setup toolbar

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        notes.clear();


        if (savedInstanceState != null) {
            state = true;
        }

        //assign image variables
        Intent game_intent = getIntent();
        is_lefty = game_intent.getBooleanExtra("lefty", false);

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

        //populate indexMap, used for automated naming based on low E string
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
        //set up initial help alert dialog box
        SharedPreferences sharedpreferences = getSharedPreferences(Saved_Data, Context.MODE_PRIVATE);
        DoNotShowAgain = sharedpreferences.getBoolean("FlashcardsDoNotShowAgain", false);

        if(!DoNotShowAgain){
            final Dialog dialog = new Dialog(flashcards.this);
            dialog.setContentView(R.layout.flashcards_instructions_layout);
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.show();
            Button declineButton = (Button) dialog.findViewById(R.id.gotit);
            declineButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        //set up reference alert dialog box
        final Button b_ref = (Button) findViewById(R.id.diagram_button);
        b_ref.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(flashcards.this);
                if (is_lefty) {
                    dialog.setContentView(R.layout.layout_reference_l);
                } else {
                    dialog.setContentView(R.layout.layout_reference);
                }

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(width, height);

                dialog.show();

                //set up return button for dialog box
                Button declineButton = (Button) dialog.findViewById(R.id.ref_return);
                declineButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        //setting up based on which frets user chose
        range_of_frets = game_intent.getBooleanExtra("range_of_frets", false);

        //sound_on = game_intent.getBooleanExtra("sound", false);
        is_lefty = game_intent.getBooleanExtra("is_lefty", false);


        //set fret number indicator
        TextView number_indicator = (TextView) findViewById(R.id.fretnumber);

        Resources res = getResources();


        //move fret number indicator if lefty mode
        if (is_lefty) {
            //convert 250 dp to px
            float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, res.getDisplayMetrics());
            int margin_int = (int) margin;
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) number_indicator
                    .getLayoutParams();

            mlp.setMargins(margin_int, 0, 0, 0);

        }

        //if activity is being restarted, recover previous state
        if (savedInstanceState != null) {
            repopulate(savedInstanceState);
        }
        //if not, populate notes vector and handle media
        else {
            String note = indexMap.get(fretnos);
            MediaPlayer mp;

            //if user chose a range of frets
            if(range_of_frets){
                //get range passed from FlashcardsFretpicker
                int start = game_intent.getIntExtra("range_start", 0);
                int end = game_intent.getIntExtra("range_end", 0);
                int notecounter = start;
                int fretnumber = start;
                num_notes = (end - start + 1) * 6;

                //loop through six strings
                for (int j = 5; j >= 0; --j) {
                    //if lefty mode is not on
                    if (!is_lefty) {
                        //loop through range of frets, going from left to right
                        for (int i = start; i <= end; ++i) {
                            //each fret length is 100 dp
                            float x_pos = (100 * ((i - 1) % 2));
                            if(fretnumber == 11){
                                x_pos = 200;
                            }
                            float y_pos = 0;
                            //distance between strings depends on string width -
                            if (j == 0) {
                                y_pos = 43;
                            }
                            if (j == 1) {
                                y_pos = 71;
                            }
                            if (j == 2) {
                                y_pos = 97;
                            }
                            if (j == 3) {
                                y_pos = 124;
                            }
                            if (j == 4) {
                                y_pos = 153;
                            }
                            if (j == 5) {
                                y_pos = 180;
                            }
                            //convert to sp
                            x_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x_pos, res.getDisplayMetrics());
                            y_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y_pos, res.getDisplayMetrics());

                            StringBuilder str = new StringBuilder(indexMap.get(notecounter % 12));
                            str.append(fretnumber);
                            //double fretnumber for high e string, naming convention
                            if (j == 0) {
                                str.append(fretnumber);
                            }
                            Integer resId = res.getIdentifier(str.toString(), "raw", this.getPackageName());
                            mp = MediaPlayer.create(this, resId);
                            noteInfo note_temp = new noteInfo(x_pos, y_pos, str.toString(), mp, fretnumber);
                            notes.add(note_temp);
                            ++notecounter;
                            ++fretnumber;
                        }
                    }
                    if (is_lefty) {
                        //loop through number of frets shown, going from right to left
                        for (int i = start; i <= end; ++i) {
                            //make coordinates for indicator based on note location on fretboard
                            float x_pos;

                            if (fretnumber == 11) {
                                x_pos = 0;
                            }
                            else if(i % 2 != 0){
                                x_pos = 200;
                            }
                            else{
                                x_pos = 100;
                            }


                            float y_pos = 0;
                            //distance between strings depends on string width -
                            if (j == 0) {
                                y_pos = 43;
                            }
                            if (j == 1) {
                                y_pos = 71;
                            }
                            if (j == 2) {
                                y_pos = 97;
                            }
                            if (j == 3) {
                                y_pos = 124;
                            }
                            if (j == 4) {
                                y_pos = 153;
                            }
                            if (j == 5) {
                                y_pos = 180;
                            }
                            //convert to sp
                            x_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x_pos, res.getDisplayMetrics());
                            y_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y_pos, res.getDisplayMetrics());

                            StringBuilder str = new StringBuilder(indexMap.get(notecounter % 12));
                            str.append(fretnumber);
                            //double fretnumber for high e string, naming convention
                            if (j == 0) {
                                str.append(fretnumber);
                            }
                            Integer resId = res.getIdentifier(str.toString(), "raw", this.getPackageName());
                            mp = MediaPlayer.create(this, resId);
                            noteInfo note_temp = new noteInfo(x_pos, y_pos, str.toString(), mp, fretnumber);
                            notes.add(note_temp);
                            ++notecounter;
                            ++fretnumber;
                        }
                    }
                    //go back to original string note
                    notecounter = notecounter - (end - start + 1);
                    //guitar strings separated by 5 half steps, except for G and B
                    if (j == 2) {
                        notecounter = notecounter + 4;
                    }
                    else {
                        notecounter = notecounter + 5;
                    }
                    //reset fretnumber
                    fretnumber = start;
                }
            }

            //if user chose a single fret
            else {
                int single_fret = game_intent.getIntExtra("single_fret", 0);
                int notecounter = single_fret;
                int fretnumber = single_fret;
                float x_pos;

                for (int j = 5; j >= 0; --j) {
                    //if lefty mode is not on
                    if (!is_lefty) {
                        //where to put indicator based on odd/even numbered frets and picture
                        if (fretnumber == 11) {
                            x_pos = 200;
                        }
                        else if (single_fret % 2 != 0) {
                            //put at beginning
                            x_pos = 0;
                        }
                        else {
                            x_pos = 100;
                        }

                        float y_pos = 0;
                        //distance between strings depends on string width -
                        if (j == 0) {
                            y_pos = 43;
                        }
                        if (j == 1) {
                            y_pos = 71;
                        }
                        if (j == 2) {
                            y_pos = 97;
                        }
                        if (j == 3) {
                            y_pos = 124;
                        }
                        if (j == 4) {
                            y_pos = 153;
                        }
                        if (j == 5) {
                            y_pos = 180;
                        }
                        //convert to sp
                        x_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x_pos, res.getDisplayMetrics());
                        y_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y_pos, res.getDisplayMetrics());

                        StringBuilder str = new StringBuilder(indexMap.get(notecounter % 12));
                        str.append(fretnumber);
                        //double fretnumber for high e string, naming convention
                        if (j == 0) {
                            str.append(fretnumber);
                        }
                        Integer resId = res.getIdentifier(str.toString(), "raw", this.getPackageName());
                        mp = MediaPlayer.create(this, resId);
                        noteInfo note_temp = new noteInfo(x_pos, y_pos, str.toString(), mp, fretnumber);
                        notes.add(note_temp);
                    }
                    if (is_lefty) {
                        x_pos = 100;
                        if (single_fret % 2 != 0) {
                            x_pos = 200;
                        }
                        if (fretnumber == 11) {
                            x_pos = 0;
                        }
                            float y_pos = 0;
                            //distance between strings depends on string width -
                            if (j == 0) {
                                y_pos = 43;
                            }
                            if (j == 1) {
                                y_pos = 71;
                            }
                            if (j == 2) {
                                y_pos = 97;
                            }
                            if (j == 3) {
                                y_pos = 124;
                            }
                            if (j == 4) {
                                y_pos = 153;
                            }
                            if (j == 5) {
                                y_pos = 180;
                            }
                            //convert to sp
                            x_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x_pos, res.getDisplayMetrics());
                            y_pos = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y_pos, res.getDisplayMetrics());

                            StringBuilder str = new StringBuilder(indexMap.get(notecounter % 12));
                            str.append(fretnumber);
                            //double fretnumber for high e string, naming convention
                            if (j == 0) {
                                str.append(fretnumber);
                            }
                            Integer resId = res.getIdentifier(str.toString(), "raw", this.getPackageName());
                            mp = MediaPlayer.create(this, resId);
                            noteInfo note_temp = new noteInfo(x_pos, y_pos, str.toString(), mp, single_fret);
                            notes.add(note_temp);
                        }

                    //guitar strings separated by 5 half steps, except for G and B
                    if (j == 2) {
                        notecounter = notecounter + 4;
                    } else {
                        notecounter = notecounter + 5;
                    }
                }
            }


            //shuffle notes
            if (savedInstanceState == null) {
                Collections.shuffle(notes, new Random(System.nanoTime()));
                total = notes.size();
            }

            //set overview image
            setup();

            ImageView indicator = (ImageView) findViewById(R.id.indicator);
            ObjectAnimator xAnim = ObjectAnimator.ofFloat(indicator, "translationX", x_prev, notes.get(0).get_x());
            ObjectAnimator yAnim = ObjectAnimator.ofFloat(indicator, "translationY", y_prev, notes.get(0).get_y());
            xAnim.start();
            yAnim.start();


            //play first note's sound
            if (!state && sound_on) {
                notes.get(0).get_mp().start();
            }

            //set initial answer
            setAnswer(notes.get(0).get_name());

        }

        //set up touch commands
        card_top = (CardView) findViewById(R.id.card_view_image);

        card_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!is_flipped) {
                    RelativeLayout layout_img = (RelativeLayout) findViewById(R.id.layout_image);
                    RelativeLayout layout_ans = (RelativeLayout) findViewById(R.id.layout_answer);
                    layout_img.setVisibility(View.INVISIBLE);
                    layout_ans.setVisibility(View.VISIBLE);
                    is_flipped = true;
                } else {
                    RelativeLayout layout_img = (RelativeLayout) findViewById(R.id.layout_image);
                    RelativeLayout layout_ans = (RelativeLayout) findViewById(R.id.layout_answer);
                    layout_img.setVisibility(View.VISIBLE);
                    layout_ans.setVisibility(View.INVISIBLE);
                    is_flipped = false;
                }
            }

        });

        card_top.setOnTouchListener(new onSwipeTouchListener() {
            public boolean onSwipeRight() {
                moveCardRight();
                return true;
            }

            public boolean onSwipeLeft() {
                nextNoteLeft();
                moveCardLeft();
                return true;
            }
        });

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
            notes.add(i, new noteInfo(x_list[i], y_list[i], name_list[i], mp, 0));
        }

    }
    public void moveIndicator(){

        ImageView indicator = (ImageView) findViewById(R.id.indicator);

        ObjectAnimator xAnim = ObjectAnimator.ofFloat(indicator, "translationX", x_prev , notes.get(notes_index).get_x());
        xAnim.setDuration(0);
        xAnim.setStartDelay(100);
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(indicator, "translationY", y_prev, notes.get(notes_index).get_y());
        yAnim.setDuration(0);
        yAnim.setStartDelay(100);
        xAnim.start();
        yAnim.start();

        //setup previous notes
        x_prev = notes.get(notes_index).get_x();
        y_prev = notes.get(notes_index).get_y();
    }
    public void moveCardRight(){

        CardView card = (CardView) findViewById(R.id.card_view_image);
        TextView fret_number = (TextView) findViewById(R.id.fretnumber);
        TextView answer = (TextView)findViewById(R.id.answer);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        Animation fade_out = AnimationUtils.loadAnimation(this, R.anim.fadein);

        ObjectAnimator card_in = ObjectAnimator.ofFloat(card, "translationX", -4000, 10);
        card_in.setDuration(50);
        card_in.setStartDelay(200);

        ObjectAnimator card_out = ObjectAnimator.ofFloat(card, "translationX", 10, 2000);
        card_out.setDuration(200);

        card_in.start();
        card_out.start();
        answer.startAnimation(fade_out);
        answer.startAnimation(fade_in);
        fret_number.startAnimation(fade_out);
        fret_number.startAnimation(fade_in);

        nextNoteRight();


    }

    public void moveCardLeft(){

        CardView card = (CardView) findViewById(R.id.card_view_image);
        TextView fret_number = (TextView) findViewById(R.id.fretnumber);
        TextView answer = (TextView)findViewById(R.id.answer);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        Animation fade_out = AnimationUtils.loadAnimation(this, R.anim.fadein);

        ObjectAnimator card_in = ObjectAnimator.ofFloat(card, "translationX", 4000, 10);
        card_in.setDuration(200);
        card_in.setStartDelay(100);

        ObjectAnimator card_out = ObjectAnimator.ofFloat(card, "translationX", 10, -2000);
        card_out.setDuration(100);

        card_in.start();
        card_out.start();
        answer.startAnimation(fade_out);
        answer.startAnimation(fade_in);
        fret_number.startAnimation(fade_out);
        fret_number.startAnimation(fade_in);

    }

    public void setAnswer(String name){

        TextView answer_text = (TextView)findViewById(R.id.answer);
        String correct_value = name.replaceAll("[0-9]", "");

        //if correct answer is an accidental
        if(correct_value.length() > 1){
            //display correct answer in other textview
            String note = correct_value.substring(0,1).toUpperCase();
            String accidental = correct_value.substring(correct_value.length() - 1);
            if(accidental.equals("s")){
                note = note + "♯";
                answer_text.setText(note);
            }
            else{
                note = note + "♭";
                answer_text.setText(note);
            }
        }
        else{
            String note = correct_value.toUpperCase();
            answer_text.setText(note);
        }
    }

    public void nextNoteRight(){

        ++notes_index;
        if(notes_index > num_notes - 1){
            notes_index = 0;
        }
        moveIndicator();
        setAnswer(notes.get(notes_index).get_name());
        setup();
    }


    public void nextNoteLeft(){

        --notes_index;
        if(notes_index < 0){
            notes_index = num_notes - 1;
        }
        moveIndicator();
        setAnswer(notes.get(notes_index).get_name());
        setup();
    }

    public void setup(){
        fretnos = notes.get(notes_index).get_fretnumber();
        if(fretnos % 2 == 0){
            --fretnos;
        }
        if(fretnos == 11){
            fretnos = 9;
        }

        TextView number_indicator = (TextView) findViewById(R.id.fretnumber);
        ImageView overview = (ImageView) findViewById(R.id.overview);

        //set fret number indicator
        String fret_number = "" + fretnos;
        number_indicator.setText(fret_number);

        if(fretnos == 1){
            if(is_lefty){
                overview.setImageResource(R.drawable.overview12_l);
            }
            else{
                overview.setImageResource(R.drawable.overview12);
            }
        }
        if (fretnos == 3) {
            if (is_lefty) {
                overview.setImageResource(R.drawable.overview34_l);
            } else {
                overview.setImageResource(R.drawable.overview34);
            }
        }
        if (fretnos == 5) {
            if (is_lefty) {
                overview.setImageResource(R.drawable.overview56_l);
            } else {
                overview.setImageResource(R.drawable.overview56);
            }
        }
        if (fretnos == 7) {
            if (is_lefty) {
                overview.setImageResource(R.drawable.overview78_l);
            } else {
                overview.setImageResource(R.drawable.overview78);
            }
        }
        if (fretnos == 9) {
            if (is_lefty) {
                overview.setImageResource(R.drawable.overview911_l);

            } else {
                overview.setImageResource(R.drawable.overview911);
            }
        }
    }

    public void Shuffle(View v){
        Collections.shuffle(notes, new Random(System.nanoTime()));

        notes_index = 0;
        moveIndicator();
        setAnswer(notes.get(notes_index).get_name());
        setup();

        transitionToast = Toast.makeText(this, "Shuffled!", Toast.LENGTH_SHORT);
        transitionToast.show();

        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fadeoutshuffle);
        Animation fade_out = AnimationUtils.loadAnimation(this, R.anim.fadeinshuffle);
        CardView card = (CardView) findViewById(R.id.card_view_image);
        card.startAnimation(fade_out);
        card.startAnimation(fade_in);
    }

    public void PlayNote(View v){
        notes.get(notes_index).get_mp().start();
    }

    public void ShowAgainCheckbox(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        SharedPreferences sharedpreferences = getSharedPreferences(Saved_Data, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.Donotshow:
                if(checked){
                    editor.putBoolean("FlashcardsDoNotShowAgain", true);
                    editor.apply();
                }
                else{
                    editor.putBoolean("FlashcardsDoNotShowAgain", false);
                    editor.apply();
                }
                break;
        }
    }
}
