package com.lytran.seniorproject;

import android.net.Network;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import java.util.ArrayList;
import java.util.List;


//TODO: research best way to get capabilities and infos (scan results or wifi info)
public class Rate {

    private int getA(List<ScanResult> s){
        List<String> ogBSSID = new ArrayList<>();
        ogBSSID.add(s.get(0).BSSID);

        for(ScanResult scan: s){
            if(!ogBSSID.contains(scan.BSSID))
                ogBSSID.add(scan.BSSID);
        }

        int c = ogBSSID.size();
        if(c > 2)
            return Constant.Rates.HIGH;
        else if(c > 1)
            return Constant.Rates.MID;
        else
            return Constant.Rates.LOW;
    }

    //TODO: discuss with mentor to expand detection for security type
    private static int getC(WifiInfo net){
        if(net.getCurrentSecurityType() == WifiInfo.SECURITY_TYPE_OPEN)
            return Constant.Rates.NONE;
        else if(net.getCurrentSecurityType() == WifiInfo.SECURITY_TYPE_WEP){
            return Constant.Rates.LOW;
        }
        else if(net.getCurrentSecurityType() == WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE){
            return Constant.Rates.HIGH;
        }
        else
            return Constant.Rates.MID;
    }

    public static int rate(WifiInfo net){
        return getC(net);
    }

    public static double rate(ScanResult scanResult){
        return 0;
    }

}
