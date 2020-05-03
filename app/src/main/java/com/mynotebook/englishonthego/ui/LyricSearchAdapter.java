package com.mynotebook.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.networking.LyricModel;

import java.util.List;

public class LyricSearchAdapter extends RecyclerView.Adapter<LyricSearchAdapter.LyricSearchViewHolder> {

    private List<LyricModel> items;
    private final Context context;
    private OnItemClickListener mOnItemClickListener;

    public LyricSearchAdapter(List<LyricModel> items, Context context, OnItemClickListener mOnItemClickListener) {
        this.items = items;
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public LyricSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_lyric_search, parent, false);
        return new LyricSearchViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LyricSearchViewHolder holder, int position) {
        final LyricModel lyricModel = items.get(position);
        holder.trackName.setText(lyricModel.getTrackName());
        holder.artist.setText(lyricModel.getArtistName());
        holder.album.setText(lyricModel.getAlbumName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * to set new data when data changes
     * TODO: Not needed when using LiveData?
     */
    public void setItems(List<LyricModel> lyrics) {
        this.items = lyrics;
    }

    public void addItemsToTop(List<LyricModel> lyricsToAdd) {
        items.addAll(0, lyricsToAdd);
        notifyDataSetChanged();
    }

    public void addItemsToBottom(List<LyricModel> lyricsToAdd) {
        items.addAll(items.size() - 1, lyricsToAdd);
        notifyDataSetChanged();
    }

    public class LyricSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trackName, artist, album;
        OnItemClickListener onItemClickListener;

        public LyricSearchViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
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
