package com.manojgollamudi.fretteacher;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;


/**
 * Created by mgollamudi on 9/10/17.
 */

public class flashcards_instructions extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.flashcards_instructions_layout, null));

        return builder.create();
    }



}
