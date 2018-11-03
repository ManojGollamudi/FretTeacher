package com.manojgollamudi.fretteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class End extends AppCompatActivity{
    int fretnos = 0;
    boolean lefty_bool = false;
    boolean sound_bool = true;
    int score;
    int total;
    String Saved_Data = "Saved_Data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        TextView textview = (TextView)findViewById(R.id.result);
        Button b = (Button)findViewById(R.id.return_button);
        b.setBackgroundResource(R.drawable.border_tan);

        Intent end_intent = getIntent();
        score = end_intent.getIntExtra("score_value", -1);
        total = end_intent.getIntExtra("total_value", -1);
        fretnos = end_intent.getIntExtra("fretnos", -1);

        String score_string = score + "/" + total;
        textview.setText(score_string);

        update_score();
    }

    //for return to menu button
    public void return_menu(View view){

        Intent return_intent = new Intent(this, GameList.class);
        return_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(return_intent);
        finish();
    }
    //for restart button
    public void restart(View view) {
        Intent end_intent = getIntent();
        fretnos = end_intent.getIntExtra("fretnos", -1);
        lefty_bool = end_intent.getBooleanExtra("lefty_bool", false);
        sound_bool = end_intent.getBooleanExtra("sound_bool", false);
        Intent restart_intent = new Intent(this, Quiz.class);
        restart_intent.putExtra("fretnos", fretnos);
        restart_intent.putExtra("lefty_bool", lefty_bool);
        restart_intent.putExtra("sound_bool", sound_bool);
        startActivity(restart_intent);
        finish();
    }

    public void update_score(){
        SharedPreferences sharedpreferences = getSharedPreferences(Saved_Data, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String fretnos_string = "" + fretnos;
        int old_score = sharedpreferences.getInt(fretnos_string, 0);
        if(old_score == 0 || score > old_score){
            editor.putInt(fretnos_string, score);
        }
        editor.apply();
    }
}
