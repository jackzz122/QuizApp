package com.example.quizapp_btl.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp_btl.AddQuestions.QuestionActivity;
import com.example.quizapp_btl.AddQuestions.QuestionClass;
import com.example.quizapp_btl.Category.CategoryAdapter;
import com.example.quizapp_btl.Category.CategoryClass;
import com.example.quizapp_btl.R;
import com.example.quizapp_btl.my_InterFace.Categories_Interface_ClickItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    private String nameAdmin;
    private Intent i;
    private boolean checkAddUpdate = false;
    private int indexLst;
    private String uriTemp;
    private String userUID;
    //Btn add update Categories
    Button btnAdd, btnAddQuote, delBtn, cancelBtn, btnUpdate;
    SearchView searchCategories;
    String imgLink;
    Uri imgTemp;
    CardView cardView, cardViewDelete;
    EditText quoteTitle;
    ImageView closeBtn;
    TextView edtName;
    CircleImageView imageGallery;
    CircleImageView AvatarHome;

    //Categories List
    RecyclerView CateGory;
    ArrayList<CategoryClass> lstCate;
    CategoryAdapter adapter;
    final int REQUEST_CODE = 90;

    //Firebase
    FirebaseFirestore fstoreRealize;
    FirebaseUser userMainActiviy;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mdata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseApp.initializeApp(getActivity());
        init(rootView);
        return rootView;
    }
    public void init(View rootView){
        edtName = rootView.findViewById(R.id.NameDB);
        AvatarHome = rootView.findViewById(R.id.avatarAdmin);
        CateGory = rootView.findViewById(R.id.categoryLST);
        int numberRows = 2;
        CateGory.setLayoutManager(new GridLayoutManager(getContext(), numberRows));
        lstCate = new ArrayList<>();
        adapter = new CategoryAdapter(lstCate, new Categories_Interface_ClickItem() {
            @Override
            public void onClickCategoriesItem(CategoryClass categoryClass) {
                Intent i = new Intent(getActivity(), QuestionActivity.class);
                i.putExtra("KeysCate", categoryClass.getKeys());
                startActivity(i);
            }
        });
        adapter.setContextMenuEnable(true);
        CateGory.setAdapter(adapter);

        fstoreRealize = FirebaseFirestore.getInstance();
        userMainActiviy = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://quizappbtl.appspot.com");
        mdata = FirebaseDatabase.getInstance().getReference();

        LoadData();

        if(userMainActiviy != null){
            String uid = userMainActiviy.getUid();
            userUID = uid;
            DocumentReference findAdmin = fstoreRealize.collection("Users").document(uid);
            findAdmin.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    nameAdmin = documentSnapshot.getString("FullName");
                    edtName.setText(nameAdmin);
                    Picasso.get().load(documentSnapshot.getString("linkAvatar")).into(AvatarHome);

                }
            });
        }

