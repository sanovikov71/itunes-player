package com.gmail.sanovikov71.intechtask.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.sanovikov71.intechtask.R;
import com.gmail.sanovikov71.intechtask.network.Song;
import com.gmail.sanovikov71.intechtask.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<Song> mData = new ArrayList<>();

    public SongListAdapter(Context context) {
        mContext = context;
    }

    public void updateDataset(List<Song> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_list_item, parent, false);
        return new ViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder typedHolder = (ViewHolder) holder;

        Song song = mData.get(position);

        Glide.with(mContext)
                .load(song.getArtworkUrl100())
                .into(typedHolder.mImage);

        typedHolder.mItemId = song.getTrackId();
        typedHolder.mArtistName.setText(song.getArtistName());
        typedHolder.mTrackName.setText(song.getTrackName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        int mItemId;
        ImageView mImage;
        TextView mArtistName;
        TextView mTrackName;

        public ViewHolder(final Context context, View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.item_image);
            mArtistName = (TextView) view.findViewById(R.id.item_artist_name);
            mTrackName = (TextView) view.findViewById(R.id.item_track_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra(PlayerActivity.EXTRA_SONG_ID, mItemId);
                    context.startActivity(intent);
                }
            });
        }
    }

}
