package com.example.quizapp_btl.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp_btl.MainActivity;
import com.example.quizapp_btl.R;
import com.example.quizapp_btl.UserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText edtName,edtMail, edtPass ;
    Button btnRegister, btnLogin;
    FirebaseAuth myAuth;
    FirebaseFirestore fstore;
    boolean valid = true;
    String imageLinkDefault = "https://firebasestorage.googleapis.com/v0/b/quizappbtl.appspot.com/o/p2.jpg?alt=media&token=e5145ac4-471a-4662-a252-ee3316e4c3cd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        edtName = findViewById(R.id.FullName);
        edtMail = findViewById(R.id.Mail);
        edtPass = findViewById(R.id.Password);
        btnRegister = findViewById(R.id.Regiser_Res);
        btnLogin = findViewById(R.id.Login_Res);
        myAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }

    public void handleRegisterFromRegis(View view){
        checkValid(edtName);
        checkValid(edtMail);
        checkValid(edtPass);
        if(valid == true){
            myAuth.createUserWithEmailAndPassword(edtMail.getText().toString(), edtPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = myAuth.getCurrentUser();
                    Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                    DocumentReference df = fstore.collection("Users").document(user.getUid());
                    Map<String, Object> userInfor = new HashMap<>();
                    userInfor.put("FullName", edtName.getText().toString());
                    userInfor.put("Email", edtMail.getText().toString());
                    userInfor.put("PassWord", edtPass.getText().toString());
                    userInfor.put("linkAvatar", imageLinkDefault);
                    userInfor.put("Id", df.getId());
                    //specify user is the admin
                    userInfor.put("User", "1");
                    df.set(userInfor);
                    Intent i = new Intent(Register.this, UserActivity.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Failed cause input aren't type enough", Toast.LENGTH_SHORT).show();
        }
    }
    public void handleLoginFromRegis(View view){
        Intent i = new Intent(Register.this, Login.class);
        startActivity(i);
    }
    public boolean checkValid(EditText editText) {
        if(editText.getText().toString().isEmpty()){
            valid = false;
        }
        else valid = true;
        return valid;
    }
}