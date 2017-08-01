package com.manojgollamudi.fretteacher;

import android.media.MediaPlayer;

/**
 * Created by mgollamudi on 7/21/17.
 */

    public class songPair {
        private int left;
        private MediaPlayer right;
        public songPair(int left_in, MediaPlayer right_in){
            this.left = left_in;
            this.right = right_in;
        }
        public int getleft(){ return left; }
        public MediaPlayer getright(){ return right; }

        public void setleft(int left_in) {
            this.left = left_in; }

        public void setright(MediaPlayer right_in){
            this.right = right_in; }
    }


