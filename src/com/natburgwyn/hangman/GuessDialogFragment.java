package com.natburgwyn.hangman;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class GuessDialogFragment extends DialogFragment {
	
	private EditText txtGuess;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guess, container, false);
        
        txtGuess = (EditText)v.findViewById(R.id.txtGuess);
        
        ((Button)v.findViewById(R.id.btnCancel)).setOnClickListener(listenerCancel);
        ((Button)v.findViewById(R.id.btnGuess)).setOnClickListener(listenerGuess);
        return v;
    }
    
    private View.OnClickListener listenerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    
    private View.OnClickListener listenerGuess = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	if (txtGuess.getText().toString().length() > 0){
        		((IGuessListener)getActivity()).handleGuess(txtGuess.getText().toString());
        	}
            dismiss();
        }
    };
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(getString(R.string.dialog_guess_title));
        return d;
    }
}
