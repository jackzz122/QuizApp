package com.example.quizapp_btl.History;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quizapp_btl.R;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryClass> {
    private Activity context;
    int idLayout;
     List<HistoryClass> lst;

    public HistoryAdapter(Activity context, List<HistoryClass> lst){
        super(context, R.layout.item_history, lst);
        this.context = context;
        this.lst = lst;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.item_history, null, false);
        TextView nameCate = rootView.findViewById(R.id.NameTile);
        nameCate.setText(lst.get(position).getNameCate());
        TextView correctAnsw = rootView.findViewById(R.id.correctAnsw);
        correctAnsw.setText(lst.get(position).getCorrectAnsw());
        TextView wrongAnsw = rootView.findViewById(R.id.wrongAnsw);
        wrongAnsw.setText(lst.get(position).getWrongAns());
        TextView totalAns = rootView.findViewById(R.id.totalAns);
        totalAns.setText(lst.get(position).getTotalAns());

        return rootView;
    }
}
