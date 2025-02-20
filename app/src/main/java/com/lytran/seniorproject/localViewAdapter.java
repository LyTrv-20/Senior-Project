package com.lytran.seniorproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class localViewAdapter extends RecyclerView.Adapter<localViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Wifi.wifiModel> localList;

    public localViewAdapter(Context context, ArrayList<Wifi.wifiModel> localList){
        this.context = context;
        this.localList = localList;
    }
    @NonNull
    @Override
    public localViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout, giving it visuals

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.network_view, parent, false);
        return new localViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull localViewAdapter.MyViewHolder holder, int position) {
        //add values to views

        holder.name.setText(localList.get(position).name());
        holder.infos.setText(localList.get(position).getAnalysis());
        //TODO: set color of status light according to status
    }

    @Override
    public int getItemCount() {
        //number of itemms to display
        return localList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, infos;
        View status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status_light);
            name = itemView.findViewById(R.id.networkName);
            infos = itemView.findViewById(R.id.infos);
        }
    }
}
