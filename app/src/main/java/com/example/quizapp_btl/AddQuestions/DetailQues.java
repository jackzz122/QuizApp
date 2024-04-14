package com.example.quizapp_btl.AddQuestions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.quizapp_btl.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

public class DetailQues extends AppCompatActivity {

    EditText edtQues, edtOptA, edtOptB,edtOptC,edtOptD,edtOptCorrect;
    Button btnUpload, btnUpdate;
    private String keyQuiz;
    private boolean check = true;
    DatabaseReference quesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ques);

        init();
    }
    public void init(){
        Intent data = getIntent();
        edtQues = findViewById(R.id.edtQues);
        edtOptA = findViewById(R.id.edtOptA);
        edtOptB = findViewById(R.id.edtOptB);
        edtOptC = findViewById(R.id.edtOptC);
        edtOptD = findViewById(R.id.edtOptD);
        btnUpdate = findViewById(R.id.btnUpdate);
        edtOptCorrect = findViewById(R.id.edtCorrect);
        btnUpload = findViewById(R.id.btnUpload);

        quesRef = FirebaseDatabase.getInstance().getReference();
        if(!data.getStringExtra("Mode").equals("1")){
            keyQuiz = data.getStringExtra("KeysQues");
        }
        else{
            keyQuiz = data.getStringExtra("Keys");
            edtQues.setText(data.getStringExtra("title"));
            edtOptCorrect.setText(data.getStringExtra("correct"));
            edtOptA.setText(data.getStringExtra("optA"));
            edtOptB.setText(data.getStringExtra("optB"));
            edtOptC.setText(data.getStringExtra("optC"));
            edtOptD.setText(data.getStringExtra("optD"));
        }

    }
    public boolean checkValid(EditText edit){
        if(edit.getText().toString().isEmpty()){
            check = false;
            return check;
        }
        return check;
    }
    public void handleUpLoad(View view){
        checkValid(edtQues);
        checkValid(edtOptA);
        checkValid(edtOptB);
        checkValid(edtOptC);
        checkValid(edtOptD);
        if(check){
            quesRef.child("questions").child(keyQuiz).child("correctOpt").setValue(edtOptCorrect.getText().toString());
            quesRef.child("questions").child(keyQuiz).child("optA").setValue(edtOptA.getText().toString());
            quesRef.child("questions").child(keyQuiz).child("optB").setValue(edtOptB.getText().toString());
            quesRef.child("questions").child(keyQuiz).child("optC").setValue(edtOptC.getText().toString());
            quesRef.child("questions").child(keyQuiz).child("optD").setValue(edtOptD.getText().toString());
            quesRef.child("questions").child(keyQuiz).child("titleQues").setValue(edtQues.getText().toString());
        }
        finish();
    }
}