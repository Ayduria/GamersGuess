package uqac.dim.gamersguess;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.stream.Stream;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SettingsActivity extends DialogFragment {

    AudioManager audioManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.activity_settings, null));

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        ImageButton closeButton = (ImageButton) getDialog().findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "settings menu");

                dismiss();
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

        /*ToggleButton toggle = (ToggleButton) getDialog().findViewById(R.id.toggleMusic);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //tests
                    //goodAnswerSound.start();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                } else {
                    //tests
                    //goodAnswerSound.start();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10,0);
                }

            }
        });*/

    }
}