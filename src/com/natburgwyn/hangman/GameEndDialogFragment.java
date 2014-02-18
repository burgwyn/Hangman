package com.natburgwyn.hangman;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GameEndDialogFragment extends DialogFragment {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_end_confirm, container, false);
        
        ((Button)v.findViewById(R.id.btnNo)).setOnClickListener(listenerNo);
        ((Button)v.findViewById(R.id.btnYes)).setOnClickListener(listenerYes);
        return v;
    }
    
    private View.OnClickListener listenerNo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    
    private View.OnClickListener listenerYes = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	((IGameEndListener)getActivity()).handleGameEnd();
            dismiss();
        }
    };
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(getString(R.string.dialog_game_end_title));
        return d;
    }
}
