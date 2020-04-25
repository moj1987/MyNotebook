package com.mynotebook.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.model.NoteModel;

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
        return new NotesViewHolder(view, onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NoteModel currentNote = noteItems.get(position);
        holder.noteTitle.setText(currentNote.getTitle());
        holder.noteText.setText(currentNote.getText());
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle, noteText;
        OnNoteClickListener onNoteClickListener;

        public NotesViewHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.item_note_title);
            noteText = itemView.findViewById(R.id.item_note_text);

            this.onNoteClickListener = onNoteClickListener;
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