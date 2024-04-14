package com.example.quizapp_btl.RecycleView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp_btl.R;
import com.example.quizapp_btl.my_InterFace.User_InterFace_ClickItem;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder>{
    private List<UserClass> lst;

    private User_InterFace_ClickItem userInterFaceClick;

    public UserAdapter(List<UserClass> lst, User_InterFace_ClickItem itemClick) {
        this.lst = lst;
        this.userInterFaceClick = itemClick;
    }

    @NonNull
    @Override
    public UserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_items, parent, false);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterViewHolder holder, int position) {
        UserClass userClass = lst.get(position);
        holder.txtSetting.setText(userClass.getNameSettings());
        Picasso.get().load(userClass.getImgSettings()).into(holder.avatarSetting);

        holder.LayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInterFaceClick.onClickItem(userClass);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(lst != null) return lst.size();
        return 0;
    }

    public class UserAdapterViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout LayoutItem;
        private TextView txtSetting;
        private CircleImageView avatarSetting;

        public UserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            LayoutItem = itemView.findViewById(R.id.layoutItem);
            txtSetting = itemView.findViewById(R.id.NameQuiz);
            avatarSetting = itemView.findViewById(R.id.imgQuiz);
        }
    }
}
