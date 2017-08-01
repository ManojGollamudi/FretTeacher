package com.manojgollamudi.fretteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
    }

    protected void onResume() {
        super.onResume();
        //reset button color
        Button b = (Button)findViewById(R.id.button_begin);
        b.setBackgroundResource(R.drawable.border_tan);
    }
    public void beginGame(View view){
        //change button color when clicked
        //Button b = (Button)findViewById(R.id.button_begin);
        //b.setBackgroundResource(R.drawable.border_tan_filled);
        Intent intent = new Intent(this, GameList.class);
        startActivity(intent);
    }
}