//        DocumentReference documentReference = fstoreRealize.collection("Users").document(userUID);
//        documentReference.addSnapshotListener((Executor) this, (documentSnapShot, e) -> {
//           if(documentSnapShot.exists()) {
//               edtName.setText(documentSnapShot.getString("FullName"));
//               Picasso.get().load(documentSnapShot.getString("linkAvatar")).into(AvatarHome);
//           }
//           else {
//               Toast.makeText(getContext(), "Doc do not exists", Toast.LENGTH_SHORT).show();
//           }
//        });

        btnAdd = rootView.findViewById(R.id.AddQuotez);
        cardView = rootView.findViewById(R.id.CardView);
        cardViewDelete = rootView.findViewById(R.id.deleteCardView);
        closeBtn = rootView.findViewById(R.id.close);
        btnAddQuote = rootView.findViewById(R.id.confirmAddQuote);
        imageGallery = rootView.findViewById(R.id.addImageQuotez);

        quoteTitle = rootView.findViewById(R.id.quoteHeader);
        delBtn = rootView.findViewById(R.id.delAgree);
        cancelBtn = rootView.findViewById(R.id.DelDisagree);
        btnUpdate = rootView.findViewById(R.id.updateQuote);

        searchCategories = rootView.findViewById(R.id.searchCategories);
        searchCategories.clearFocus();
        searchCategories.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOpenPic(view);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClose(view);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAdd(view);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuotes(view, indexLst);
            }
        });
        btnAddQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuote(view);
            }
        });


    }

    private void filterList(String newText) {
        ArrayList<CategoryClass> cateLst = new ArrayList<>();
        for(CategoryClass cate : lstCate ){
            if(cate.getNameCate().toLowerCase().contains(newText.toLowerCase())){
                cateLst.add(cate);
            }
        }
        if(cateLst.isEmpty()){
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilterList(cateLst);
        }
    }

    public void LoadData(){
        mdata.child("category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CategoryClass cate = snapshot.getValue(CategoryClass.class);
                lstCate.add(new CategoryClass(cate.getKeys(),cate.getNameCate(), cate.getImageCate(), cate.getSetNums()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CategoryClass updated = snapshot.getValue(CategoryClass.class);
                if(updated != null){
                    String cateKey =  snapshot.getKey();
                    for(int i = 0; i< lstCate.size();i++){
                        if(lstCate.get(i).getKeys().equals(cateKey)){
                            lstCate.set(i, updated);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
                if(getContext() != null){
                    Toast.makeText(getContext(), "Changed Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removedKey = snapshot.getKey();
                for(int i = 0;i<lstCate.size();i++){
                    if(lstCate.get(i).getKeys().equals(removedKey)){
                        lstCate.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
                if(getContext() != null){
                    Toast.makeText(getContext(), "Remove cate Success", Toast.LENGTH_SHORT).show();
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

    public void handleOpenPic(View view){
        if(i == null){
            i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.setType("image/*");
        }
        startActivityForResult(i,REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getContext(), data.getDataString(), Toast.LENGTH_SHORT).show();
        if(requestCode == REQUEST_CODE && data != null){
            Toast.makeText(getContext(), data.getDataString(), Toast.LENGTH_SHORT).show();
            Uri uri= data.getData();
            imgTemp = data.getData();
            imgLink = data.getDataString();
            Log.v("Active", imgLink);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imageGallery.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleAdd(View view){
        cardView.setVisibility(View.VISIBLE);
        btnAddQuote.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
    }

    public void addQuote(View view) {
        Calendar cal = Calendar.getInstance();
        StorageReference riversRef = storageReference.child("images"+ cal.getTimeInMillis() + ".png");
        UploadTask uploadTask = riversRef.putFile(imgTemp);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.v("Error", e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUri = uri.toString();
                        DatabaseReference df = mdata.child("category").push();
                        String keys = df.getKey();
                        //Tao node tren datbase
                        CategoryClass categoryClass = new CategoryClass(keys,quoteTitle.getText().toString(), downloadUri, 0);
                        df.setValue(categoryClass, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if(error == null){
                                    Toast.makeText(getContext(), "Save data success", Toast.LENGTH_SHORT).show();

                                }
                                else Toast.makeText(getContext(), "Save data failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                });
            }
        });



    }

    public void handleClose(View view){
        cardView.setVisibility(View.GONE);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteCate){
            Snackbar.make(getActivity().findViewById(R.id.rootAct), "Delete", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getContext(),String.valueOf(item.getGroupId()), Toast.LENGTH_SHORT).show();
            handleDelete(item.getGroupId());
        }
        else if(item.getItemId() == R.id.updateCate){
            Snackbar.make(getActivity().findViewById(R.id.rootAct), "Update", Snackbar.LENGTH_LONG).show();
            indexLst = item.getGroupId();
            btnUpdate.setVisibility(View.VISIBLE);
            btnAddQuote.setVisibility(View.GONE);
            handleUpdate(indexLst);
        }
        return super.onContextItemSelected(item);

    }
    public void handleDelete(int index){
        cardViewDelete.setVisibility(View.VISIBLE);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CategoryClass cateChoose = lstCate.get(index);
                    mdata.child("category").child(cateChoose.getKeys()).removeValue();
                    String imageUri = cateChoose.getImageCate();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri);
                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Delete image successfull", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Delete image unsucessfull", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DatabaseReference deleteques = FirebaseDatabase.getInstance().getReference("questions");
                    Query query = deleteques.orderByChild("idCate").equalTo(cateChoose.getKeys());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.v("Success", "Success delete");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.v("Error", "Failed to delete ques");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                cardViewDelete.setVisibility(View.GONE);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewDelete.setVisibility(View.GONE);
            }
        });

    }
    public void handleUpdate(int indexLst){
        CategoryClass categoryClass = lstCate.get(indexLst);
        Picasso.get().load(categoryClass.getImageCate()).into(imageGallery);
        quoteTitle.setText(categoryClass.getNameCate());
        cardView.setVisibility(View.VISIBLE);
        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOpenPic(view);
            }
        });

    }
    public void updateQuotes(View view, int indexLst) {
        CategoryClass categoryClass = lstCate.get(indexLst);
        Calendar cal = Calendar.getInstance();
        StorageReference riversRef = storageReference.child("images" + cal.getTimeInMillis() + ".png");
        UploadTask uploadTask;
        if(imgTemp == null){
            uploadTask = riversRef.putFile(Uri.fromFile(new File(categoryClass.getImageCate())));
        }
        else uploadTask = riversRef.putFile(imgTemp);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to Update" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.v("Error", e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       String downloadUri = uri.toString();
                       mdata.child("category").child(categoryClass.getKeys()).child("imageCate").setValue(downloadUri);
                       mdata.child("category").child(categoryClass.getKeys()).child("nameCate").setValue(quoteTitle.getText().toString());

                   }
               });
            }
        });
    }
}