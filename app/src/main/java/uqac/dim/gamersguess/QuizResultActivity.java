package uqac.dim.gamersguess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresult);

        Bundle b = getIntent().getExtras();
        finalScore = b.getInt("score");

        TextView score = (TextView)findViewById(R.id.final_score);
        score.setText(String.valueOf(finalScore));

        Button menuButton = (Button)findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DIM", "Back to menu");

                finish();
            }
        });
    }
}
