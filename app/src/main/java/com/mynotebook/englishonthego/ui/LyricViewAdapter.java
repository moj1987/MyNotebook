package com.mynotebook.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.model.LyricSaveModel;

import java.util.List;

public class LyricViewAdapter extends RecyclerView.Adapter<LyricViewAdapter.LyricViewAdapterViewHolder> {
    private List<LyricSaveModel> items;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public LyricViewAdapter(List<LyricSaveModel> items, Context context, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public LyricViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_lyric, parent, false);
        return new LyricViewAdapterViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LyricViewAdapterViewHolder holder, int position) {
        LyricSaveModel lyricSaveModel = items.get(position);
        holder.trackName.setText(lyricSaveModel.getTrackName());
        holder.artist.setText(lyricSaveModel.getArtistName());
        holder.album.setText(lyricSaveModel.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class LyricViewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener onItemClickListener;
        TextView trackName, artist, album;


        public LyricViewAdapterViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            trackName = itemView.findViewById(R.id.track_name);
            artist = itemView.findViewById(R.id.artist);
            album = itemView.findViewById(R.id.album);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
}
