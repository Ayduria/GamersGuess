package uqac.dim.gamersguess;

import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import java.util.ArrayList;
import java.util.List;

import uqac.dim.gamersguess.persistance.QuizBD;
import uqac.dim.gamersguess.persistance.Score;

public class LeaderboardActivity extends AppCompatActivity {

    private QuizBD bd;
    List<Score> scores;
    TableLayout leaderboard;
    String difficulty;
    int rank = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        bd = QuizBD.getDatabase(getApplicationContext());
        scores = bd.quizDao().getAllScores();

        leaderboard = (TableLayout)findViewById(R.id.leaderboard);

        // Check if there are scores
        if (!scores.isEmpty())
            displayScores();
        else
            displayNoScoreMessage();

        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayScores() {
        Log.i("DIM", "Display ranked scores");

        for (Score score : scores) {

            // Get difficulty full string
            switch (score.difficulte) {
                case "f":   difficulty = "Facile";
                    break;
                case "m":   difficulty = "Intermédiaire";
                    break;
                case "d":   difficulty = "Difficile";
                    break;
            }

            // Add row for score
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));

            TextView playerRank = new TextView(this);
            playerRank.setText(String.valueOf(rank));
            TextViewCompat.setTextAppearance(playerRank, R.style.Theme_GamersGuess_leaderboardItem);
            tr.addView(playerRank);

            TextView playerScore = new TextView(this);
            playerScore.setText(String.valueOf(score.score));
            TextViewCompat.setTextAppearance(playerScore, R.style.Theme_GamersGuess_leaderboardItem);
            tr.addView(playerScore);

            TextView playerName = new TextView(this);
            playerName.setText(score.nom);
            TextViewCompat.setTextAppearance(playerName, R.style.Theme_GamersGuess_leaderboardItem);
            tr.addView(playerName);

            TextView gameDifficulty = new TextView(this);
            gameDifficulty.setText(difficulty);
            TextViewCompat.setTextAppearance(gameDifficulty, R.style.Theme_GamersGuess_leaderboardItem);
            tr.addView(gameDifficulty);

            List<TextView> columns = new ArrayList<TextView>();
            columns.add(playerRank);
            columns.add(playerScore);
            columns.add(playerName);
            columns.add(gameDifficulty);

            // Top rank colors
            switch (rank) {
                case 1:     for (TextView textView : columns)
                            textView.setTextColor(ContextCompat.getColor(this, R.color.gold));
                            break;
                case 2:     for (TextView textView : columns)
                            textView.setTextColor(ContextCompat.getColor(this, R.color.silver));
                            break;
                case 3:     for (TextView textView : columns)
                            textView.setTextColor(ContextCompat.getColor(this, R.color.bronze));
                            break;
                default:    for (TextView textView : columns)
                            textView.setTextColor(Color.WHITE);
                            break;
            }

            leaderboard.addView(tr, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT));

            rank++;
        }
    }

    private void displayNoScoreMessage() {
        Log.i("DIM", "No scores to display");

        TableRow header = (TableRow)findViewById(R.id.leaderboardHeader);
        header.setVisibility(View.GONE);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));

        TextView noScore = new TextView(this);
        noScore.setText(getResources().getString(R.string.noScore));
        TextViewCompat.setTextAppearance(noScore, R.style.Theme_GamersGuess_leaderboardItem);
        noScore.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tr.addView(noScore);

        leaderboard.addView(tr, new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT));
    }


    @Override
    protected void onDestroy() {
        QuizBD.destroyInstance();
        super.onDestroy();
    }
}