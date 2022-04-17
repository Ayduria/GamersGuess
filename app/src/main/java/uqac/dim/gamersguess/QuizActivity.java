package uqac.dim.gamersguess;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uqac.dim.gamersguess.persistance.Question;
import uqac.dim.gamersguess.persistance.QuizBD;
import uqac.dim.gamersguess.persistance.Reponse;
import uqac.dim.gamersguess.persistance.Score;

public class QuizActivity extends AppCompatActivity {

    private QuizBD bd;
    String difficulty;

    List<Question> questions;
    Question question;
    TextView questionDisplay;
    List<Button> reponsesBtn;

    int questionIndex = 0;

    int ptsTotal = 0;
    int comboPtsMultiplier = 1;
    int difficultyPtsMultiplier;

    MediaPlayer goodAnswerSound;
    MediaPlayer wrongAnswerSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get sounds
        goodAnswerSound = MediaPlayer.create(this,R.raw.good_answer);
        wrongAnswerSound = MediaPlayer.create(this,R.raw.wrong_answer);

        // Get question field and answer buttons
        questionDisplay = (TextView)findViewById(R.id.question);
        Button reponse1 = (Button)findViewById(R.id.reponse1);
        Button reponse2 = (Button)findViewById(R.id.reponse2);
        Button reponse3 = (Button)findViewById(R.id.reponse3);
        Button reponse4 = (Button)findViewById(R.id.reponse4);
        reponsesBtn = Arrays.asList(reponse1, reponse2, reponse3, reponse4);

        // Get all questions for selected difficulty
        bd = QuizBD.getDatabase(getApplicationContext());
        Bundle b = getIntent().getExtras();
        difficulty = b.getString("difficulty");

        switch(difficulty) {
            case "f":   questions = bd.quizDao().getEasyQuestions();
                        difficultyPtsMultiplier = 1;
                break;
            case "m":   questions = bd.quizDao().getMediumQuestions();
                        difficultyPtsMultiplier = 2;
                break;
            case "d":   questions = bd.quizDao().getHardQuestions();
                        difficultyPtsMultiplier = 3;
                break;
        }

        // Random order
        Collections.shuffle(questions);

        // Display first question and answers
        displayQuestion();

        // Managing answer buttons clicks
        for(Button button : reponsesBtn) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("DIM", "Answer chosen");

                    for(Button button : reponsesBtn)
                        button.setClickable(false);

                    manageAnswer(button);
                }
            });
        }

        // Pause button
        ImageButton pauseButton = (ImageButton)findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Paused game");

            startActivity(new Intent(QuizActivity.this, PauseMenuActivity.class));
            }
        });
    }

    private void displayQuestion() {

        Log.i("DIM", "New question displayed");

        // Question
        question = questions.get(questionIndex);
        questionDisplay.setText(question.question);

        // Answers
        List<Reponse> reponses = bd.quizDao().loadAllQuestionAnswers(question.id);

        int noReponse = 0;
        for(Button button : reponsesBtn) {
            button.setText(reponses.get(noReponse).choixReponse);
            button.setTag(reponses.get(noReponse));
            button.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.defaultBtn), PorterDuff.Mode.MULTIPLY));
            button.setTextColor(Color.parseColor("#000000"));
            button.setClickable(true);
            noReponse++;
        }
    }

    private void manageAnswer(Button clickedButton) {

        Reponse chosenAnswer = (Reponse)clickedButton.getTag();
        int validAnswer = chosenAnswer.bonneReponse;

        clickedButton.setTextColor(Color.parseColor("#ffffff"));

        if (validAnswer == 1) {
            Log.i("DIM", "Good answer");
            goodAnswerSound.start();
            clickedButton.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.goodAnswerBtn), PorterDuff.Mode.MULTIPLY));
            ptsTotal += 5 * difficultyPtsMultiplier * comboPtsMultiplier;
            comboPtsMultiplier++;

        } else {
            Log.i("DIM", "Bad answer");
            wrongAnswerSound.start();
            Button rightButton = getGoodAnswerButton();
            clickedButton.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.badAnswerBtn), PorterDuff.Mode.MULTIPLY));
            rightButton.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.goodAnswerBtn), PorterDuff.Mode.MULTIPLY));
            rightButton.setTextColor(Color.parseColor("#ffffff"));
            comboPtsMultiplier = 1;
        }

        questionIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (questionIndex < questions.size())
                    displayQuestion();
                else
                    displayResult();
            }
        }, 800);

    }

    private Button getGoodAnswerButton() {
        Button goodButton = null;

        for(Button button : reponsesBtn) {
            Reponse reponse = (Reponse)button.getTag();
            if(reponse.bonneReponse == 1)
                goodButton = button;
        }

        return goodButton;
    }

    private void displayResult() {
        Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
        intent.putExtra("score", ptsTotal);
        intent.putExtra("difficulte", difficulty);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Voulez-vous vraiment arrêter le quiz?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        QuizBD.destroyInstance();
        super.onDestroy();
    }
}
