package com.example.quizapp_btl.User_Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.quizapp_btl.R;
import com.example.quizapp_btl.RecycleView.UserAdapter;
import com.example.quizapp_btl.RecycleView.UserClass;
import com.example.quizapp_btl.my_InterFace.User_InterFace_ClickItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserAccountActivity extends AppCompatActivity {
    RecyclerView userList;
    ArrayList<UserClass> arrayUser;
    UserAdapter adapter;
    FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        init();
    }
    public void init(){
        userList = findViewById(R.id.userList);
        userList.setLayoutManager(new LinearLayoutManager(this));
        arrayUser = new ArrayList<>();
        adapter = new UserAdapter(arrayUser, new User_InterFace_ClickItem() {
            @Override
            public void onClickItem(UserClass userClass) {

            }
        });
        userList.setAdapter(adapter);
        fstore = FirebaseFirestore.getInstance();
        CollectionReference collection = fstore.collection("Users");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getId();
                        Map<String, Object> data = document.getData();
                        if(data != null){
                            Object FullName = data.get("FullName");
                            Object LinkImage = data.get("linkAvatar");
                            if(FullName != null && LinkImage != null){
                                String nameStr = FullName.toString();
                                String imageUrl = LinkImage.toString();
                                arrayUser.add(new UserClass(nameStr, imageUrl));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                else{
                    Log.v("ERROR", "Error getting documents");
                }
            }
        });
    }
}