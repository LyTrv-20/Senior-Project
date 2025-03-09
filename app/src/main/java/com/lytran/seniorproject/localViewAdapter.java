package com.lytran.seniorproject;

import static androidx.core.content.ContextCompat.getDrawable;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

public class localViewAdapter extends RecyclerView.Adapter<localViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Wifi.wifiModel> localList;
    boolean isAbs;

    public localViewAdapter(Context context, ArrayList<ScanResult> localList, String current, boolean isAbs){
        this.context = context;
        this.localList = new ArrayList<>();
        HashSet<String> seenSSIDs = new HashSet<>(); // Set to track unique SSIDs
        this.isAbs = isAbs;
        seenSSIDs.add(current);

        for (ScanResult scanResult : localList) {
            if (!scanResult.SSID.trim().isEmpty() && !seenSSIDs.contains(scanResult.SSID)) {
                this.localList.add(new Wifi.wifiModel(scanResult.SSID, Rate.rate(scanResult, localList), new Wifi.Data(scanResult))); // Add only unique SSIDs
                seenSSIDs.add(scanResult.SSID); // Mark SSID as seen
            }
        }
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
        if(isAbs)
            holder.infos.setText(localList.get(position).getAbstract());
        else
            holder.infos.setText(localList.get(position).getAnalysis());

        int stat;
        double rat = MathUtil.interpolate(-90, 90, localList.get(position).status());
        if(rat <= 90 && rat > 30)
            stat = 2;
        else if(rat <= 30 && rat > -30)
            stat = 1;
        else
            stat = 0;

        holder.setStatus(context, stat);
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

        public void setStatus(Context context, int stat){
            switch(stat){
                case 1:
                    status.setBackground(ContextCompat.getDrawable(context, R.drawable.statusy));
                    break;
                case 2:
                    status.setBackground(ContextCompat.getDrawable(context, R.drawable.statusr));
                    break;
                default:
                    status.setBackground(ContextCompat.getDrawable(context, R.drawable.statusg));
            }
        }
    }
}
