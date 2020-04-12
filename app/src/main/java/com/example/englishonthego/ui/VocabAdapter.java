package com.example.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.model.VocabModel;

import java.util.List;

public class VocabAdapter extends RecyclerView.Adapter<VocabAdapter.VocabViewHolder> {

    private List<VocabModel> vocabItems;
    private Context context;

    public VocabAdapter(List<VocabModel> vocabItems, Context context) {
        this.vocabItems = vocabItems;
        this.context = context;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vocab, parent, false);
        return new VocabViewHolder(view);
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

    public class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView vocab, definition, example;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            vocab = itemView.findViewById(R.id.item_vocab);
            definition = itemView.findViewById(R.id.item_definition);
            example = itemView.findViewById(R.id.item_example);
        }
    }

    interface setOnVocabClickListener {
         void onVocabClicked(int position);

    }
}
