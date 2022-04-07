package uqac.dim.gamersguess;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import uqac.dim.gamersguess.persistance.Question;
import uqac.dim.gamersguess.persistance.QuizBD;
import uqac.dim.gamersguess.persistance.Reponse;
import uqac.dim.gamersguess.persistance.Score;

public class QuizActivity extends AppCompatActivity {

    private QuizBD bd;
    String difficulty;
    List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        bd = QuizBD.getDatabase(getApplicationContext());

        // Get all questions for selected difficulty
        Bundle b = getIntent().getExtras();
        difficulty = b.getString("difficulty");

        switch(difficulty) {
            case "f":   questions = bd.quizDao().getEasyQuestions();
                break;
            case "m":   questions = bd.quizDao().getMediumQuestions();
                break;
            case "d":   questions = bd.quizDao().getHardQuestions();
                break;
        }

        displayTest();
    }

    private void displayTest() {
        TextView question = (TextView)findViewById(R.id.question);
        TextView reponse1 = (TextView)findViewById(R.id.reponse1);
        TextView reponse2 = (TextView)findViewById(R.id.reponse2);
        TextView reponse3 = (TextView)findViewById(R.id.reponse3);
        TextView reponse4 = (TextView)findViewById(R.id.reponse4);
        List<TextView> reponseViews = Arrays.asList(reponse1, reponse2, reponse3, reponse4);

        Question firstQuestion = questions.get(0);

        question.setText(firstQuestion.question);

        List<Reponse> reponses = bd.quizDao().loadAllQuestionAnswers(firstQuestion.getQuestionId());

        int noReponse = 0;
        for(TextView textView : reponseViews) {
            textView.setText(reponses.get(noReponse).choixReponse);
            noReponse++;
        }
    }

    @Override
    protected void onDestroy() {
        bd.destroyInstance();
        super.onDestroy();
    }
}
