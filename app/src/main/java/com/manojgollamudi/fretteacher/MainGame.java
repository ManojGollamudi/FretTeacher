package com.manojgollamudi.fretteacher;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.Image;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class MainGame extends AppCompatActivity {
    //boolean to keep track of submit/next button. False = button named submit, true = named next.
    boolean submit_next = false;

    //boolean to keep track of state
    boolean state = false;

    //booleans to keep track of user preferences: sound and lefty mode
    boolean sound_bool;
    boolean lefty_bool;

    //to keep track of score
    int score = 0;
    int total = 0;
    int count = 0;

    //for use keeping track of which frets are being used
    int fretnos = 0;

    //variable to store user input, to be compared with fragment tags
    String note_input = "NONE";
    String textview_text = "";

    //map of pairs, used to associate equivalent answers
    Map <String, String> noteMap = new HashMap<>(24);

    //map of images and associated general names, for automated association
    Map <Integer, String> nameMap = new HashMap<>(66);

    //map of name(string)-song(int) pairs, used to play audio for notes
    final Map <Integer, MediaPlayer> songMap = new HashMap<>();



    //
    MediaPlayer mediaplayer;

    //vector of image and name pairs
    ArrayList<namePair> pairs = new ArrayList<>();



    //strings to put in pairs
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

    //images stored as ints
    int a10;
    int a2;
    int a5;
    int a55;
    int a7;
    int as1;
    int as11;
    int as3;
    int as6;
    int as66;
    int as8;
    int b2;
    int b4;
    int b7;
    int b77;
    int b9;
    int c1;
    int c10;
    int c3;
    int c5;
    int c8;
    int c88;
    int cs11;
    int cs2;
    int cs4;
    int cs6;
    int cs9;
    int cs99;
    int d10;
    int d1010;
    int d3;
    int d5;
    int d7;
    int ds1;
    int ds11;
    int ds1111;
    int ds4;
    int ds6;
    int ds8;
    int e2;
    int e5;
    int e7;
    int e9;
    int f1;
    int f10;
    int f11;
    int f3;
    int f6;
    int f8;
    int fs11;
    int fs2;
    int fs22;
    int fs4;
    int fs7;
    int fs9;
    int g10;
    int g3;
    int g33;
    int g5;
    int g8;
    int gs1;
    int gs11;
    int gs4;
    int gs44;
    int gs6;
    int gs9 ;





    //songs stored as int
    int e2_song = R.raw.e2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        pairs.clear();

        if(savedInstanceState != null){
            state = true;
        }

        final TextView textview = (TextView)findViewById(R.id.input_text);

        //make next button invisible
        //Button next = (Button) findViewById(R.id.next_button);
        //next.setVisibility(View.GONE);

        //assign image variables
        Intent game_intent = getIntent();
        lefty_bool = game_intent.getBooleanExtra("lefty", false);

        if(lefty_bool){
            a10 = R.drawable.a10_l;
            a2 = R.drawable.a2_l;
            a5 = R.drawable.a5_l;
            a55 = R.drawable.a55_l;
            a7 = R.drawable.a7_l;
            as1 = R.drawable.as1_l;
            as11 = R.drawable.as11_l;
            as3 = R.drawable.as3_l;
            as6 = R.drawable.as6_l;
            as66 = R.drawable.as66_l;
            as8 = R.drawable.as8_l;
            b2 = R.drawable.b2_l;
            b4 = R.drawable.b4_l;
            b7 = R.drawable.b7_l;
            b77 = R.drawable.b77_l;
            b9 = R.drawable.b9_l;
            c1 = R.drawable.c1_l;
            c10 = R.drawable.c10_l;
            c3 = R.drawable.c3_l;
            c5 = R.drawable.c5_l;
            c8 = R.drawable.c8_l;
            c88 = R.drawable.c88_l;
            cs11 = R.drawable.cs11_l;
            cs2 = R.drawable.cs2_l;
            cs4 = R.drawable.cs4_l;
            cs6 = R.drawable.cs6_l;
            cs9 = R.drawable.cs9_l;
            cs99 = R.drawable.cs99_l;
            d10 = R.drawable.d10_l;
            d1010 = R.drawable.d1010_l;
            d3 = R.drawable.d3_l;
            d5 = R.drawable.d5_l;
            d7 = R.drawable.d7_l;
            ds1 = R.drawable.ds1_l;
            ds11 = R.drawable.ds11_l;
            ds1111 = R.drawable.ds1111_l;
            ds4 = R.drawable.ds4_l;
            ds6 = R.drawable.ds6_l;
            ds8 = R.drawable.ds8_l;
            e2 = R.drawable.e2_l;
            e5 = R.drawable.e5_l;
            e7 = R.drawable.e7_l;
            e9 = R.drawable.e9_l;
            f1 = R.drawable.f1_l;
            f10 = R.drawable.f10_l;
            f11 = R.drawable.f11_l;
            f3 = R.drawable.f3_l;
            f6 = R.drawable.f6_l;
            f8 = R.drawable.f8_l;
            fs11 = R.drawable.fs11_l;
            fs2 = R.drawable.fs2_l;
            fs22 = R.drawable.fs22_l;
            fs4 = R.drawable.fs4_l;
            fs7 = R.drawable.fs7_l;
            fs9 = R.drawable.fs9_l;
            g10 = R.drawable.g10_l;
            g3 = R.drawable.g3_l;
            g33 = R.drawable.g33_l;
            g5 = R.drawable.g5_l;
            g8 = R.drawable.g8_l;
            gs1 = R.drawable.gs1_l;
            gs11 = R.drawable.gs11_l;
            gs4 = R.drawable.gs4_l;
            gs44 = R.drawable.gs44_l;
            gs6 = R.drawable.gs6_l;
            gs9 = R.drawable.gs9_l;
        }
        else {
            a10 = R.drawable.a10;
            a2 = R.drawable.a2;
            a5 = R.drawable.a5;
            a55 = R.drawable.a55;
            a7 = R.drawable.a7;
            as1 = R.drawable.as1;
            as11 = R.drawable.as11;
            as3 = R.drawable.as3;
            as6 = R.drawable.as6;
            as66 = R.drawable.as66;
            as8 = R.drawable.as8;
            b2 = R.drawable.b2;
            b4 = R.drawable.b4;
            b7 = R.drawable.b7;
            b77 = R.drawable.b77;
            b9 = R.drawable.b9;
            c1 = R.drawable.c1;
            c10 = R.drawable.c10;
            c3 = R.drawable.c3;
            c5 = R.drawable.c5;
            c8 = R.drawable.c8;
            c88 = R.drawable.c88;
            cs11 = R.drawable.cs11;
            cs2 = R.drawable.cs2;
            cs4 = R.drawable.cs4;
            cs6 = R.drawable.cs6;
            cs9 = R.drawable.cs9;
            cs99 = R.drawable.cs99;
            d10 = R.drawable.d10;
            d1010 = R.drawable.d1010;
            d3 = R.drawable.d3;
            d5 = R.drawable.d5;
            d7 = R.drawable.d7;
            ds1 = R.drawable.ds1;
            ds11 = R.drawable.ds11;
            ds1111 = R.drawable.ds1111;
            ds4 = R.drawable.ds4;
            ds6 = R.drawable.ds6;
            ds8 = R.drawable.ds8;
            e2 = R.drawable.e2;
            e5 = R.drawable.e5;
            e7 = R.drawable.e7;
            e9 = R.drawable.e9;
            f1 = R.drawable.f1;
            f10 = R.drawable.f10;
            f11 = R.drawable.f11;
            f3 = R.drawable.f3;
            f6 = R.drawable.f6;
            f8 = R.drawable.f8;
            fs11 = R.drawable.fs11;
            fs2 = R.drawable.fs2;
            fs22 = R.drawable.fs22;
            fs4 = R.drawable.fs4;
            fs7 = R.drawable.fs7;
            fs9 = R.drawable.fs9;
            g10 = R.drawable.g10;
            g3 = R.drawable.g3;
            g33 = R.drawable.g33;
            g5 = R.drawable.g5;
            g8 = R.drawable.g8;
            gs1 = R.drawable.gs1;
            gs11 = R.drawable.gs11;
            gs4 = R.drawable.gs4;
            gs44 = R.drawable.gs44;
            gs6 = R.drawable.gs6;
            gs9 = R.drawable.gs9;
        }

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

        //populate nameMap
        nameMap.put(a10, "a");
        nameMap.put(a2, "a");
        nameMap.put(a5, "a");
        nameMap.put(a55, "a");
        nameMap.put(a7, "a");
        nameMap.put(as1, "as");
        nameMap.put(as11, "as");
        nameMap.put(as3, "as");
        nameMap.put(as6, "as");
        nameMap.put(as66, "as");
        nameMap.put(as8, "as");
        nameMap.put(b2, "b");
        nameMap.put(b4, "b");
        nameMap.put(b7, "b");
        nameMap.put(b77, "b");
        nameMap.put(b9, "b");
        nameMap.put(c1, "c");
        nameMap.put(c10, "c");
        nameMap.put(c3, "c");
        nameMap.put(c5, "c");
        nameMap.put(c8, "c");
        nameMap.put(c88, "c");
        nameMap.put(cs11, "cs");
        nameMap.put(cs2, "cs");
        nameMap.put(cs4, "cs");
        nameMap.put(cs6, "cs");
        nameMap.put(cs9, "cs");
        nameMap.put(cs99, "cs");
        nameMap.put(d10, "d");
        nameMap.put(d1010, "d");
        nameMap.put(d3, "d");
        nameMap.put(d5, "d");
        nameMap.put(d7, "d");
        nameMap.put(ds1, "ds");
        nameMap.put(ds11, "ds");
        nameMap.put(ds1111, "ds");
        nameMap.put(ds4, "ds");
        nameMap.put(ds6, "ds");
        nameMap.put(ds8, "ds");
        nameMap.put(e2, "e");
        nameMap.put(e5, "e");
        nameMap.put(e7, "e");
        nameMap.put(e9, "e");
        nameMap.put(f1, "f");
        nameMap.put(f10, "f");
        nameMap.put(f11 , "f");
        nameMap.put(f3, "f");
        nameMap.put(f6, "f");
        nameMap.put(f8, "f");
        nameMap.put(fs11, "fs");
        nameMap.put(fs2 , "fs");
        nameMap.put(fs22, "fs");
        nameMap.put(fs4, "fs");
        nameMap.put(fs7, "fs");
        nameMap.put(fs9, "fs");
        nameMap.put(g10, "g");
        nameMap.put(g3, "g");
        nameMap.put(g33, "g");
        nameMap.put(g5, "g");
        nameMap.put(g8, "g");
        nameMap.put(gs1, "gs");
        nameMap.put(gs11, "gs");
        nameMap.put(gs4, "gs");
        nameMap.put(gs44, "gs");
        nameMap.put(gs6, "gs");
        nameMap.put(gs9, "gs");


        //set up reference alert dialog box
        final Button b_ref = (Button)findViewById(R.id.button_reference);
        b_ref.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0){
                final Dialog dialog = new Dialog(MainGame.this);
                if(lefty_bool){
                    dialog.setContentView(R.layout.layout_reference_l);
                }
                else {
                    dialog.setContentView(R.layout.layout_reference);
                }

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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



        /*final ToggleButton toggle_sharp = (ToggleButton) findViewById(R.id.sharp_toggle);
        final ToggleButton toggle_flat = (ToggleButton) findViewById(R.id.flat_toggle);

        //if the sharp toggle is clicked
        toggle_sharp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //the toggle is enabled:
                if (isChecked) {
                    if(textview_text != "NONE"){
                        //disable flat toggle
                        toggle_flat.setChecked(false);

                        textview_text = textview_text + "♯";
                        note_input = note_input + "s";
                        textview.setText(textview_text);
                    }
                }
                //The toggle is disabled:
                else {
                    textview_text = textview_text.substring(0,1);
                    note_input = note_input.substring(0,1);
                    textview.setText(textview_text);
                }
            }
        });

        //if the flat toggle is clicked
        toggle_flat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //the toggle is enabled:
                if (isChecked) {
                    //can't put flat symbol alone
                    if(textview_text != "NONE"){
                        //disable sharp toggle
                        toggle_sharp.setChecked(false);

                        textview_text = textview_text + "♭";
                        note_input = note_input + "f";
                        textview.setText(textview_text);
                    }
                }
                //The toggle is disabled:
                else {
                    textview_text = textview_text.substring(0,1);
                    note_input = note_input.substring(0,1);
                    textview.setText(textview_text);
                }
            }
        });*/

        //setting up game based on which frets user chose
        fretnos = game_intent.getIntExtra("fretnos", -1);
        sound_bool = game_intent.getBooleanExtra("sound", true);
        lefty_bool = game_intent.getBooleanExtra("lefty", true);

        ImageView overview = (ImageView) findViewById(R.id.overview);

        if(fretnos == 12){
            if(!lefty_bool) {
                overview.setImageResource(R.drawable.overview12);
            }
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview12_l);
            }
            //no restart
            if(savedInstanceState == null) {
                namePair pair_f = new namePair(f1, string_f);
                namePair pair_as1 = new namePair(as1, string_as);
                namePair pair_ds1 = new namePair(ds1, string_ds);
                namePair pair_gs1 = new namePair(gs1, string_gs);
                namePair pair_c1 = new namePair(c1, string_c);
                namePair pair_f11 = new namePair(f11, string_f);
                namePair pair_fs2 = new namePair(fs2, string_fs);
                namePair pair_b2 = new namePair(b2, string_b);
                namePair pair_e2 = new namePair(e2, string_e);
                namePair pair_a2 = new namePair(a2, string_a);
                namePair pair_cs2 = new namePair(cs2, string_cs);
                namePair pair_fs22 = new namePair(fs22, string_fs);


                pairs.add(pair_f);
                pairs.add(pair_as1);
                pairs.add(pair_ds1);
                pairs.add(pair_gs1);
                pairs.add(pair_c1);
                pairs.add(pair_f11);
                pairs.add(pair_fs2);
                pairs.add(pair_b2);
                pairs.add(pair_e2);
                pairs.add(pair_a2);
                pairs.add(pair_cs2);
                pairs.add(pair_fs22);
            }
            //restart
            else{
                repopulate(savedInstanceState);
            }

            //if user selected sound, set up mediaplayers
            if(sound_bool){
                MediaPlayer mp_f1 = MediaPlayer.create(this, R.raw.f1);
                MediaPlayer mp_as1 = MediaPlayer.create(this, R.raw.as1);
                MediaPlayer mp_ds1 = MediaPlayer.create(this, R.raw.ds1);
                MediaPlayer mp_gs1 = MediaPlayer.create(this, R.raw.gs1);
                MediaPlayer mp_c1 = MediaPlayer.create(this, R.raw.c1);
                MediaPlayer mp_f11 = MediaPlayer.create(this, R.raw.f11);
                MediaPlayer mp_fs2 = MediaPlayer.create(this, R.raw.fs2);
                MediaPlayer mp_b2 = MediaPlayer.create(this, R.raw.b2);
                MediaPlayer mp_e2 = MediaPlayer.create(this, R.raw.e2);
                MediaPlayer mp_a2 = MediaPlayer.create(this, R.raw.a2);
                MediaPlayer mp_cs2 = MediaPlayer.create(this, R.raw.cs2);
                MediaPlayer mp_fs22 = MediaPlayer.create(this, R.raw.fs22);

                songMap.put(f1, mp_f1);
                songMap.put(as1, mp_as1);
                songMap.put(ds1, mp_ds1);
                songMap.put(gs1, mp_gs1);
                songMap.put(c1, mp_c1);
                songMap.put(f11, mp_f11);
                songMap.put(fs2, mp_fs2);
                songMap.put(b2, mp_b2);
                songMap.put(e2, mp_e2);
                songMap.put(a2, mp_a2);
                songMap.put(cs2, mp_cs2);
                songMap.put(fs22, mp_fs22);
            }
        }

        if(fretnos == 34){
            if(!lefty_bool) {
                overview.setImageResource(R.drawable.overview34);
            }
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview34_l);
            }
            if(savedInstanceState == null) {
                namePair pair_g = new namePair(g3, string_g);
                namePair pair_c = new namePair(c3, string_c);
                namePair pair_f = new namePair(f3, string_f);
                namePair pair_as = new namePair(as3, string_as);
                namePair pair_d = new namePair(d3, string_d);
                namePair pair_g1 = new namePair(g33, string_g);
                namePair pair_gs = new namePair(gs4, string_gs);
                namePair pair_cs = new namePair(cs4, string_cs);
                namePair pair_fs = new namePair(fs4, string_fs);
                namePair pair_b = new namePair(b4, string_b);
                namePair pair_ds = new namePair(ds4, string_ds);
                namePair pair_gs1 = new namePair(gs44, string_gs);

                pairs.add(pair_g);
                pairs.add(pair_c);
                pairs.add(pair_f);
                pairs.add(pair_as);
                pairs.add(pair_d);
                pairs.add(pair_g1);
                pairs.add(pair_gs);
                pairs.add(pair_cs);
                pairs.add(pair_fs);
                pairs.add(pair_b);
                pairs.add(pair_ds);
                pairs.add(pair_gs1);
            }
            else{
                repopulate(savedInstanceState);
            }

            if(sound_bool) {
                MediaPlayer mp_g3 = MediaPlayer.create(this, R.raw.g3);
                MediaPlayer mp_c3 = MediaPlayer.create(this, R.raw.c3);
                MediaPlayer mp_f3 = MediaPlayer.create(this, R.raw.f3);
                MediaPlayer mp_as3 = MediaPlayer.create(this, R.raw.as3);
                MediaPlayer mp_d3 = MediaPlayer.create(this, R.raw.d3);
                MediaPlayer mp_g33 = MediaPlayer.create(this, R.raw.g33);
                MediaPlayer mp_gs4 = MediaPlayer.create(this, R.raw.gs4);
                MediaPlayer mp_cs4 = MediaPlayer.create(this, R.raw.cs4);
                MediaPlayer mp_fs4 = MediaPlayer.create(this, R.raw.fs4);
                MediaPlayer mp_b4 = MediaPlayer.create(this, R.raw.b4);
                MediaPlayer mp_ds4 = MediaPlayer.create(this, R.raw.ds4);
                MediaPlayer mp_gs44 = MediaPlayer.create(this, R.raw.gs44);

                songMap.put(g3, mp_g3);
                songMap.put(c3, mp_c3);
                songMap.put(f3, mp_f3);
                songMap.put(as3, mp_as3);
                songMap.put(d3, mp_d3);
                songMap.put(g33, mp_g33);
                songMap.put(gs4, mp_gs4);
                songMap.put(cs4, mp_cs4);
                songMap.put(fs4, mp_fs4);
                songMap.put(b4, mp_b4);
                songMap.put(ds4, mp_ds4);
                songMap.put(gs44, mp_gs44);
            }
        }

        if(fretnos == 56) {
            if(!lefty_bool) {
                overview.setImageResource(R.drawable.overview56);
            }
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview56_l);
            }
            if(savedInstanceState == null) {
                namePair pair_a = new namePair(a5, string_a);
                namePair pair_d = new namePair(d5, string_d);
                namePair pair_g = new namePair(g5, string_g);
                namePair pair_c = new namePair(c5, string_c);
                namePair pair_e = new namePair(e5, string_e);
                namePair pair_a1 = new namePair(a55, string_a);
                namePair pair_as = new namePair(as6, string_as);
                namePair pair_ds = new namePair(ds6, string_ds);
                namePair pair_gs = new namePair(gs6, string_gs);
                namePair pair_cs = new namePair(cs6, string_cs);
                namePair pair_f = new namePair(f6, string_f);
                namePair pair_as1 = new namePair(as66, string_as);

                pairs.add(pair_a);
                pairs.add(pair_d);
                pairs.add(pair_g);
                pairs.add(pair_c);
                pairs.add(pair_e);
                pairs.add(pair_a1);
                pairs.add(pair_as);
                pairs.add(pair_ds);
                pairs.add(pair_gs);
                pairs.add(pair_cs);
                pairs.add(pair_f);
                pairs.add(pair_as1);
            }
            else{
                repopulate(savedInstanceState);
            }

            if(sound_bool) {
                MediaPlayer mp_a5 = MediaPlayer.create(this, R.raw.a5);
                MediaPlayer mp_d5 = MediaPlayer.create(this, R.raw.d5);
                MediaPlayer mp_g5 = MediaPlayer.create(this, R.raw.g5);
                MediaPlayer mp_c5 = MediaPlayer.create(this, R.raw.c5);
                MediaPlayer mp_e5 = MediaPlayer.create(this, R.raw.e5);
                MediaPlayer mp_a55 = MediaPlayer.create(this, R.raw.a55);
                MediaPlayer mp_as6 = MediaPlayer.create(this, R.raw.as6);
                MediaPlayer mp_ds6 = MediaPlayer.create(this, R.raw.ds6);
                MediaPlayer mp_gs6 = MediaPlayer.create(this, R.raw.gs6);
                MediaPlayer mp_cs6 = MediaPlayer.create(this, R.raw.cs6);
                MediaPlayer mp_f6 = MediaPlayer.create(this, R.raw.f6);
                MediaPlayer mp_as66 = MediaPlayer.create(this, R.raw.as66);

                songMap.put(a5, mp_a5);
                songMap.put(d5, mp_d5);
                songMap.put(g5, mp_g5);
                songMap.put(c5, mp_c5);
                songMap.put(e5, mp_e5);
                songMap.put(a55, mp_a55);
                songMap.put(as6, mp_as6);
                songMap.put(ds6, mp_ds6);
                songMap.put(gs6, mp_gs6);
                songMap.put(cs6, mp_cs6);
                songMap.put(f6, mp_f6);
                songMap.put(as66, mp_as66);
            }
        }

        if(fretnos == 78){
            if(!lefty_bool) {
                overview.setImageResource(R.drawable.overview78);
            }
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview78_l);
            }
            if(savedInstanceState == null) {
                namePair pair_b = new namePair(b7, string_b);
                namePair pair_e = new namePair(e7, string_e);
                namePair pair_a = new namePair(a7, string_a);
                namePair pair_d = new namePair(d7, string_d);
                namePair pair_fs = new namePair(fs7, string_fs);
                namePair pair_b1 = new namePair(b77, string_b);
                namePair pair_c = new namePair(c8, string_c);
                namePair pair_f = new namePair(f8, string_f);
                namePair pair_as = new namePair(as8, string_as);
                namePair pair_ds = new namePair(ds8, string_ds);
                namePair pair_g = new namePair(g8, string_g);
                namePair pair_c1 = new namePair(c88, string_c);

                pairs.add(pair_b);
                pairs.add(pair_e);
                pairs.add(pair_a);
                pairs.add(pair_d);
                pairs.add(pair_fs);
                pairs.add(pair_b1);
                pairs.add(pair_c);
                pairs.add(pair_f);
                pairs.add(pair_as);
                pairs.add(pair_ds);
                pairs.add(pair_g);
                pairs.add(pair_c1);
            }
            else{
                repopulate(savedInstanceState);
            }

            if(sound_bool) {
                MediaPlayer mp_b7 = MediaPlayer.create(this, R.raw.b7);
                MediaPlayer mp_e7 = MediaPlayer.create(this, R.raw.e7);
                MediaPlayer mp_a7 = MediaPlayer.create(this, R.raw.a7);
                MediaPlayer mp_d7 = MediaPlayer.create(this, R.raw.d7);
                MediaPlayer mp_fs7 = MediaPlayer.create(this, R.raw.fs7);
                MediaPlayer mp_b77 = MediaPlayer.create(this, R.raw.b77);
                MediaPlayer mp_c8 = MediaPlayer.create(this, R.raw.c8);
                MediaPlayer mp_f8 = MediaPlayer.create(this, R.raw.f8);
                MediaPlayer mp_as8 = MediaPlayer.create(this, R.raw.as8);
                MediaPlayer mp_ds8 = MediaPlayer.create(this, R.raw.ds8);
                MediaPlayer mp_g8 = MediaPlayer.create(this, R.raw.g8);
                MediaPlayer mp_c88 = MediaPlayer.create(this, R.raw.c88);

                songMap.put(b7, mp_b7);
                songMap.put(e7, mp_e7);
                songMap.put(a7, mp_a7);
                songMap.put(d7, mp_d7);
                songMap.put(fs7, mp_fs7);
                songMap.put(b77, mp_b77);
                songMap.put(c8, mp_c8);
                songMap.put(f8, mp_f8);
                songMap.put(as8, mp_as8);
                songMap.put(ds8, mp_ds8);
                songMap.put(g8, mp_g8);
                songMap.put(c88, mp_c88);
            }
        }

        if(fretnos == 911){
            if(!lefty_bool) {
                overview.setImageResource(R.drawable.overview911);
            }
            if(lefty_bool){
                overview.setImageResource(R.drawable.overview911_l);
            }
            if(savedInstanceState == null) {
                namePair pair_cs = new namePair(cs9, string_cs);
                namePair pair_fs = new namePair(fs9, string_fs);
                namePair pair_b = new namePair(b9, string_b);
                namePair pair_e = new namePair(e9, string_e);
                namePair pair_gs = new namePair(gs9, string_gs);
                namePair pair_cs1 = new namePair(cs99, string_cs);
                namePair pair_d = new namePair(d10, string_d);
                namePair pair_g = new namePair(g10, string_g);
                namePair pair_c = new namePair(c10, string_c);
                namePair pair_f = new namePair(f10, string_f);
                namePair pair_a = new namePair(a10, string_a);
                namePair pair_d1 = new namePair(d1010, string_d);
                namePair pair_ds = new namePair(ds11, string_ds);
                namePair pair_gs1 = new namePair(gs11, string_ds);
                namePair pair_cs3 = new namePair(cs11, string_cs);
                namePair pair_fs1 = new namePair(fs11, string_fs);
                namePair pair_as = new namePair(as11, string_as);
                namePair pair_ds1 = new namePair(ds1111, string_ds);



                pairs.add(pair_cs);
                pairs.add(pair_fs);
                pairs.add(pair_b);
                pairs.add(pair_e);
                pairs.add(pair_gs);
                pairs.add(pair_cs1);
                pairs.add(pair_d);
                pairs.add(pair_g);
                pairs.add(pair_c);
                pairs.add(pair_f);
                pairs.add(pair_a);
                pairs.add(pair_d1);
                pairs.add(pair_ds);
                pairs.add(pair_gs1);
                pairs.add(pair_cs3);
                pairs.add(pair_fs1);
                pairs.add(pair_as);
                pairs.add(pair_ds1);
            }
            else{
                repopulate(savedInstanceState);
            }

            if(sound_bool) {
                MediaPlayer mp_cs9 = MediaPlayer.create(this, R.raw.cs9);
                MediaPlayer mp_fs9 = MediaPlayer.create(this, R.raw.fs9);
                MediaPlayer mp_b9 = MediaPlayer.create(this, R.raw.b9);
                MediaPlayer mp_e9 = MediaPlayer.create(this, R.raw.e9);
                MediaPlayer mp_gs9 = MediaPlayer.create(this, R.raw.gs9);
                MediaPlayer mp_cs99 = MediaPlayer.create(this, R.raw.cs99);
                MediaPlayer mp_d10 = MediaPlayer.create(this, R.raw.d10);
                MediaPlayer mp_g10 = MediaPlayer.create(this, R.raw.g10);
                MediaPlayer mp_c10 = MediaPlayer.create(this, R.raw.c10);
                MediaPlayer mp_f10 = MediaPlayer.create(this, R.raw.f10);
                MediaPlayer mp_a10 = MediaPlayer.create(this, R.raw.a10);
                MediaPlayer mp_d1010 = MediaPlayer.create(this, R.raw.d1010);
                MediaPlayer mp_ds11 = MediaPlayer.create(this, R.raw.ds11);
                MediaPlayer mp_gs11 = MediaPlayer.create(this, R.raw.gs11);
                MediaPlayer mp_cs11 = MediaPlayer.create(this, R.raw.cs11);
                MediaPlayer mp_fs11 = MediaPlayer.create(this, R.raw.fs11);
                MediaPlayer mp_as11 = MediaPlayer.create(this, R.raw.as11);
                MediaPlayer mp_ds1111 = MediaPlayer.create(this, R.raw.ds1111);

                songMap.put(cs9, mp_cs9);
                songMap.put(fs9, mp_fs9);
                songMap.put(b9, mp_b9);
                songMap.put(e9, mp_e9);
                songMap.put(gs9, mp_gs9);
                songMap.put(cs99, mp_cs99);
                songMap.put(d10, mp_d10);
                songMap.put(g10, mp_g10);
                songMap.put(c10, mp_c10);
                songMap.put(f10, mp_f10);
                songMap.put(a10, mp_a10);
                songMap.put(d1010, mp_d1010);
                songMap.put(ds11, mp_ds11);
                songMap.put(gs11, mp_gs11);
                songMap.put(cs11, mp_cs11);
                songMap.put(fs11, mp_fs11);
                songMap.put(as11, mp_as11);
                songMap.put(ds1111, mp_ds1111);
            }
        }


        if(savedInstanceState == null) {
            Collections.shuffle(pairs, new Random(System.nanoTime()));
            total = pairs.size();

        }

        ImageView imgview = (ImageView) findViewById(R.id.ImageView);
        imgview.setImageResource(pairs.get(0).getleft());

        //play first note's sound
        if(!state && sound_bool){
            songMap.get(pairs.get(0).getleft()).start();
        }

        Button submit_next_btn = (Button) findViewById(R.id.next_button);
        submit_next_btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Button next_submit_view = (Button)findViewById(R.id.next_button);

                //work as the submit button
                if(!submit_next){
                    submit_next = true;
                    next_submit_view.setText("Next");
                    check_value(songMap.get(pairs.get(0).getleft()));
                }
                //work as next button
                else {
                    submit_next = false;
                    //remove previous note from pairs
                    pairs.remove(0);
                    //if done, clean up and go to end screen
                    if(pairs.size() == 0){
                        //release mediaplayers
                        if(sound_bool) {
                            for(int key : songMap.keySet()){
                                songMap.get(key).release();
                            }
                        }

                        //go to end screen
                        Intent end_intent = new Intent(v.getContext(), End.class);
                        end_intent.putExtra("score_value", score);
                        end_intent.putExtra("total_value", total);
                        end_intent.putExtra("fretnos", fretnos);
                        startActivity(end_intent);
                        finish();

                        //next.setText("Return to Main Menu");
                    }
                    //if not done, continue
                    else {
                        next_submit_view.setText("Submit");
                        next(songMap.get(pairs.get(0).getleft()));
                    }
                }
            }
        });

    }
    /*@Override
    public void onStart(){
        super.onStart();
        if(!state && pairs.size() != 0) {
            if(sound_bool) {
                songMap.get(pairs.get(0).getleft()).start();
            }
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //array list for use with bundles and recovering/saving data. Check onSavedInstanceState and
        //repopulate functions
        ArrayList<Integer> img_list = new ArrayList<>();
        //Take all images remaining and put ids in arraylist, to be saved
        for(int i = 0; i < pairs.size(); ++i){
            img_list.add(i, pairs.get(i).getleft());
        }
        outState.putIntegerArrayList("images", img_list);
        outState.putInt("score", score);
        outState.putInt("total", total);
        outState.putInt("count", count);
        super.onSaveInstanceState(outState);
    }

    //used to repopulate vectors and maps if onCreate must be called again
    public void repopulate(Bundle savedInstanceState){
        //set scorekeeping
        score = savedInstanceState.getInt("score");
        total = savedInstanceState.getInt("total");
        count = savedInstanceState.getInt("count");

        ArrayList<Integer> img_list = new ArrayList<>();
        img_list = savedInstanceState.getIntegerArrayList("images");
        //repopulate image database
        pairs.clear();
        for(int i = 0; i < img_list.size(); ++i){
            Integer img = img_list.get(i);
            String imgname = nameMap.get(img);
            pairs.add(i, new namePair(img, imgname));
        }

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
    public void check_value(MediaPlayer m){
        if(note_input.equals("NONE") || note_input.equals("")){
        }

        else{
            String correct_value = pairs.get(0).getright();
            //View submit = findViewById(R.id.submit_button);
            TextView textview = (TextView)findViewById(R.id.input_text);
            //FOR DEBUG PURPOSES
            //TextView test = (TextView)findViewById(R.id.test);

            //Button next = (Button) findViewById(R.id.next_button);

            //remove submit button and add next button
            //submit.setVisibility(View.GONE);
            //next.setVisibility(View.VISIBLE);

            //for accidentals
            if(note_input.length() > 1){
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
                    //put the card at the back of the deck
                    namePair pairIncorrect = pairs.get(0);
                    pairs.add(pairIncorrect);
                }
            }
            //for diatonic
            else {
                //if user is correct
                if(note_input.equals(pairs.get(0).getright())){
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
                    //put the card at the back of the deck
                    namePair pairIncorrect = pairs.get(0);
                    pairs.add(pairIncorrect);
                }

            }

        }

    }

    //prepare and execute next note, called when NEXT button is clicked
    public void next(MediaPlayer m){
        TextView textview = (TextView)findViewById(R.id.input_text);
        TextView textcorrect = (TextView)findViewById(R.id.correct_text);


        //View submit = findViewById(R.id.submit_button);
        //View next = findViewById(R.id.next_button);
        ImageView imgview = (ImageView) findViewById(R.id.ImageView);


        //remove next button and put up submit button
        //next.setVisibility(View.GONE);
        //submit.setVisibility(View.VISIBLE);

        //if not, put up next pair
        imgview.setImageResource(pairs.get(0).getleft());
        //play next note
        if(sound_bool) {
            m.start();
        }
        //clear variables
        note_input = "";
        textview_text = "";
        //clear textview
        textview.setText("");
        textview.setBackgroundResource(R.drawable.border_tan);
        textcorrect.setText("");
        ++count;

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



}
