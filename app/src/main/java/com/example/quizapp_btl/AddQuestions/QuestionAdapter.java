package com.example.quizapp_btl.AddQuestions;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp_btl.R;
import com.example.quizapp_btl.my_InterFace.QuizItemsClicked;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    List<QuestionClass> quesClass;
    private QuizItemsClicked quizItemsClicked;
    public QuestionAdapter(List<QuestionClass> lst, QuizItemsClicked quizItemsClicked){
        this.quesClass = lst;
        this.quizItemsClicked = quizItemsClicked;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_items, null, false);
        return new QuestionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionClass questionClass = quesClass.get(position);
        holder.titleQuiz.setText(questionClass.getTitleQues());
        holder.titleQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizItemsClicked.QuizItemsClickListener(questionClass);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(quesClass != null) return quesClass.size();
        return 0;
    }

    public void setFilterQuiz(ArrayList<QuestionClass> lst){
        this.quesClass = lst;
        notifyDataSetChanged();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView titleQuiz;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleQuiz = itemView.findViewById(R.id.titleQuiz);
            titleQuiz.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Update-Delete");
            contextMenu.add(getAdapterPosition(), R.id.updateCate, 0, "Update");
            contextMenu.add(getAdapterPosition(), R.id.deleteCate, 1, "Delete");
        }
    }


}
