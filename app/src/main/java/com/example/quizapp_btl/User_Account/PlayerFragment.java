package com.example.quizapp_btl.User_Account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp_btl.History.HistoryActivity;
import com.example.quizapp_btl.Information.InformationActivity;
import com.example.quizapp_btl.Login_Register.Login;
import com.example.quizapp_btl.R;
import com.example.quizapp_btl.RecycleView.UserAdapter;
import com.example.quizapp_btl.RecycleView.UserClass;
import com.example.quizapp_btl.my_InterFace.User_InterFace_ClickItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerFragment extends Fragment {
    private String name;
    private String email;
    private String pass;
    private String url;
    RecyclerView recycleUser;
    FirebaseUser myUser;
    TextView txtName;
    CircleImageView avatarUser;
    Button logOut;
    FirebaseFirestore fstoreUser;
    UserAdapter adapter;
    List<UserClass> lstSettings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        init(rootView);
        return rootView;
    }
    public List<UserClass> ListSettings(){
        lstSettings = new ArrayList<UserClass>();
        lstSettings.add(new UserClass("Information", "https://firebasestorage.googleapis.com/v0/b/quizappbtl.appspot.com/o/setting.png?alt=media&token=30510c34-fb1b-448d-968d-e3f1f08a2fd7"));
        lstSettings.add(new UserClass("History", "https://firebasestorage.googleapis.com/v0/b/quizappbtl.appspot.com/o/baseline_person_white_48.png?alt=media&token=379abca3-50e9-4cdc-92f4-28f387a5a64e"));
        lstSettings.add(new UserClass("Contact", "https://firebasestorage.googleapis.com/v0/b/quizappbtl.appspot.com/o/phone-call.png?alt=media&token=1b08fd12-cd4f-41f6-b4a8-9c6e5842892f"));
        return lstSettings;
    }
    public void init(View rootView){
        recycleUser = rootView.findViewById(R.id.settingUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycleUser.setLayoutManager(linearLayoutManager);

        txtName = rootView.findViewById(R.id.nameFirbase);
        avatarUser = rootView.findViewById(R.id.avatrFirebase);

        logOut = rootView.findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogOut(view);
            }
        });

        adapter = new UserAdapter(ListSettings(), new User_InterFace_ClickItem() {
            @Override
            public void onClickItem(UserClass userClass) {
                handleClickSettings(userClass);
            }
        });
        myUser = FirebaseAuth.getInstance().getCurrentUser();
        fstoreUser = FirebaseFirestore.getInstance();
        if(myUser != null){
            DocumentReference userRef = fstoreUser.collection("Users").document(myUser.getUid());
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name = documentSnapshot.getString("FullName");
                    email = documentSnapshot.getString("Email");
                    url = documentSnapshot.getString("linkAvatar");
                    txtName.setText(name);
                    Picasso.get().load(url).into(avatarUser);
                }
            });
        }
        recycleUser.setAdapter(adapter);
    }

    private void handleClickSettings(UserClass userClass) {
        String nameSets = userClass.getNameSettings();
        Intent i;
        if(nameSets.equals("Information")){
            i = new Intent(getContext(), InformationActivity.class);
            startActivity(i);
        }
        else if(nameSets.equals("History")){
            i = new Intent(getContext(), HistoryActivity.class);
            startActivity(i);
        }
        else if(nameSets.equals("Contact")){
            i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel: " + "012345789"));
            startActivity(i);
        }
    }

    public void handleLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), Login.class));
        getActivity().finish();
    }
}