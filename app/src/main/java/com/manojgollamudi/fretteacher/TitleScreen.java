package com.manojgollamudi.fretteacher;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TitleScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
    }

    public void Flashcards(View view){

        Intent intent = new Intent(this, FlashcardsFretpicker.class);
        intent.putExtra("fretnos", 1);
        intent.putExtra("lefty_bool", false);
        intent.putExtra("sound_bool", false);
        startActivity(intent);
    }

    public void Quiz(View view){

        Intent intent = new Intent(this, GameList.class);
        startActivity(intent);
    }

}
