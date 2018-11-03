package com.manojgollamudi.fretteacher;


import android.media.MediaPlayer;

/**
 * Created by mgollamudi on 7/9/17.
 */

public class note_info {
    private float x;
    private float y;
    private String name;
    private MediaPlayer mp;
    private int fretnumber;
    public note_info(float x_in, float y_in, String name_in, MediaPlayer mp_in, int fretnumber_in){
        this.x = x_in;
        this.y = y_in;
        this.name = name_in;
        this.mp = mp_in;
        this.fretnumber = fretnumber_in;
    }

    public float get_x(){
        return x;
    }
    public float get_y(){
        return y;
    }
    public String get_name(){
        return name;
    }
    public MediaPlayer get_mp(){
        return mp;
    }
    public int get_fretnumber(){
        return fretnumber;
    }
}
