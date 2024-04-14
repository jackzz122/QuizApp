package com.example.quizapp_btl.QuesForUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp_btl.AddQuestions.QuestionClass;
import com.example.quizapp_btl.Category.CategoryClass;
import com.example.quizapp_btl.R;
import com.google.android.material.color.utilities.Score;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowQues extends AppCompatActivity {
    TextView txtTitle;
    ProgressBar progessBar;
    TextView edtansA, edtansB, edtansC, edtansD, currenIndex, totalIndex;
    Button btnConfirm, btnNext, backToQuiz;
    ArrayList<QuestionClass> quesList;
    DatabaseReference quesTionData;
    CardView noData;
    private int delta;
    private String idPlayer, keysCate, NameCate;
    private String resultUser;
    private int indexQues = 0;
    private boolean checkUnknown = false;
    private TextView textChoose;
    private TextView textResult;
    private int dem = 1;
    private int correctAns = 0;
    private int wrongAns = 0;
    private int maxIndex;
    SQLiteDatabase sqlLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ques);
        init();
    }
    public void init(){
        Intent data = getIntent();
         keysCate = data.getStringExtra("KeysCate");
        idPlayer = data.getStringExtra("Id");
        NameCate = data.getStringExtra("NameQuiz");

        txtTitle = findViewById(R.id.txtquestion);
        edtansA = findViewById(R.id.ansA);
        edtansB = findViewById(R.id.ansB);
        edtansC = findViewById(R.id.ansC);
        edtansD = findViewById(R.id.ansD);
        btnConfirm = findViewById(R.id.confirm_btn);
        quesList = new ArrayList<>();
        btnNext = findViewById(R.id.Next_btn);
        noData = findViewById(R.id.noData);
        backToQuiz = findViewById(R.id.backToQuiz);
        currenIndex = findViewById(R.id.currentIndex);
        totalIndex = findViewById(R.id.totalIndex);
        currenIndex.setText(String.valueOf(dem));

        sqlLite = openOrCreateDatabase("ThongTinDiem.db", MODE_PRIVATE, null);
        try {
            String sql = "CREATE TABLE score(idPlayer TEXT, idCate TEXT, CateName TEXT, correct TEXT , wrong TEXT, total TEXT)";
            sqlLite.execSQL(sql);
        }catch (Exception e){
            Log.e("Error", "Table Exists");
        }

        View.OnClickListener textViewClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTextViewClick((TextView) view);
            }
        };
        edtansA.setOnClickListener(textViewClick);
        edtansB.setOnClickListener(textViewClick);
        edtansC.setOnClickListener(textViewClick);
        edtansD.setOnClickListener(textViewClick);
        btnNext.setEnabled(false);
        btnNext.setClickable(false);

        quesTionData = FirebaseDatabase.getInstance().getReference("questions");
        Query query = quesTionData.orderByChild("idCate").equalTo(keysCate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    QuestionClass ques = snapshot1.getValue(QuestionClass.class);
                    Log.v("ACTIVE", ques.getTitleQues());
                    if(ques.getTitleQues().equals("Unkown")) checkUnknown = true;
                    quesList.add(ques);
                }
                if(quesList.size() == 0){
                    showNoData();
                }
                else{
                    if(checkUnknown){
                        showNoData();
                    }
                    else{
                        maxIndex = quesList.size();
                        delta = 100 / maxIndex;
                        totalIndex.setText(String.valueOf("/" +maxIndex));
                        showQuestion(indexQues);
                        noData.setVisibility(View.GONE);
                        btnNext.setEnabled(true);
                        btnConfirm.setEnabled(true);
                        checkUnknown = false;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void showNoData(){
        noData.setVisibility(View.VISIBLE);
        backToQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnNext.setEnabled(false);
        btnConfirm.setEnabled(false);
    }
    public void showQuestion(int indexQues){
        txtTitle.setText(quesList.get(indexQues).getTitleQues());
        edtansA.setText(quesList.get(indexQues).getOptA());
        edtansB.setText(quesList.get(indexQues).getOptB());
        edtansC.setText(quesList.get(indexQues).getOptC());
        edtansD.setText(quesList.get(indexQues).getOptD());

        enableText(edtansA);
        colorBack(edtansA);
        enableText(edtansB);
        colorBack(edtansB);
        enableText(edtansC);
        colorBack(edtansC);
        enableText(edtansD);
        colorBack(edtansD);
    }
    public void colorBack(TextView text){
        text.setBackgroundColor(Color.WHITE);
        text.setTextColor(Color.BLACK);
    }
    public void enableText(TextView text){
        text.setEnabled(true);
    }
    public void disableText(TextView text){
        text.setEnabled(false);
    }
    public void handleTextViewClick(TextView t){
        String text= t.getText().toString();
        resultUser = text;
        textChoose = t;
        t.setTextColor(Color.WHITE);
        t.setBackgroundColor(Color.BLUE);
        if(textChoose != edtansA){
           colorBack(edtansA);
        }
        if(textChoose != edtansB){
            colorBack(edtansB);
        }
        if(textChoose != edtansC){
            colorBack(edtansC);
        }
        if(textChoose != edtansD){
            colorBack(edtansD);
        }

    }

    public void RealCorrect(TextView text){
        text.setTextColor(Color.WHITE);
        text.setBackgroundColor(Color.GREEN);
    }
    public void handleConfirmAns(View view){
        if(quesList.get(indexQues).getCorrectOpt().equals(resultUser)){
            correctAns += 1;
        }
        else{
            wrongAns += 1;
        }
        textResult = new TextView(this);
        textResult.setText(quesList.get(indexQues).getCorrectOpt());

        if(textResult.getText().toString().equals(edtansA.getText().toString()) ){
            RealCorrect(edtansA);
        }
        else {
            disableText(edtansA);
        }

        if(textResult.getText().toString().equals(edtansB.getText().toString())){
            RealCorrect(edtansB);
        }
        else{
            disableText(edtansB);
        }
        if(textResult.getText().toString().equals(edtansC.getText().toString())){
            RealCorrect(edtansC);
        }
        else{
            disableText(edtansC);
        }
        if(textResult.getText().toString().equals(edtansD.getText().toString())){
            RealCorrect(edtansD);
        }
        else{
            disableText(edtansD);
        }

        btnNext.setEnabled(true);
        btnNext.setClickable(true);
    }
    public void handleNext(View view){
        indexQues++;
        currenIndex.setText(String.valueOf(dem));
        if(dem == maxIndex){
            Intent i = new Intent(ShowQues.this, ScoreActivity.class);
            i.putExtra("Max", maxIndex);
            i.putExtra("Correct", correctAns);
            i.putExtra("Wrong", wrongAns);
            i.putExtra("idPlayer", idPlayer);

            Cursor c = sqlLite.query("score", null, "idCate = ?", new String[]{keysCate}, null, null, null);

            ContentValues myValues = new ContentValues();
            myValues.put("idPlayer", idPlayer);
            myValues.put("idCate", keysCate);
            myValues.put("CateName", NameCate);
            myValues.put("correct", String.valueOf(correctAns));
            myValues.put("wrong", String.valueOf(wrongAns));
            myValues.put("total", String.valueOf(maxIndex));

            if(c != null && c.getCount() > 0){
                int update = sqlLite.update("score", myValues, "idCate = ?", new String[]{keysCate} );
                String msg = " ";
                if(update == 0){
                    msg = "No record to update";
                }
                else msg = "Record updated";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            else{
                String msg = "";
                if(sqlLite.insert("score", null, myValues) == -1){
                    msg = "Failed to Insert Record";
                }
                else{
                    msg = "Insert Record Success";
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }

            startActivity(i);
            finish();
        }
        else {
            showQuestion(indexQues);
        }
        dem++;

    }
}