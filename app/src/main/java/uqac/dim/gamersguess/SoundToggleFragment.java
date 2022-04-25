package uqac.dim.gamersguess;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import uqac.dim.gamersguess.persistance.VolumeSingleton;

public class SoundToggleFragment extends Fragment {

    AudioManager audioManager;
    Vibrator vibrator;
    VolumeSingleton appVolumeControl;
    SwitchCompat muteButton;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.sound_toggle_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        appVolumeControl = VolumeSingleton.getInstance();
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        muteButton = (SwitchCompat) getView().findViewById(R.id.mute_button);
        image = (ImageView) getView().findViewById(R.id.mute_image);

        muteButton.setChecked(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0);

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlVolume();
            }
        });
    }

    public void controlVolume() {
        if (appVolumeControl.getMute()) {
            Log.i("DIM", "Unmuted app");

            appVolumeControl.setMute(false);
            int volume = appVolumeControl.getSavedVolume();
            if (volume == 0)
                volume = 5;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
            image.setBackgroundResource(R.drawable.soundon_icon);
            muteButton.setChecked(true);
        } else {
            Log.i("DIM", "Muted app");

            appVolumeControl.setMute(true);
            appVolumeControl.setSavedVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_SHOW_UI);
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            image.setBackgroundResource(R.drawable.soundoff_icon);
            muteButton.setChecked(false);
        }
    }
}
