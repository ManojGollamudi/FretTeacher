package com.manojgollamudi.fretteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class GameList extends AppCompatActivity{
    boolean sound_bool = true;
    boolean lefty_bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    protected void onResume(){
        super.onResume();
        //reset button colors
        Button b12 = (Button)findViewById(R.id.button_12);
        Button b34 = (Button)findViewById(R.id.button_34);
        Button b56 = (Button)findViewById(R.id.button_56);
        Button b78 = (Button)findViewById(R.id.button_78);
        Button b911 = (Button)findViewById(R.id.button_911);

        b12.setBackgroundResource(R.drawable.border_tan);
        b34.setBackgroundResource(R.drawable.border_tan);
        b56.setBackgroundResource(R.drawable.border_tan);
        b78.setBackgroundResource(R.drawable.border_tan);
        b911.setBackgroundResource(R.drawable.border_tan);
    }
    public void Start12(View v){
        //change button color when clicked
        Button b = (Button)findViewById(R.id.button_12);
        b.setBackgroundResource(R.drawable.border_tan_filled);
        Intent i12 = new Intent(this, MainGame.class);
        i12.putExtra("fretnos", 12);
        i12.putExtra("sound", sound_bool);
        i12.putExtra("lefty", lefty_bool);
        startActivity(i12);
    }
    public void Start34(View v){
        Button b = (Button)findViewById(R.id.button_34);
        b.setBackgroundResource(R.drawable.border_tan_filled);
        Intent i34 = new Intent(this, MainGame.class);
        i34.putExtra("fretnos", 34);
        i34.putExtra("sound", sound_bool);
        i34.putExtra("lefty", lefty_bool);
        startActivity(i34);
    }
    public void Start56(View v){
        Button b = (Button)findViewById(R.id.button_56);
        b.setBackgroundResource(R.drawable.border_tan_filled);
        Intent i56 = new Intent(this, MainGame.class);
        i56.putExtra("fretnos", 56);
        i56.putExtra("sound", sound_bool);
        i56.putExtra("lefty", lefty_bool);
        startActivity(i56);
    }
    public void Start78(View v){
        Button b = (Button)findViewById(R.id.button_78);
        b.setBackgroundResource(R.drawable.border_tan_filled);
        Intent i78 = new Intent(this, MainGame.class);
        i78.putExtra("fretnos", 78);
        i78.putExtra("sound", sound_bool);
        i78.putExtra("lefty", lefty_bool);
        startActivity(i78);
    }
    public void Start911(View v){
        Button b = (Button)findViewById(R.id.button_911);
        b.setBackgroundResource(R.drawable.border_tan_filled);
        Intent i911 = new Intent(this, MainGame.class);
        i911.putExtra("fretnos", 911);
        i911.putExtra("sound", sound_bool);
        i911.putExtra("lefty", lefty_bool);
        startActivity(i911);
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

}


