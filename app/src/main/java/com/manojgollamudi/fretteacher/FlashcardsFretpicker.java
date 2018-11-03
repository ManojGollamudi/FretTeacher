package com.manojgollamudi.fretteacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.manojgollamudi.fretteacher.GameList.transitionToast;


/**
 * Created by mgollamudi on 8/29/17.
 */


public class FlashcardsFretpicker extends AppCompatActivity {
    boolean initial = true;
    boolean range_of_frets = false;
    int range_start = 0;
    int range_end = 0;
    int single_fret = 0;
    boolean lefty_bool = false;


    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcards_fretpicker);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //set up numberpickers
        NumberPicker np_start = (NumberPicker)findViewById(R.id.numberpicker_range_start);
        NumberPicker np_end = (NumberPicker)findViewById(R.id.numberpicker_range_end);
        NumberPicker np_single = (NumberPicker)findViewById(R.id.numberpicker_single_fret);

        np_start.setMaxValue(10);
        np_end.setMaxValue(11);
        np_single.setMaxValue(11);

        np_start.setMinValue(1);
        np_end.setMinValue(2);
        np_single.setMinValue(1);


    }

    public void Choose_Range(View v){
        range_of_frets = true;
        //handle highlighting of buttons and show correct numberpickers
        Button b_range = (Button)findViewById(R.id.select_range_button);
        Button b_single = (Button)findViewById(R.id.select_single_button);

        NumberPicker np_start = (NumberPicker)findViewById(R.id.numberpicker_range_start);
        NumberPicker np_end = (NumberPicker)findViewById(R.id.numberpicker_range_end);
        NumberPicker np_single = (NumberPicker)findViewById(R.id.numberpicker_single_fret);
        CheckBox cb_lefty = (CheckBox)findViewById(R.id.lefty_checkbox);

        TextView text_through = (TextView)findViewById(R.id.through_textview);

        b_range.setBackgroundResource(R.drawable.border_tan_filled);
        b_range.setTextColor(getApplication().getResources().getColor(R.color.bluedark));

        if(initial){
            initial = false;
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.numberpicker_layout);
            Button b_start = (Button)findViewById(R.id.start_button);

            layout.setVisibility(View.VISIBLE);
            np_start.setVisibility(View.VISIBLE);
            np_end.setVisibility(View.VISIBLE);
            text_through.setVisibility(View.VISIBLE);
            b_start.setVisibility(View.VISIBLE);
            cb_lefty.setVisibility(View.VISIBLE);

        }
        if(!initial){
            b_single.setBackgroundResource(R.drawable.border_tan);
            b_single.setTextColor(getApplication().getResources().getColor(R.color.dark));

            np_start.setVisibility(View.VISIBLE);
            np_end.setVisibility(View.VISIBLE);
            np_single.setVisibility(View.INVISIBLE);
            text_through.setVisibility(View.VISIBLE);

        }
    }

    public void Choose_Single(View v){
        range_of_frets = false;

        //handle highlighting of buttons
        Button b_range = (Button)findViewById(R.id.select_range_button);
        Button b_single = (Button)findViewById(R.id.select_single_button);

        b_single.setBackgroundResource(R.drawable.border_tan_filled);
        b_single.setTextColor(getApplication().getResources().getColor(R.color.bluedark));

        NumberPicker np_single = (NumberPicker)findViewById(R.id.numberpicker_single_fret);
        NumberPicker np_start = (NumberPicker)findViewById(R.id.numberpicker_range_start);
        NumberPicker np_end = (NumberPicker)findViewById(R.id.numberpicker_range_end);
        CheckBox cb_lefty = (CheckBox)findViewById(R.id.lefty_checkbox);


        TextView text_through = (TextView)findViewById(R.id.through_textview);

        if(initial){
            initial = false;
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.numberpicker_layout);
            Button b_start = (Button)findViewById(R.id.start_button);

            layout.setVisibility(View.VISIBLE);
            np_single.setVisibility(View.VISIBLE);
            b_start.setVisibility(View.VISIBLE);
            cb_lefty.setVisibility(View.VISIBLE);
        }
        if(!initial){
            b_range.setBackgroundResource(R.drawable.border_tan);
            b_range.setTextColor(getApplication().getResources().getColor(R.color.dark));
            np_start.setVisibility(View.INVISIBLE);
            np_end.setVisibility(View.INVISIBLE);
            np_single.setVisibility(View.VISIBLE);
            text_through.setVisibility(View.INVISIBLE);
        }
    }
    public void Start(View v){

        CheckBox lefty_checkbox = (CheckBox)findViewById(R.id.lefty_checkbox);
        boolean checked = lefty_checkbox.isChecked();
        if(checked){
            lefty_bool = true;
        }
        NumberPicker np_single = (NumberPicker)findViewById(R.id.numberpicker_single_fret);
        NumberPicker np_start = (NumberPicker)findViewById(R.id.numberpicker_range_start);
        NumberPicker np_end = (NumberPicker)findViewById(R.id.numberpicker_range_end);

        range_start = np_start.getValue();
        range_end = np_end.getValue();
        single_fret = np_single.getValue();

        if(range_end <= range_start && range_of_frets){
            transitionToast = Toast.makeText(this, "Please choose a valid range", Toast.LENGTH_SHORT);
            transitionToast.show();
        }
        else{
            Intent flashcard_intent = new Intent(this, Flashcards.class);
            flashcard_intent.putExtra("range_of_frets", range_of_frets);
            flashcard_intent.putExtra("range_start", range_start);
            flashcard_intent.putExtra("range_end", range_end);
            flashcard_intent.putExtra("single_fret", single_fret);
            flashcard_intent.putExtra("lefty_bool", lefty_bool);
            transitionToast = Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT);
            transitionToast.show();
            startActivity(flashcard_intent);
        }

    }

    //for handling the checkboxes
    public void LeftyCheckbox(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.lefty_checkbox:
                if(checked){
                    lefty_bool = true;
                }
                else{
                    lefty_bool = false;
                }
                break;
        }
    }

}
