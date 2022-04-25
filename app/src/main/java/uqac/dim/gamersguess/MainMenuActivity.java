package uqac.dim.gamersguess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    DialogFragment settingsDialog;
    MediaPlayer popupSound;
    MediaPlayer closeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        settingsDialog = new SettingsDialog();

        popupSound = MediaPlayer.create(this, R.raw.pause_sound);
        closeSound = MediaPlayer.create(this, R.raw.unpause_sound);

        Button playButton = (Button)findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("DIM", "Play game");
                playButton.startAnimation(buttonClick);
                startActivity(new Intent(MainMenuActivity.this, DifficultyChoiceActivity.class));
            }
        });

        ImageButton leaderboardButton = (ImageButton)findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "View leaderboard");
                leaderboardButton.startAnimation(buttonClick);
                startActivity(new Intent(MainMenuActivity.this, LeaderboardActivity.class));
            }
        });

        ImageButton optionsButton = (ImageButton)findViewById(R.id.options_button);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Settings menu");
                optionsButton.startAnimation(buttonClick);
                popupSound.start();
                settingsDialog.show(getSupportFragmentManager(), "options");
            }
        });
    }

    public void closeSettings() {
        closeSound.start();
    }
}