package com.example.quizapp_btl.QuesForUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizapp_btl.History.HistoryActivity;
import com.example.quizapp_btl.R;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class ScoreActivity extends AppCompatActivity {
    Button btnShare, btnScore;
    CircularSeekBar circularSeekBar;
    TextView totalCorrect, MaxQues, wrongAns;
    private String idPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        init();
    }
    public void init(){
        Intent data = getIntent();
        int Max = data.getIntExtra("Max", 10);
        int correct = data.getIntExtra("Correct", 10);
        int wrong = data.getIntExtra("Wrong", 0);
         idPlayer = data.getStringExtra("idPlayer");
        btnScore = findViewById(R.id.closeScore);
        btnShare = findViewById(R.id.Share);
        circularSeekBar = findViewById(R.id.circular);
        totalCorrect = findViewById(R.id.totalCorrect);
        MaxQues = findViewById(R.id.MaxQues);
        wrongAns = findViewById(R.id.wrongAns);

        totalCorrect.setText(String.valueOf(correct));
        MaxQues.setText(String.valueOf(Max));
        wrongAns.setText(String.valueOf(wrong));

        circularSeekBar.setMax(Max);
        circularSeekBar.setProgress(correct);
    }

    public void handleHistory(View view){
        Intent i = new Intent(ScoreActivity.this, HistoryActivity.class);
        i.putExtra("idPlayer",idPlayer );
        startActivity(i);
        finish();
    }
    public void handleClose(View view){
        finish();
    }
}