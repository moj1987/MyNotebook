package com.mynotebook.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.model.VocabModel;

import java.util.List;

public class VocabAdapter extends RecyclerView.Adapter<VocabAdapter.ExpandedVocabViewHolder> {
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
    public ExpandedVocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ExpandedVocabViewHolder(view, onVocabClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpandedVocabViewHolder holder, int position) {
        VocabModel currentVocab = vocabItems.get(position);

        holder.vocab.setText(currentVocab.getVocab());
        holder.definition.setText(currentVocab.getDefinition());
        holder.example.setText(currentVocab.getExample());
    }

    @Override
    public int getItemCount() {
        return vocabItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        return R.layout.item_vocab;
    }

    public class ExpandedVocabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vocab, definition, example;
        OnVocabClickListener onVocabClickListener;

        public ExpandedVocabViewHolder(@NonNull View itemView, OnVocabClickListener onVocabClickListener) {
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
