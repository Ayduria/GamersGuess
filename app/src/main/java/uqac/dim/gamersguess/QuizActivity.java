package uqac.dim.gamersguess;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uqac.dim.gamersguess.persistance.Question;
import uqac.dim.gamersguess.persistance.QuizBD;
import uqac.dim.gamersguess.persistance.Reponse;

public class QuizActivity extends AppCompatActivity {

    DialogFragment pauseDialog;

    // Questions and answers
    private QuizBD bd;
    String difficulty;
    List<Question> questions;
    Question question;
    TextView questionDisplay;
    List<Button> reponsesBtn;
    int questionIndex = 0;

    // Score calculations
    int ptsTotal = 0;
    int comboPtsMultiplier = 1;
    int difficultyPtsMultiplier;
    int timeMultiplier;

    // Sounds
    MediaPlayer goodAnswerSound;
    MediaPlayer wrongAnswerSound;
    MediaPlayer timesUpSound;
    MediaPlayer pauseSound;
    MediaPlayer unpauseSound;
    MediaPlayer tickSound;

    // Timer
    private TextView qTimer;
    private static final long timeLengthMilli = 11000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = timeLengthMilli;
    public boolean pauseVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        pauseDialog = new PauseMenuDialog();

        // Get timer
        qTimer = (TextView) findViewById(R.id.timer);

        // Get sounds
        goodAnswerSound = MediaPlayer.create(this,R.raw.good_answer);
        wrongAnswerSound = MediaPlayer.create(this,R.raw.wrong_answer);
        timesUpSound = MediaPlayer.create(this, R.raw.times_up_sound);
        pauseSound = MediaPlayer.create(this, R.raw.pause_sound);
        unpauseSound = MediaPlayer.create(this, R.raw.unpause_sound);
        tickSound = MediaPlayer.create(this, R.raw.tick_sound);

        // Get question field and answer buttons
        questionDisplay = (TextView)findViewById(R.id.question);
        reponsesBtn = Arrays.asList(
                (Button)findViewById(R.id.reponse1),
                (Button)findViewById(R.id.reponse2),
                (Button)findViewById(R.id.reponse3),
                (Button)findViewById(R.id.reponse4));

        // Get all questions for selected difficulty
        bd = QuizBD.getDatabase(getApplicationContext());
        Bundle b = getIntent().getExtras();
        difficulty = b.getString("difficulty");

        switch(difficulty) {
            case "f":   questions = bd.quizDao().getEasyQuestions();
                        difficultyPtsMultiplier = 1;
                        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.easy));
                break;
            case "m":   questions = bd.quizDao().getMediumQuestions();
                        difficultyPtsMultiplier = 2;
                        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.medium));
                break;
            case "d":   questions = bd.quizDao().getHardQuestions();
                        difficultyPtsMultiplier = 3;
                        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.hard));
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
                pauseGame();
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

        //Timer
        startTimer(timeLengthMilli);
        updateCountDownText();
        qTimer.setTextColor(Color.WHITE);
    }

    private void manageAnswer(Button clickedButton) {

        Reponse chosenAnswer = (Reponse)clickedButton.getTag();
        int validAnswer = chosenAnswer.bonneReponse;

        clickedButton.setTextColor(Color.parseColor("#ffffff"));

        if (validAnswer == 1) {
            Log.i("DIM", "Good answer");
            goodAnswerSound.start();
            clickedButton.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.goodAnswerBtn), PorterDuff.Mode.DARKEN));
            timeMultiplier = (int)(mTimeLeftInMillis/1000);
            if (timeMultiplier == 0)
                timeMultiplier = 1;
            ptsTotal += 5 * difficultyPtsMultiplier * comboPtsMultiplier * timeMultiplier;
            comboPtsMultiplier++;

        } else {
            Log.i("DIM", "Bad answer");
            wrongAnswerSound.start();
            clickedButton.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.badAnswerBtn), PorterDuff.Mode.DARKEN));
            showGoodAnswer();
            comboPtsMultiplier = 1;
        }

        questionIndex++;
        pauseTimer();
        delayedChange();
    }

    private void delayedChange() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (questionIndex < questions.size())
                    displayQuestion();
                else
                    displayResult();
            }

        }, 800);
        resetTimer();
    }

    private void showGoodAnswer() {
        Button goodButton = null;

        for(Button button : reponsesBtn) {
            Reponse reponse = (Reponse)button.getTag();
            if(reponse.bonneReponse == 1)
                goodButton = button;
        }

        goodButton.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.goodAnswerBtn), PorterDuff.Mode.MULTIPLY));
        goodButton.setTextColor(Color.parseColor("#ffffff"));
    }

    private void startTimer(long timeLengthMilli){
        mCountDownTimer = new CountDownTimer(timeLengthMilli,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               mTimeLeftInMillis = millisUntilFinished;
               updateCountDownText();

               if (mTimeLeftInMillis/1000 < 1) {
                   onFinish();
               }
            }

            @Override
            public void onFinish() {
                timesUpSound.start();
                qTimer.setTextColor(Color.RED);

                for(Button button : reponsesBtn)
                    button.setClickable(false);

                showGoodAnswer();

                questionIndex++;
                mTimerRunning = false;
                mCountDownTimer.cancel();
                delayedChange();
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText(){
        if ((mTimeLeftInMillis / 1000) <= 3)
            tickSound.start();
        String remainingTime = getResources().getString(R.string.time_remaining) + " " + mTimeLeftInMillis / 1000;
        qTimer.setText(remainingTime);
    }

    public void pauseTimer(){
        Log.i("DIM", "Paused timer");
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    public void resumeTimer(){
        Log.i("DIM", "Resumed timer");
        startTimer(mTimeLeftInMillis);
        mTimerRunning = true;
    }

    private void resetTimer(){
        Log.i("DIM", "Reset timer");
        //qTimer.setTextColor(Color.WHITE);
        mTimeLeftInMillis = timeLengthMilli;
    }

    private void displayResult() {
        Log.i("DIM", "Quiz done");
        Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
        intent.putExtra("score", ptsTotal);
        intent.putExtra("difficulte", difficulty);
        startActivity(intent);
        finish();

    }

    public void onBackPressed() {
        if(mTimerRunning)
            pauseTimer();

        new AlertDialog.Builder(this)
                .setMessage("Voulez-vous vraiment arrêter le quiz?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resumeTimer();
                    }
                })
                .show();
    }

    public void pauseGame() {
        Log.i("DIM", "Paused game");

        pauseVisible = true;
        pauseSound.start();

        pauseDialog.show(getSupportFragmentManager(), "pause");

        if(mTimerRunning)
            pauseTimer();
    }

    public void resumeGame() {
        Log.i("DIM", "Resumed game");

        pauseVisible = false;
        unpauseSound.start();

        if (!mTimerRunning)
            resumeTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimerRunning && !pauseVisible)
            pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mTimerRunning && !pauseVisible)
            resumeTimer();
    }
  
    @Override
    protected void onDestroy() {
        QuizBD.destroyInstance();
        super.onDestroy();
    }

}

