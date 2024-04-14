package com.example.quizapp_btl.History;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quizapp_btl.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    SQLiteDatabase hisDatabase;
    ListView lstViewHist;
    HistoryAdapter adapter;
    ArrayList<HistoryClass> hisList;
    private String idPlayer;
    private String idCate;
    Button btnDelete;
    FirebaseUser userAuth;
    private int idEle;
    FirebaseFirestore storageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        init();
    }
    public void init(){
        lstViewHist = findViewById(R.id.historyPlayer);
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        storageUser = FirebaseFirestore.getInstance();
        if(userAuth != null){
            DocumentReference userRef = storageUser.collection("Users").document(userAuth.getUid());
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    idPlayer = documentSnapshot.getString("Id");
                    loadData();
                }
            });
        }
        hisList = new ArrayList<>();
        adapter = new HistoryAdapter(this, hisList);
        lstViewHist.setAdapter(adapter);
        lstViewHist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idCate = hisList.get(i).getCategoriesId();
                int n = hisDatabase.delete("score","idCate = ?", new String[]{idCate});
                String msg = "";
                if(n == 0){
                    msg = "No record to delete";
                }
                else msg = n + " record is deleted";
                Toast.makeText(HistoryActivity.this, "delete success", Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete = findViewById(R.id.deleteSQL);

    }
    public void loadData(){
        hisDatabase = openOrCreateDatabase("ThongTinDiem.db", MODE_PRIVATE, null);
        hisList.clear();
        Cursor c= hisDatabase.query("score", null, "idPlayer = ?", new String[]{idPlayer}, null, null, null);
        c.moveToNext();
        while(c.isAfterLast() == false){
            String cateKeys = c.getString(1);
            String nameCate = c.getString(2);
            String correct = c.getString(3);
            String wrong = c.getString(4);
            String total = c.getString(5);
            HistoryClass historyClass = new HistoryClass(cateKeys,nameCate, correct, wrong, total);
            c.moveToNext();
            hisList.add(historyClass);
        }
        c.close();
        adapter.notifyDataSetChanged();
    }



}