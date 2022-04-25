package uqac.dim.gamersguess;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import androidx.fragment.app.FragmentTransaction;

public class SettingsDialog extends DialogFragment {

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

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.volume_fragment, new SoundToggleFragment());
        ft.commit();

        ImageButton closeButton = (ImageButton) getDialog().findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Settings menu");
                ((MainMenuActivity)getActivity()).closeSettings();
                dismiss();
            }
        });
    }
}