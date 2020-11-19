package com.lawlett.musicplayer.ui.history;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.lawlett.musicplayer.App;
import com.lawlett.musicplayer.R;
import com.lawlett.musicplayer.adapters.adapter.AdapterMusic;
import com.lawlett.musicplayer.adapters.favoriteAdapter.AdapterFavorite;
import com.lawlett.musicplayer.adapters.yearsAdapter.OnYearClickListener;
import com.lawlett.musicplayer.data.network.MusicBrowser;
import com.lawlett.musicplayer.data.network.MusicLoaderListener;
import com.lawlett.musicplayer.data.network.local.Prefs;
import com.lawlett.musicplayer.room.FavoriteModel;
import com.lawlett.musicplayer.ui.list.ListFragment;
import com.lawlett.musicplayer.ui.music.MusicActivity;
import com.lawlett.musicplayer.ui.years.YearsFragment;
import com.lawlett.musicplayer.widgets.LineProgress;
import com.lawlett.musicplayer.widgets.PlayPauseView;
import com.lawlett.musicplayer.widgets.Slider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;

import dm.audiostreamer.AudioStreamingManager;
import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.Logger;
import dm.audiostreamer.MediaMetaData;


public class HistoryFragment extends Fragment {

    List<FavoriteModel> list;
    AdapterFavorite adapterFavorite;
    RecyclerView recyclerView;
    public static final String MEDIA = "media";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        list = new ArrayList<>();
        adapterFavorite = new AdapterFavorite();

        App.getDataBase().favoriteDao().getAllLive().observe(requireActivity(), favoriteModels -> {
            if (favoriteModels != null) {
                list.clear();
                for (int i = 0; i < favoriteModels.size() ; i++) {
                    if (favoriteModels.get(i).getYear().equals(new Prefs(getContext()).getYear())){
                        list.add(favoriteModels.get(i));
                    }
                }
                adapterFavorite.updateList(list);
            }
        });


        recyclerView=view.findViewById(R.id.history_recycler);
        adapterFavorite= new AdapterFavorite();
        recyclerView.setAdapter(adapterFavorite);

        adapterFavorite.setListener(new OnYearClickListener() {
            @Override
            public void onCLick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MEDIA,list.get(position));
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_historyFragment_to_listFragment,bundle);
            }
        });

    }

}
