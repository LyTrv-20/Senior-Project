package com.lytran.seniorproject;

import android.net.Network;
import android.net.wifi.WifiInfo;

public class ConnectionHelper {

    public String getName(WifiInfo info){
        return info.getSSID();
    }

}
