package com.lytran.seniorproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.*;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.thanosfisherman.wifiutils.wifiScan.WifiScanCallback;
import com.thanosfisherman.wifiutils.wifiScan.WifiScanReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class CurrNetwork implements Wifi{

    WifiManager wifiManager;
    WifiInfo infos;
    ScanResult scan;
    String name;

    Context context;

    wifiModel curr;
    Activity act;

    //constructor
    CurrNetwork(Context context, Activity act) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.act = act;
        scanWifi();
        this.name = infos.getSSID();
    }

    //authentication, encryption scheme, & key management
    public String getCapabilities(){
        return scan == null? "Scanning Wifi...": scan.capabilities;
    }

    public void setScan(List<ScanResult> scan){
        for (ScanResult scanResult : scan) {
            if (!scanResult.SSID.trim().isEmpty() && name.contains(scanResult.SSID)) {
                this.scan = scanResult;
                curr = new wifiModel(scanResult.SSID, Rate.rate(scanResult, scan), new Data(scanResult));
            }
        }
    }

    public String getCurrName(){
        return scan == null? "Scanning Wifi...": infos.getSSID().replace("\"","");
    }

    @Override
    public void scanWifi(){
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (manager.isWifiEnabled())
            {
                WifiInfo wifiInfo = manager.getConnectionInfo();
                if (wifiInfo != null)
                {
                    NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                    if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR)
                    {
                        infos = wifiInfo;
                    }
                    else
                    {
                        Toast.makeText(context,"WIFI not connected", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(context,"No WIFI Information", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context,"WIFI not enabled", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception xx)
        {
            Toast.makeText(context, xx.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
