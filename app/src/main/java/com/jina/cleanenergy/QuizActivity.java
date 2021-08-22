package com.jina.cleanenergy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    TextView quizText;
    TextView answer1;
    TextView answer2;
    TextView answer3;
    TextView answer4;
    String answer = "1";
    private int quizStage = 0;
    private final static String CODE_QUIZSTAGE = "quizStage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        init();

    }

    private void init() {
        quizText = findViewById(R.id.quizText);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        answer1.setTag("1");
        answer2.setTag("2");
        answer3.setTag("3");
        answer4.setTag("4");

        SharedPreferences settings = getSharedPreferences("quiz", MODE_PRIVATE);
        quizStage = settings.getInt(CODE_QUIZSTAGE, 0);

        quizText.setText(getResources().getStringArray(R.array.quiz)[quizStage]);
        answer1.setText(getResources().getStringArray(R.array.answer1)[quizStage]);
        answer2.setText(getResources().getStringArray(R.array.answer2)[quizStage]);
        answer3.setText(getResources().getStringArray(R.array.answer3)[quizStage]);
        answer4.setText(getResources().getStringArray(R.array.answer4)[quizStage]);

        answer = getResources().getStringArray(R.array.answer)[quizStage];

        View.OnClickListener onClickListener = v -> {
            String tag = v.getTag().toString();
            if (answer.equals(tag)) {
                showGoodJob();
            } else {
                v.setBackgroundColor(0xffff0000);
//                showTryAgain();
            }
        };

        answer1.setOnClickListener(onClickListener);
        answer2.setOnClickListener(onClickListener);
        answer3.setOnClickListener(onClickListener);
        answer4.setOnClickListener(onClickListener);
    }

    private void showGoodJob() {
        Toast.makeText(this, R.string.goodJob, Toast.LENGTH_SHORT).show();

        quizStage ++;
        if (quizStage > 9) {
            quizStage = 0;
        }

        SharedPreferences settings = getSharedPreferences("quiz", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(CODE_QUIZSTAGE, quizStage).apply();

        setResult(RESULT_OK);
        finish();
    }

    private void showTryAgain() {
        Toast.makeText(this, R.string.tryAgain, Toast.LENGTH_SHORT).show();
    }
}
