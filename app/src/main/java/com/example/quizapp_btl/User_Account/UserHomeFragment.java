package com.example.quizapp_btl.User_Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp_btl.AddQuestions.QuestionClass;
import com.example.quizapp_btl.Category.CategoryAdapter;
import com.example.quizapp_btl.Category.CategoryClass;
import com.example.quizapp_btl.QuesForUser.ShowQues;
import com.example.quizapp_btl.R;
import com.example.quizapp_btl.my_InterFace.Categories_Interface_ClickItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeFragment extends Fragment {
    RecyclerView QuestionUserRecycle;
    TextView txtUserName;
    ArrayList<CategoryClass> listCategories;
    CircleImageView userAvatar;
    FirebaseUser user;
    private String idUser;
    FirebaseAuth userAuth;
    FirebaseFirestore fstoreUser;
    DatabaseReference databaseUser;
    CategoryAdapter adapter;
    SearchView searchQuiz;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_home, container, false);
        init(rootView);
        return rootView;
    }
    public void init(View rootView){
        QuestionUserRecycle = rootView.findViewById(R.id.userQues);
        txtUserName = rootView.findViewById(R.id.userDB);
        userAvatar = rootView.findViewById(R.id.avatarUser);
        int nums = 2;
        QuestionUserRecycle.setLayoutManager(new GridLayoutManager(getContext(), nums));
        listCategories = new ArrayList<>();
        searchQuiz = rootView.findViewById(R.id.userSearch);
        searchQuiz.clearFocus();
        searchQuiz.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        adapter = new CategoryAdapter(listCategories, new Categories_Interface_ClickItem() {
            @Override
            public void onClickCategoriesItem(CategoryClass categoryClass) {
                Intent i = new Intent(getContext(), ShowQues.class);
                i.putExtra("KeysCate", categoryClass.getKeys());
                i.putExtra("NameQuiz", categoryClass.getNameCate());
                i.putExtra("Id", idUser);
                startActivity(i);
            }
        });
        adapter.setContextMenuEnable(false);
        QuestionUserRecycle.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();
        fstoreUser = FirebaseFirestore.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference();
        if(user != null){
            String Uid = user.getUid();
            DocumentReference userRef = fstoreUser.collection("Users").document(Uid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    idUser = documentSnapshot.getString("Id");
                    String name = documentSnapshot.getString("FullName");
                    txtUserName.setText(name);
                    Picasso.get().load(documentSnapshot.getString("linkAvatar")).into(userAvatar);
                }
            });
        }

        loadData();
    }
    public void filterList(String newText){
        ArrayList<CategoryClass> lstTemp = new ArrayList<>();
        for(CategoryClass cate : listCategories){
            if(cate.getNameCate().toLowerCase().contains(newText.toLowerCase())){
                lstTemp.add(cate);
            }
        }
        if(lstTemp.isEmpty()){
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilterList(lstTemp);
        }
    }
    public void loadData(){
        databaseUser.child("category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CategoryClass cateClass = snapshot.getValue(CategoryClass.class);
                listCategories.add(new CategoryClass(cateClass.getKeys(), cateClass.getNameCate(), cateClass.getImageCate(), cateClass.getSetNums()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CategoryClass cateClass = snapshot.getValue(CategoryClass.class);
                String keys = snapshot.getKey();
                for(int i = 0; i< listCategories.size();i++){
                    if(listCategories.get(i).getKeys().equals(keys)){
                        listCategories.set(i, cateClass);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}