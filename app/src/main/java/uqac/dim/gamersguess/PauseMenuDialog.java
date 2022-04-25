package uqac.dim.gamersguess;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PauseMenuDialog extends DialogFragment {

    AudioManager audioManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_pausemenu, null));

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);

        ImageButton closeButton = (ImageButton) getDialog().findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuizActivity)getActivity()).resumeGame();
                dismiss();
            }
        });

        ImageButton homeButton = (ImageButton) getDialog().findViewById(R.id.home_button2);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Back to menu");

                getActivity().finish();
            }
        });


        // Sets physical volume to app volume
        getDialog().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //tests
        audioManager = (AudioManager) getDialog().getContext().getSystemService(Context.AUDIO_SERVICE);

        // Sound ON
        ImageButton soundOnButton = (ImageButton) getDialog().findViewById(R.id.soundON_button);
        soundOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Sound on value 50");

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10,0);
            }
        });

        // Sound OFF
        ImageButton soundOffButton = (ImageButton) getDialog().findViewById(R.id.soundOff_button);
        soundOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Sound off value 0");

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            }
        });

    }
}