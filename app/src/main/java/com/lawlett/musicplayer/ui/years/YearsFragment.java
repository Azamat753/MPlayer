package com.lawlett.musicplayer.ui.years;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawlett.musicplayer.R;
import com.lawlett.musicplayer.adapters.yearsAdapter.AdapterYears;
import com.lawlett.musicplayer.adapters.yearsAdapter.OnYearClickListener;
import com.lawlett.musicplayer.data.network.local.Prefs;

import java.util.ArrayList;

public class YearsFragment extends Fragment {

    AdapterYears adapterYears;
    RecyclerView recyclerView;
    ArrayList<String> strings = new ArrayList<>();
    public static final String YEAR = "year";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fill();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_years, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_years);
        adapterYears = new AdapterYears();
        recyclerView.setAdapter(adapterYears);
        adapterYears.setYears(strings);
        adapterYears.setListener(new OnYearClickListener() {
            @Override
            public void onCLick(int position) {
//                Bundle bundle = new Bundle();
//                bundle.putString(YEAR,strings.get(position));
                new Prefs(getContext()).setYear(strings.get(position));
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.action_yearsFragment_to_listFragment);
            }
        });
    }

    private void fill(){
        for (int i = 4; i < 21; i++) {
            if (i>9){
                strings.add("20"+i);
            }else{
            strings.add("200"+i);
            }
        }
    }
}