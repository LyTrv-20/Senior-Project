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
import androidx.core.content.ContextCompat;

import com.thanosfisherman.wifiutils.wifiScan.WifiScanCallback;
import com.thanosfisherman.wifiutils.wifiScan.WifiScanReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class CurrNetwork implements Wifi{

    WifiManager wifiManager;
    WifiInfo infos;
    public static CurrNetwork instance;
    List<ScanResult> scan;
    String name;

    Context context;

    wifiModel curr;
    WifiScanReceiver receiver;

    //constructor
    CurrNetwork(Context context, WifiManager wifiManager) {
        this.context = context;
        this.wifiManager = wifiManager;
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
        List<ScanResult> scantemp = scan;
        final NetworkRequest req =
                new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build();

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Executor exe = ContextCompat.getMainExecutor(context);
        WifiManager.ScanResultsCallback scanCallback = new WifiManager.ScanResultsCallback(){
            @Override
            public void onScanResultsAvailable(){
                scan = wifiManager.getScanResults();
            }
        };

        wifiManager.registerScanResultsCallback(exe, scanCallback);
        wifiManager.startScan();
        final ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback(ConnectivityManager.NetworkCallback.FLAG_INCLUDE_LOCATION_INFO) {
            @Override
            public void onAvailable(Network network) {

            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                infos = (WifiInfo) networkCapabilities.getTransportInfo();
                
                manager.unregisterNetworkCallback(this);
            }
            // etc.
        };

        

        manager.requestNetwork(req, callback); //request
        manager.registerNetworkCallback(req, callback); //listen for

        if(scan == null){
            scan = scantemp;
            wifiManager.unregisterScanResultsCallback(scanCallback);
            CharSequence txt = "Please connect to Wifi";
            Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
        }
        if(infos == null) {
            infos = temp;
            manager.unregisterNetworkCallback(callback);

            CharSequence txt = "Please connect to Wifi";
            Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
        }
    
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(context, "Turning Wifi On...", Toast.LENGTH_SHORT).show();
        }
        ScanResult w = new ScanResult();
        for (ScanResult re: scan){
            if(re.SSID == infos.getSSID()){
                w = re;
                return;
            }
        }
        System.out.println(infos.getSSID() + ":" + w.capabilities);
        System.out.println(infos.getSSID() + ":" + infos.getCurrentSecurityType());
    }

    @Override
    public void getScan(){

    }

    @Override
    public void setModel(){
//
//        curr = new wifiModel(infos.getSSID(), 0,
//                new Data());
    }
}
