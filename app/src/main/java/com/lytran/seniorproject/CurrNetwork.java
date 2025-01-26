package com.lytran.seniorproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.*;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class CurrNetwork implements Wifi{

    WifiManager wifiManager;
    WifiInfo infos;
    public static CurrNetwork instance;
    List<ScanResult> scan;
    String name;

    Context context;

    wifiModel curr;


    public static synchronized CurrNetwork getInstance(Context context) {
        if (instance == null) instance = new CurrNetwork(context);
        return instance;
    }

    //constructor
    private CurrNetwork(Context context) {
        this.context = context;

        scanWifi();

    }

    //authentication, encryption scheme, & key management
    public String getCapabilities(){
        ArrayList<String> result = new ArrayList<>();
        for(ScanResult s: scan){
            if(s.SSID.equals(name))
                result.add(s.capabilities);

        }
        return result.toString();
    }

    //TODO: scan or info to get security
    public String getSecurityType() {
        return "";
    }

    public String getCurrName(){
        return name;
    }

    private boolean isConnected(){
        return infos != null && infos.getNetworkId() != -1;
    }

    @Override
    public void scanWifi(){
        WifiInfo temp = infos; //get last infos
        final NetworkRequest req =
                new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build();

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback(ConnectivityManager.NetworkCallback.FLAG_INCLUDE_LOCATION_INFO) {
            @Override
            public void onAvailable(Network network) {}

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                infos = (WifiInfo) networkCapabilities.getTransportInfo();
                manager.unregisterNetworkCallback(this);
            }
            // etc.
        };

        manager.requestNetwork(req, callback); //request
        manager.registerNetworkCallback(req, callback); //listen for changes


        if(infos == null) {
            infos = temp;
            manager.unregisterNetworkCallback(callback);

            CharSequence txt = "Please connect to Wifi";
            Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setModel(){

    }
}
