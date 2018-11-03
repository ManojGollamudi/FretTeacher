package com.manojgollamudi.fretteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class titleScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
    }

    public void Flashcards(View view){

        Intent intent = new Intent(this, flashcardsFretpicker.class);
        intent.putExtra("fretnos", 1);
        intent.putExtra("lefty_bool", false);
        intent.putExtra("sound_bool", false);
        startActivity(intent);
    }

    public void Quiz(View view){

        Intent intent = new Intent(this, gameList.class);
        startActivity(intent);
    }

}
