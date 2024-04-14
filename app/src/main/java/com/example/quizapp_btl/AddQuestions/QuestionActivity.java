package com.example.quizapp_btl.AddQuestions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizapp_btl.R;
import com.example.quizapp_btl.my_InterFace.QuizItemsClicked;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    RecyclerView RecycleLst;
    SearchView searchQuiz;
    Button btnAddQuiz;
    private String keyCate;
    private int indexLst;
    QuestionAdapter QuesAdapter;
    ArrayList<QuestionClass> quesList;
    DatabaseReference Quizmdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        init();

//        private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String deletedItem = intent.getStringExtra("deleted");
//            }
//        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    public void init(){
        Intent data = getIntent();
        keyCate = data.getStringExtra("KeysCate");
        quesList = new ArrayList<>();
        RecycleLst = findViewById(R.id.QuizList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecycleLst.setLayoutManager(linearLayoutManager);
        btnAddQuiz = findViewById(R.id.addQuiz);
        QuesAdapter = new QuestionAdapter(quesList, new QuizItemsClicked() {
            @Override
            public void QuizItemsClickListener(QuestionClass questionClass) {
                Intent i = new Intent(QuestionActivity.this, DetailQues.class);
                i.putExtra("KeysQues", questionClass.getIdOWn());
                i.putExtra("Mode", "0");
                startActivity(i);
            }
        });

        RecycleLst.setAdapter(QuesAdapter);
        Quizmdata = FirebaseDatabase.getInstance().getReference();

        loadData();
    }
    public void loadData(){
        Quizmdata.child("questions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QuestionClass data = snapshot.getValue(QuestionClass.class);
                if(data != null && data.getIdCate().equals(keyCate)){
                    quesList.add(new QuestionClass(data.getIdOWn(), data.getIdCate(), data.getOptA(), data.getOptB(), data.getOptC(), data.getOptD(), data.getCorrectOpt(), data.getTitleQues()));
                    QuesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QuestionClass update = snapshot.getValue(QuestionClass.class);
                String quesKeys = snapshot.getKey();
                for(int i =0; i<quesList.size();i++){
                    if(quesList.get(i).getIdOWn().equals(quesKeys)) {
                        quesList.set(i, update);
                        QuesAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String quesKeys = snapshot.getKey();
                for(int i =0; i<quesList.size();i++){
                    if(quesList.get(i).getIdOWn().equals(quesKeys)) {
                        quesList.remove(i);
                        QuesAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteCate){
            Toast.makeText(this,String.valueOf(item.getGroupId()), Toast.LENGTH_SHORT).show();
            handleDeleteQues(item.getGroupId());
        }
        else if(item.getItemId() == R.id.updateCate){
            indexLst = item.getGroupId();
            handleUpdateQues(indexLst);
        }
        return super.onContextItemSelected(item);
    }

    public void handleDeleteQues(int groupId) {
        QuestionClass questionClass = quesList.get(groupId);
        Quizmdata.child("questions").child(questionClass.getIdOWn()).removeValue();
    }

    public void handleUpdateQues(int indeLst){
        Intent i = new Intent(QuestionActivity.this, DetailQues.class);
        i.putExtra("Mode", "1");
        i.putExtra("Keys", quesList.get(indeLst).getIdOWn());
        i.putExtra("title", quesList.get(indeLst).getTitleQues());
        i.putExtra("correct", quesList.get(indeLst).getCorrectOpt());
        i.putExtra("optA", quesList.get(indeLst).getOptA());
        i.putExtra("optB", quesList.get(indeLst).getOptB());
        i.putExtra("optC", quesList.get(indeLst).getOptC());
        i.putExtra("optD", quesList.get(indeLst).getOptD());
        startActivity(i);
    }

    public void handleAddQues(View view){
        DatabaseReference mdata = Quizmdata.child("questions").push();
        String keys = mdata.getKey();
        QuestionClass quesClass = new QuestionClass(keys, keyCate, " ", " ", " ", " ", " ", "Unkown");
        mdata.setValue(quesClass, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null){
                    Toast.makeText(QuestionActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(QuestionActivity.this, "Add Failed", Toast.LENGTH_SHORT).show();
                    Log.v("Error", String.valueOf(error));
                }
            }
        });
    }
}