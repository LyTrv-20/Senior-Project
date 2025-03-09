package com.lytran.seniorproject;


import android.content.Context;
import android.net.wifi.ScanResult;

import java.util.List;

public interface Wifi {
    default void scanWifi(){}
    record wifiModel(String name, double status, Data data){
        public String getAnalysis(){
            //TODO: change to rating output
            return "Security Type: " + data.getSecurity()
                    + "; Encryption: " + data.getEncryption()
                    + "; Authentication: " + data.getAuthen();
        }
        public String getAbstract(){
            return "Security Type: " + data.getSecurityAb()
                    + "; Encryption: " + data.getEncryptionAb()
                    + "; Authentication: " + data.getAuthenAb();
        }
    }

    //all the cia stuff here
    //TODO: change to raw data of network
    record Data(ScanResult scan){
        public String getSecurity(){
            final String cap = scan.capabilities;
            final String[] securityModes = {"WEP", "WPA", "WPA2", "WPA_EAP", "WPA3", "IEEE8021X" };
            for (int i = securityModes.length - 1; i >= 0; i--) {
                if (cap.contains(securityModes[i])) {
                    return securityModes[i];
                }
            }

            return "OPEN";
        }
        public String getSecurityAb(){
            final String cap = scan.capabilities;
            final String[] securityModes = {"WEP", "WPA", "WPA2", "WPA_EAP", "WPA3", "IEEE8021X" };
            for (int i = securityModes.length - 1; i >= 0; i--) {
                if (cap.contains(securityModes[i])) {
                    return i > 3 ? "HIGH" : i > 1 ? "MEDIUM" : "LOW";
                }
            }
            return "NONE";
        }
        public String getAuthen(){
            String[] authen = new String[]{"OSA", "PSK","EAP","SAE"};
            for(String re: authen){
                if(scan.capabilities.contains(re))
                    return re;
            }
            return "NONE";
        }

        public String getAuthenAb(){
            String[] authen = new String[]{"OSA", "PSK","EAP","SAE"};
            for(int i = 0; i < authen.length; i++) {
                if (scan.capabilities.contains(authen[i]))
                    return i == 0 ? "LOW" : i > 1? "HIGH": "MEDIUM";
            }
            return "NONE";
        }
        public String getEncryption(){
            String[] authen = new String[]{"OWE", "TKIP", "CCMP", "SHA"};
            for(String re: authen){
                if(scan.capabilities.contains(re))
                    return re;
            }
            return "NONE";
        }

        public String getEncryptionAb(){
            String[] authen = new String[]{"OWE", "TKIP", "CCMP", "SHA"};
            for(int i = 0; i < authen.length; i++) {
                if (scan.capabilities.contains(authen[i]))
                    return i == 0 ? "LOW" : i > 1? "HIGH": "MEDIUM";;
            }
            return "NONE";
        }

    }
}
