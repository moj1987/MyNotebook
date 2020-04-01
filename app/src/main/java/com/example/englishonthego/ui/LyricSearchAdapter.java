package com.example.englishonthego.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.networking.Responses;

import java.util.List;

public class LyricSearchAdapter extends RecyclerView.Adapter<LyricSearchAdapter.ViewHolder> {

    private List<Responses> mResponses;
    private final Context context;

    public LyricSearchAdapter(List<Responses> mResponses, Context context) {
        this.mResponses = mResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lyric_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Responses responses = mResponses.get(position);
        holder.trackName.setText(responses.getTrackName());
        holder.artist.setText(responses.getArtistName());
        holder.album.setText(responses.getAlbumName());

    }

    @Override
    public int getItemCount() {
        return mResponses.size();
    }

    /**
     * to set new data when data changes
     */
    public void setItems(List<Responses> responses) {
        this.mResponses = responses;
    }

    public class ViewHolder extends RecyclerView.ViewHolder    {
        TextView trackName, artist, album;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.track_name);
            artist = itemView.findViewById(R.id.artist);
            album = itemView.findViewById(R.id.album);
        }
    }
    public interface onItemClickListener {
 void onItemClicked(int position);
    }
}
