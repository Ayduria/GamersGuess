package uqac.dim.gamersguess;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import org.w3c.dom.Text;

import java.util.List;

import uqac.dim.gamersguess.persistance.Question;
import uqac.dim.gamersguess.persistance.QuizBD;
import uqac.dim.gamersguess.persistance.Score;

public class QuizResultActivity extends AppCompatActivity {

    private QuizBD bd;

    int finalScore;
    int bestScore;
    String difficulty;
    EditText nameInput;
    String playerName;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresult);

        bd = QuizBD.getDatabase(getApplicationContext());
        Bundle b = getIntent().getExtras();
        finalScore = b.getInt("score");
        difficulty = b.getString("difficulte");

        TextView score = (TextView)findViewById(R.id.final_score);
        TextView bestScoreDisplay = (TextView)findViewById(R.id.highscore);
        nameInput = (EditText)findViewById(R.id.input_name);
        submitButton = (Button)findViewById(R.id.submit_score);

        score.setText(String.valueOf(finalScore));

        // Check for highscore
        Score highScore = bd.quizDao().getHighScore();

        if (highScore == null)
            newHighScore();
        else {
            bestScore = highScore.score;
            if (finalScore > bestScore)
                newHighScore();
            else {
                hideNameInput();
            }
        }

        String highScoreDisplay = getResources().getString(R.string.highScore) + " " + String.valueOf(bestScore);
        bestScoreDisplay.setText(highScoreDisplay);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameInput.getText())) {
                    nameInput.setError("Votre nom est requis !");
                } else {
                    Log.i("DIM", "New highscore submitted");
                    addNewScore();
                    hideNameInput();
                    findViewById(R.id.score_submitted).setVisibility(View.VISIBLE);
                }
            }
        });

        Button menuButton = (Button)findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Back to menu");
                finish();
            }
        });
    }

    private void hideNameInput() {
        nameInput.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
    }

    private void newHighScore() {
        TextView announcement = (TextView)findViewById(R.id.score_txt);
        bestScore = finalScore;
        announcement.setTextColor(Color.RED);
        announcement.setText(getResources().getString(R.string.newHighscore));
    }

    private void addNewScore() {
        playerName = nameInput.getText().toString();
        Score score = new Score(finalScore, playerName, difficulty);
        bd.quizDao().addScore(score);
    }

    @Override
    protected void onDestroy() {
        QuizBD.destroyInstance();
        super.onDestroy();
    }
}
