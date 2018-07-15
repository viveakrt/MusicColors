package com.example.vivek.musiccolors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{

    ArrayList<SongInfo> song;
    Context context;
    OnItemClickListener onItemClickListener;

    SongAdapter(Context context, ArrayList<SongInfo> song){
        this.context=context;
        this.song=song;
    }

    public interface OnItemClickListener {
      void onItemClick(CardView b,View v,SongInfo obj,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemclicklistener){
        this.onItemClickListener = onItemclicklistener;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.row_song,parent,false);
        return  new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder holder, final int position) {
        final SongInfo c = song.get(position);
        holder.Song.setText(c.song);
        holder.Artist.setText(c.artist);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(holder.cv,v,c,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return song.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView Artist,Song;
        CardView cv;
        public SongHolder(View itemView) {
            super(itemView);
            Artist = itemView.findViewById(R.id.Artist_name);
            Song = itemView.findViewById(R.id.Song_name);
            cv = itemView.findViewById(R.id.cardView);
        }
    }
}
