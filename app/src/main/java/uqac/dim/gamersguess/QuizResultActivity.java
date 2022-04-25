package uqac.dim.gamersguess;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.KonfettiView;
//import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.models.Shape;
//import nl.dionsegijn.konfetti.xml.KonfettiView;
import uqac.dim.gamersguess.persistance.Question;
import uqac.dim.gamersguess.persistance.QuizBD;
import uqac.dim.gamersguess.persistance.Score;

public class QuizResultActivity extends AppCompatActivity {

    private QuizBD bd;
    private KonfettiView celebrationView;

    int finalScore;
    int bestScore;
    String difficulty;
    EditText nameInput;
    String playerName;
    Button submitButton;

    MediaPlayer errorSound;
    MediaPlayer confirmSound;
    MediaPlayer victorySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresult);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Sounds
        errorSound = MediaPlayer.create(this, R.raw.denied_sound);
        confirmSound = MediaPlayer.create(this, R.raw.confirm_sound);
        victorySound = MediaPlayer.create(this, R.raw.victory_sound);

        //Confettis
        celebrationView = findViewById(R.id.celebrationView);

        bd = QuizBD.getDatabase(getApplicationContext());
        Bundle b = getIntent().getExtras();
        finalScore = b.getInt("score");
        difficulty = b.getString("difficulte");

        TextView score = (TextView) findViewById(R.id.final_score);
        TextView bestScoreDisplay = (TextView) findViewById(R.id.highscore);
        nameInput = (EditText) findViewById(R.id.input_name);
        submitButton = (Button) findViewById(R.id.submit_score);

        score.setText(String.valueOf(finalScore));

        // Check for highscore
        Score highScore = bd.quizDao().getHighScore();

        if (highScore == null)
            newHighScore();
        else {
            bestScore = highScore.score;
            if (finalScore > bestScore)
                newHighScore();
        }

        String highScoreDisplay = getResources().getString(R.string.highScore) + " " + String.valueOf(bestScore);
        bestScoreDisplay.setText(highScoreDisplay);

        // Submit highscore to leaderboard
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameInput.getText())) {
                    errorSound.start();
                    nameInput.setError("Votre nom est requis !");
                } else {
                    Log.i("DIM", "New highscore submitted");
                    addNewScore();
                    nameInput.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);
                    findViewById(R.id.score_submitted).setVisibility(View.VISIBLE);
                    confirmSound.start();
                }
            }
        });

        // Button listeners
        ImageButton leaderboardButton = (ImageButton) findViewById(R.id.leaderboard_button2);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Display leaderboard");
                startActivity(new Intent(QuizResultActivity.this, LeaderboardActivity.class));
            }
        });

        ImageButton replayButton = (ImageButton) findViewById(R.id.replay_button);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Restart quiz");
                restartQuiz();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Back to menu");
                finish();
            }
        });
    }

    private void newHighScore() {
        Log.i("DIM", "New highscore");
        TextView announcement = (TextView) findViewById(R.id.score_txt);
        bestScore = finalScore;
        announcement.setTextColor(Color.RED);
        announcement.setText(getResources().getString(R.string.newHighscore));

        victorySound.start();

        // Funfettis for highscore
        celebrationView.build()
                .addColors(Color.BLUE, Color.GREEN)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .setPosition(-50, celebrationView.getWidth() + 50f, -50f, -50f)
                .streamFor(200, 1000L);
    }

    private void addNewScore() {
        playerName = nameInput.getText().toString();
        Score score = new Score(finalScore, playerName, difficulty);
        bd.quizDao().addScore(score);
    }

    private void restartQuiz() {
        Intent intent = new Intent(QuizResultActivity.this, QuizActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        QuizBD.destroyInstance();
        super.onDestroy();
    }


}
