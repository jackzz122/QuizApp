package com.example.quizapp_btl.Category;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp_btl.R;
import com.example.quizapp_btl.my_InterFace.Categories_Interface_ClickItem;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<CategoryClass> myList;

    private Categories_Interface_ClickItem cateClickItem;

    private boolean contextMenuEnable;
    public void setContextMenuEnable(boolean enable){
        this.contextMenuEnable = enable;
    }
    public CategoryAdapter(ArrayList<CategoryClass> myList, Categories_Interface_ClickItem cateClickItem){
        this.myList = myList;
        this.cateClickItem = cateClickItem;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_category, null, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryClass categoryClass = myList.get(position);
        holder.txtName.setText(categoryClass.getNameCate());
        Picasso.get().load(categoryClass.getImageCate()).into(holder.imageQuiz);
        holder.cardGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cateClickItem.onClickCategoriesItem(categoryClass);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(myList != null) return myList.size();
        return 0;
    }
    public void setFilterList(ArrayList<CategoryClass> categoryClasses){
        this.myList = categoryClasses;
        notifyDataSetChanged();
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private TextView txtName;
        private CircleImageView imageQuiz;

        private CardView cardGrid;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.cateName);
            imageQuiz = itemView.findViewById(R.id.cateImage);
            cardGrid = itemView.findViewById(R.id.CardGrid);
            cardGrid.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
           if(contextMenuEnable){
               contextMenu.setHeaderTitle("Update-Delete");
               contextMenu.add(getAdapterPosition(), R.id.updateCate, 0, "Update");
               contextMenu.add(getAdapterPosition(), R.id.deleteCate, 1, "Delete");
           }
        }


    }
}
