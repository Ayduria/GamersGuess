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
    private Question question;
    private Reponse reponse;
    private Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        bd = QuizBD.getDatabase(getApplicationContext());
        //displayTest();
    }

    /*private void displayTest() {
        TextView question = (TextView)findViewById(R.id.testQuestion);
        TextView reponse1 = (TextView)findViewById(R.id.testReponse1);
        TextView reponse2 = (TextView)findViewById(R.id.testReponse2);
        TextView reponse3 = (TextView)findViewById(R.id.testReponse3);
        TextView reponse4 = (TextView)findViewById(R.id.testReponse4);

        Question testQuestion = bd.quizDao().findQuestionById(1);
        question.setText(testQuestion.question);

        List<TextView> reponseViews = Arrays.asList(reponse1, reponse2, reponse3, reponse4);
        List<Reponse> reponses = bd.quizDao().loadAllQuestionAnswers(1);

        int noReponse = 0;
        for(TextView textView : reponseViews) {
            textView.setText(reponses.get(noReponse).choixReponse);
            noReponse++;
        }
    }*/

    @Override
    protected void onDestroy() {
        bd.destroyInstance();
        super.onDestroy();
    }
}