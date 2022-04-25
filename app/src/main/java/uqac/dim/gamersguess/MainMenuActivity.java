package uqac.dim.gamersguess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Button playButton = (Button)findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("DIM", "Play game");
                startActivity(new Intent(MainMenuActivity.this, DifficultyChoiceActivity.class));
            }
        });

        ImageButton leaderboardButton = (ImageButton)findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "View leaderboard");
                startActivity(new Intent(MainMenuActivity.this, LeaderboardActivity.class));
            }
        });

        ImageButton optionsButton = (ImageButton)findViewById(R.id.options_button);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Settings menu");
                DialogFragment optionsFragment = new SettingsDialog();
                optionsFragment.show(getSupportFragmentManager(), "options");
            }
        });
    }
}