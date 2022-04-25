package uqac.dim.gamersguess;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficultychoice);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Button easyButton = (Button)findViewById(R.id.Button_easy);
        Button mediumButton = (Button)findViewById(R.id.Button_medium);
        Button hardButton = (Button)findViewById(R.id.Button_hard);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Easy difficulty");
                startQuiz("f");
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Medium difficulty");
                startQuiz("m");
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Hard difficulty");
                startQuiz("d");
            }
        });
    }

    private void startQuiz(String difficulty) {
        Intent intent = new Intent(DifficultyChoiceActivity.this, QuizActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
}