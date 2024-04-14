package com.example.quizapp_btl.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText edtmail, edtpass;
    Button btnLogin, btnRegister;
    FirebaseAuth authLogin;
    FirebaseFirestore fstoreLogin;
    private boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init(){
        edtmail = findViewById(R.id.Mail);
        edtpass = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.Login);
        btnRegister = findViewById(R.id.Register);
        authLogin = FirebaseAuth.getInstance();
        fstoreLogin = FirebaseFirestore.getInstance();
    }
    public boolean checkValid(EditText editText){
        if(editText.getText().toString().isEmpty()){
            valid = false;
        }else valid = true;
        return valid;
    }

    public void checkUserOrAdmin(String uid){
        DocumentReference df = fstoreLogin.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                if(documentSnapshot.getString("Admin") != null) {
                    //User is admin
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                if(documentSnapshot.getString("User") != null){
                    Intent i = new Intent(Login.this, UserActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Login Failed in Check User Admin", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void handleLogin(View view){
        checkValid(edtmail);
        checkValid(edtpass);
        if(valid){
            authLogin.signInWithEmailAndPassword(edtmail.getText().toString(), edtpass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkUserOrAdmin(authResult.getUser().getUid());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    Log.v("Error", String.valueOf(e));
                }
            });
        }
    }

    public void handleRegister(View view){
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }
}