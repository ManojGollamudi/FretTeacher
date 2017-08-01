package com.manojgollamudi.fretteacher;


/**
 * Created by mgollamudi on 7/9/17.
 */

public class namePair {
    private Integer left;
    private String right;
    public namePair(Integer left_in, String right_in){
        this.left = left_in;
        this.right = right_in;
    }
    public Integer getleft(){ return left; }
    public String getright(){ return right; }

    public void setleft(Integer left_in) {
        this.left = left_in; }

    public void setright(String right_in){
        this.right = right_in; }
}
