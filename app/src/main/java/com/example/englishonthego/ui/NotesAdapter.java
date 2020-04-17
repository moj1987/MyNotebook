package com.example.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.model.NoteModel;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    OnNoteClickListener onNoteClickListener;
    List<NoteModel> noteItems;
    Context context;

    public NotesAdapter(List<NoteModel> noteItems, Context context, OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
        this.noteItems = noteItems;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view,onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnNoteClickListener onNoteClickListener;

        public NotesViewHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener) {
            super(itemView);

            this.onNoteClickListener=onNoteClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteClickListener.onNoteClicked(getAdapterPosition());
        }
    }

    public interface OnNoteClickListener {
        void onNoteClicked(int position);
    }
}
