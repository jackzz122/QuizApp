package com.example.quizapp_btl.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp_btl.History.HistoryActivity;
import com.example.quizapp_btl.Information.InformationActivity;
import com.example.quizapp_btl.Login_Register.Login;
import com.example.quizapp_btl.R;
import com.example.quizapp_btl.RecycleView.UserAdapter;
import com.example.quizapp_btl.RecycleView.UserClass;
import com.example.quizapp_btl.User_Account.UserAccountActivity;
import com.example.quizapp_btl.my_InterFace.User_InterFace_ClickItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {
    public static final String TAG = UserFragment.class.getName();
    private String name;
    private String url;
    private String email;
    RecyclerView recyclerView;
    FirebaseUser user;
    TextView txtName;
    CircleImageView avatar;
    Button btnLogout;
    FirebaseFirestore fstoreGetUser;
    UserAdapter adapter;
    List<UserClass> lst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rowView= inflater.inflate(R.layout.fragment_user, container, false);

        init(rowView);
        return rowView;
    }
    public List<UserClass> getListUser(){
        lst = new ArrayList<UserClass>();
        lst.add(new UserClass("Information", "https://firebasestorage.googleapis.com/v0/b/quizappbtl.appspot.com/o/setting.png?alt=media&token=30510c34-fb1b-448d-968d-e3f1f08a2fd7"));
        lst.add(new UserClass("User list", "https://firebasestorage.googleapis.com/v0/b/quizappbtl.appspot.com/o/baseline_person_white_48.png?alt=media&token=379abca3-50e9-4cdc-92f4-28f387a5a64e"));
        return lst;
    }
    public void init(View rowView){


        recyclerView = rowView.findViewById(R.id.settingUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new UserAdapter(getListUser(), new User_InterFace_ClickItem() {
            @Override
            public void onClickItem(UserClass userClass) {
                onClickChooseSettings(userClass);
            }
        });
        recyclerView.setAdapter(adapter);
        

        user = FirebaseAuth.getInstance().getCurrentUser();
        fstoreGetUser = FirebaseFirestore.getInstance();

        txtName = rowView.findViewById(R.id.nameFirbase);
        avatar = rowView.findViewById(R.id.avatrFirebase);
        btnLogout = rowView.findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogOut(view);
            }
        });

        if(user != null){
            String uiD = user.getUid();
            DocumentReference df = fstoreGetUser.collection("Users").document(uiD);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name = documentSnapshot.getString("FullName");
                    email = documentSnapshot.getString("Email");
                    url = documentSnapshot.getString("linkAvatar");
                    Picasso.get().load(url).into(avatar);
                    txtName.setText(name);
                }
            });
        }
    }
    public void handleLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), Login.class));

        getActivity().finish();
    }

    public void onClickChooseSettings(UserClass userClass){
        String settings = userClass.getNameSettings();
        Intent i;
        if(settings == "Information"){
            i = new Intent(getContext(), InformationActivity.class);
            startActivity(i);
        }
        else if(settings == "History"){
            i = new Intent(getContext(), HistoryActivity.class);
            startActivity(i);
        }
        else{
            i = new Intent(getContext(), UserAccountActivity.class);
            startActivity(i);
        }
        Toast.makeText(getContext(), userClass.getNameSettings(), Toast.LENGTH_SHORT).show();
    }
}