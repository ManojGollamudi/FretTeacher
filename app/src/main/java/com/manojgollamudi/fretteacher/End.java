package com.manojgollamudi.fretteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class End extends AppCompatActivity{
    int fretnos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        TextView textview = (TextView)findViewById(R.id.result);
        Button b = (Button)findViewById(R.id.return_button);
        b.setBackgroundResource(R.drawable.border_tan);

        Intent end_intent = getIntent();
        int score = end_intent.getIntExtra("score_value", -1);
        int total = end_intent.getIntExtra("total_value", -1);
        fretnos = end_intent.getIntExtra("fretnos", -1);


        String score_string = score + "/" + total;

        textview.setText(score_string);
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
        Intent restart_intent = new Intent(this, MainGame.class);
        restart_intent.putExtra("fretnos", fretnos);
        startActivity(restart_intent);
        finish();
    }

}
