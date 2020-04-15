package com.example.englishonthego.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.model.VocabModel;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class VocabAdapter extends RecyclerView.Adapter<VocabAdapter.VocabViewHolder> {
    private static final String TAG = "Vocab Adapter";
    private List<VocabModel> vocabItems;
    private Context context;
    private OnVocabClickListener onVocabClickListener;

    public VocabAdapter(List<VocabModel> vocabItems, Context context, OnVocabClickListener onVocabClickListener) {
        this.vocabItems = vocabItems;
        this.context = context;
        this.onVocabClickListener = onVocabClickListener;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vocab, parent, false);
        return new VocabViewHolder(view, onVocabClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        VocabModel currentVocab = vocabItems.get(position);

        holder.vocab.setText(currentVocab.getVocab());
        holder.definition.setText(currentVocab.getDefinition());
        holder.example.setText(currentVocab.getExample());
    }

    @Override
    public int getItemCount() {
        return vocabItems.size();
    }


    public class VocabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vocab, definition, example;
        OnVocabClickListener onVocabClickListener;

        public VocabViewHolder(@NonNull View itemView, OnVocabClickListener onVocabClickListener) {
            super(itemView);
            vocab = itemView.findViewById(R.id.item_vocab);
            definition = itemView.findViewById(R.id.item_definition);
            example = itemView.findViewById(R.id.item_example);
            this.onVocabClickListener = onVocabClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVocabClickListener.onVocabClicked(getAdapterPosition());
        }
    }

    public interface OnVocabClickListener {
        void onVocabClicked(int position);
    }
}
