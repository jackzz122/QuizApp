package com.example.quizapp_btl.Information;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quizapp_btl.Fragment.UserFragment;
import com.example.quizapp_btl.Login_Register.Login;
import com.example.quizapp_btl.MainActivity;
import com.example.quizapp_btl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {
    FirebaseFirestore fstoreInfor;
    FirebaseUser UserInfo;
    FirebaseAuth InfoAuth;

    EditText edtFullname, edtEmail, edtPassWord;
    ImageView edtAvatar;
    private Uri imageTemp;
    private Intent i;
    private String userUid;
    private String imgLink;
    private boolean valid = true;
    Button btnAllow, btnConfirm;
    StorageReference storageReference;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        init();
    }
    public boolean checkValid(EditText edit){
        if(edit.getText().toString().isEmpty()){
            valid = false;
            return valid;
        }return valid;
    }
    public void disabledEditText(EditText edit){
        edit.setEnabled(false);
    }
    public void enableEditText(EditText edit){
        edit.setEnabled(true);
    }
    public void disableClickedImage(ImageView image){
        image.setClickable(false);
    }
    public void enableClickedImage(ImageView image){
        image.setClickable(true);
    }
    public void init(){
        edtFullname = findViewById(R.id.Info_FullName);
        edtEmail = findViewById(R.id.Info_Email);
        edtPassWord = findViewById(R.id.Info_Password);
        edtFullname = findViewById(R.id.Info_FullName);
        edtAvatar = findViewById(R.id.Info_Avatar);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://quizappbtl.appspot.com");

        fstoreInfor = FirebaseFirestore.getInstance();
        UserInfo = FirebaseAuth.getInstance().getCurrentUser();
        if(UserInfo != null){
            String Uid = UserInfo.getUid();
            userUid = Uid;
            DocumentReference findInfo = fstoreInfor.collection("Users").document(Uid);
            findInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    edtFullname.setText(documentSnapshot.getString("FullName"));
                    edtEmail.setText(documentSnapshot.getString("Email"));
                    edtPassWord.setText(documentSnapshot.getString("PassWord"));
                    Picasso.get().load(documentSnapshot.getString("linkAvatar")).into(edtAvatar);
                }
            });
        }
        disabledEditText(edtFullname);
        disabledEditText(edtEmail);
        disabledEditText(edtPassWord);
        disableClickedImage(edtAvatar);
    }
    public void handleEditInfo(View view){
        enableEditText(edtFullname);
        enableEditText(edtPassWord);
        enableEditText(edtEmail);
        enableClickedImage(edtAvatar);
    }
    public void handleChangeAvatar(View view){
        if(i == null){
            i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.setType("image/*");
        }
        startActivityForResult(i,40);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 40 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            imageTemp = data.getData();
            try {
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                edtAvatar.setImageBitmap(imgBitmap);
            }catch (Exception e){
                Log.v("Error", "Image Info Error");
            }
        }
    }
    public void UpdateEmail(){
        UserInfo.updateEmail(edtEmail.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.v("OK", "email changed success");
                        UpdatePassword();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InformationActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void UpdatePassword(){
        UserInfo.updatePassword(edtPassWord.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.v("OK", "password success");
                        DocumentReference updateAll = fstoreInfor.collection("Users").document(userUid);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("Email", edtEmail.getText().toString());
                        updates.put("FullName", edtFullname.getText().toString());
                        updates.put("PassWord", edtPassWord.getText().toString());
                        updates.put("linkAvatar", imgLink);
                        updateAll.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(InformationActivity.this, "Success To Save to FireStore", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onFailure: " + e);
                                Toast.makeText(InformationActivity.this, "Failed to save " + String.valueOf(e), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    public void handleConfirmInfo(View view){
        checkValid(edtEmail);
        checkValid(edtFullname);
        checkValid(edtPassWord);
        if(valid){
            Calendar cal = Calendar.getInstance();
            StorageReference riversRef = storageReference.child("images" + cal.getTimeInMillis() + ".png");
            UploadTask uploadTask;
            uploadTask = riversRef.putFile(imageTemp);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InformationActivity.this, "Failed to Update" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.v("Error", e.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            imgLink = downloadUri;
                            UpdateEmail();
                        }
                    });
                }
            });
            startActivity(new Intent(InformationActivity.this, MainActivity.class));
            finish();
        }
    }
}