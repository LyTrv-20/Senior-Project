package com.lytran.seniorproject;

import android.net.Network;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


//TODO: research best way to get capabilities and infos (scan results or wifi info)
public class Rate {

    private static double getA(List<ScanResult> s, String name){
        List<String> ogBSSID = new ArrayList<>();

        for(ScanResult scan: s){
            if(scan.SSID.contains(name))
                ogBSSID.add(scan.BSSID);
        }

        int c = ogBSSID.size();
        if(c > 2)
            return Constant.Rates.HIGH;
        else if(c > 1)
            return Constant.Rates.MID;
        else
            return Constant.Rates.LOW; //out of 5
    }

    //TODO: discuss with mentor to expand detection for security type
    private static double rateSecurity(ScanResult scan){
        double tol = 0;
        String cap = scan.capabilities;
        if(cap.contains("ESS"))
            tol += Constant.Rates.NONE;
        else if(cap.contains("WEP")){
            tol += Constant.Rates.VERY_L;
        }
        else if(cap.contains("WPA2")){
            tol += Constant.Rates.HIGH;
        }else if(cap.contains("WPA3") || cap.contains("IEEE8021X")){
            tol += Constant.Rates.VERY_H;
        }else if(cap.contains("WPA")){
            tol += Constant.Rates.LOW;
        }
        else
            tol += Constant.Rates.MID;

        if(cap.contains("WPS")) {
            tol -= 5;
        }

        return tol; //out of 10
    }

    private static double rateI(ScanResult scan){
        String[] authen = new String[]{"OWE", "TKIP", "PSK", "CCMP", "EAP", "SAE"};
        double tol = 0;
        for(int i = 0; i < authen.length; i ++){
            if(scan.capabilities.contains(authen[i]))
                tol += i;
        }
        return tol; //out of 15
    }

    public static double rate(ScanResult net, List<ScanResult> scan){
        double tol = rateSecurity(net) + rateI(net) + getA(scan, net.SSID);
        return tol / 30;
    }

}
