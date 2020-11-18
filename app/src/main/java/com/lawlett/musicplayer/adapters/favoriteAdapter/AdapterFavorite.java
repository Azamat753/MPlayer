package com.lawlett.musicplayer.adapters.favoriteAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lawlett.musicplayer.R;
import com.lawlett.musicplayer.adapters.yearsAdapter.OnYearClickListener;
import com.lawlett.musicplayer.room.FavoriteModel;

import java.util.ArrayList;
import java.util.List;

import dm.audiostreamer.MediaMetaData;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.FavoriteViewHolder> {

    List<FavoriteModel> list = new ArrayList<>();
    OnYearClickListener listener;

    public void setListener(OnYearClickListener listener) {
        this.listener = listener;
    }


    public void updateList(List<FavoriteModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(parent.getContext())
                                                   .inflate(R.layout.favorite_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView favorite,fav_des;
        ImageView favorite_image;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favorite = itemView.findViewById(R.id.fav_text_mediaTitle);
            fav_des=itemView.findViewById(R.id.fav_text_mediaDesc);
            favorite_image=itemView.findViewById(R.id.fav_img_mediaArt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCLick(getAdapterPosition());
                }
            });
        }
        public void onBind(FavoriteModel favoriteModel) {
            favorite.setText(favoriteModel.getTitle());
            fav_des.setText(favoriteModel.getMediaArtist());
            Glide.with(favorite_image.getContext()).load(favoriteModel.getMediaArt()).into(favorite_image);
        }
    }
}
