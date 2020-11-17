package com.lawlett.musicplayer.adapters.yearsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawlett.musicplayer.R;

import java.util.ArrayList;

public class AdapterYears extends RecyclerView.Adapter<AdapterYears.YearsViewHolder> {

    private ArrayList<String> years = new ArrayList<>();
    OnYearClickListener listener;

    public void setListener(OnYearClickListener listener) {
        this.listener = listener;
    }

    public void setYears(ArrayList<String> years) {
        this.years = years;
    }

    @NonNull
    @Override
    public YearsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YearsViewHolder(LayoutInflater.from(parent.getContext())
                                                   .inflate(R.layout.years_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull YearsViewHolder holder, int position) {
        holder.year.setText(years.get(position));
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    public class YearsViewHolder extends RecyclerView.ViewHolder {
        TextView year;

        public YearsViewHolder(@NonNull View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.years_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCLick(getAdapterPosition());
                }
            });
        }
    }
}
