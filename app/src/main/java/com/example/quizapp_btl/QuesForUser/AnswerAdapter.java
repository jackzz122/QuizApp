package com.example.quizapp_btl.QuesForUser;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quizapp_btl.AddQuestions.QuestionClass;
import com.example.quizapp_btl.R;

import java.util.List;

public class AnswerAdapter extends ArrayAdapter<QuestionClass> {
    private Activity context;

    private List<QuestionClass> lst;

    public AnswerAdapter(Activity context, List<QuestionClass> lst) {
        super(context, R.layout.ans_items, lst);
        this.context = context;
        this.lst = lst;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.ans_items, null, false);

        TextView txtAns = view.findViewById(R.id.ansItems);
//        txtAns.setText(lst.get(position).get);

        return super.getView(position, convertView, parent);
    }
}
