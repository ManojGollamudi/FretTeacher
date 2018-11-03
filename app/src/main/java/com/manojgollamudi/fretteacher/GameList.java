package com.manojgollamudi.fretteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class GameList extends AppCompatActivity {
    boolean sound_bool = false;
    boolean lefty_bool = false;
    int fretnos = 0;
    Button prev;
    boolean initial = true;
    String Saved_Data = "Saved_Data";

    public static Toast transitionToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        UpdateScores();

    }

    public void Start12(View v){
        fretnos = 1;
        start();
    }
    public void Start35(View v){
        fretnos = 3;
        start();
    }
    public void Start57(View v){
        fretnos = 5;
        start();
    }
    public void Start79(View v){
        fretnos = 7;
        start();
    }
    public void Start911(View v){
        fretnos = 9;
        start();
    }

    public void start(){
            Intent i911 = new Intent(this, Quiz.class);
            i911.putExtra("fretnos", fretnos);
            i911.putExtra("sound", sound_bool);
            i911.putExtra("lefty", lefty_bool);
            transitionToast = Toast.makeText(this, "Loading...", Toast.LENGTH_LONG);
            transitionToast.show();
            startActivity(i911);
            finish();
    }

    //for handling the checkboxes
    public void onClickCheckbox(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_sound:
                if(checked){
                    sound_bool = true;
                }
                else{
                    sound_bool = false;
                }
                break;
            case R.id.checkbox_lefty:
                if(checked){
                    lefty_bool = true;
                }
                else{
                    lefty_bool = false;
                }
                break;
        }
    }

    public void UpdateScores(){
        SharedPreferences sharedpreferences = getSharedPreferences(Saved_Data, Context.MODE_PRIVATE);
        TextView score_13 = (TextView)findViewById(R.id.score_13);
        TextView score_35 = (TextView)findViewById(R.id.score_35);
        TextView score_57 = (TextView)findViewById(R.id.score_57);
        TextView score_79 = (TextView)findViewById(R.id.score_79);
        TextView score_911 = (TextView)findViewById(R.id.score_911);

        int best_13 = sharedpreferences.getInt("1", -1);
        if(best_13 != -1){
            String string_13 = "Best Score: " + best_13 + " / 18";
            score_13.setText(string_13);
        }

        int best_35 = sharedpreferences.getInt("3", -1);
        if(best_35 != -1){
            String string_35 = "Best Score: " + best_35 + " / 18";
            score_35.setText(string_35);
        }

        int best_57 = sharedpreferences.getInt("5", -1);
        if(best_57 != -1){
            String string_57 = "Best Score: " + best_57 + " / 18";
            score_57.setText(string_57);
        }

        int best_79 = sharedpreferences.getInt("7", -1);
        if(best_79 != -1){
            String string_79 = "Best Score: " + best_79 + " / 18";
            score_79.setText(string_79);
        }

        int best_911 = sharedpreferences.getInt("9", -1);
        if(best_911 != -1){
            String string_911 = "Best Score: " + best_911 + " / 18";
            score_911.setText(string_911);
        }
    }

    }


