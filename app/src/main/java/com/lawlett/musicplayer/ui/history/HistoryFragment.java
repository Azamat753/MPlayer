package com.lawlett.musicplayer.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lawlett.musicplayer.App;
import com.lawlett.musicplayer.R;
import com.lawlett.musicplayer.adapters.favoriteAdapter.AdapterFavorite;
import com.lawlett.musicplayer.adapters.yearsAdapter.OnYearClickListener;
import com.lawlett.musicplayer.room.FavoriteModel;

import java.util.ArrayList;
import java.util.List;

import dm.audiostreamer.MediaMetaData;


public class HistoryFragment extends Fragment {

    FavoriteModel favoriteModel;
    List<FavoriteModel> list;
    AdapterFavorite adapterFavorite;
    RecyclerView recyclerView;

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
                list.addAll(favoriteModels);
                adapterFavorite.updateList(list);
            }
        });


        recyclerView=view.findViewById(R.id.history_recycler);
        adapterFavorite= new AdapterFavorite();
        recyclerView.setAdapter(adapterFavorite);

        adapterFavorite.setListener(new OnYearClickListener() {
            @Override
            public void onCLick(int position) {
                Toast.makeText(requireContext(), position+"", Toast.LENGTH_SHORT).show();

            }
        });

    }

}