package com.lytran.seniorproject;

import android.net.wifi.WifiInfo;

public class Constant {
    public class Rates{
        public static double NONE = 0;
        public static double VERY_L = 0.5;
        public static double LOW = 1;
        public static double MID = 3;
        public static double HIGH = 5;
        public static double VERY_H = 10;
    }


    public class Permissions{
        public static int COARSE_LOC = 0;
        public static int FINE_LOC = 1;
        public static int WIFI_STATE =  2;
    }
    public static String GetSecurity(int a){
        switch(a){
            case WifiInfo.SECURITY_TYPE_OPEN:
                return "Open";
            case WifiInfo.SECURITY_TYPE_PSK:
                return "PSK";
            case WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE, WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT:
                return "WPA3";
            case WifiInfo.SECURITY_TYPE_WEP:
                return "WEP";
            case WifiInfo.SECURITY_TYPE_EAP:
                return  "EAP";
            case WifiInfo.SECURITY_TYPE_OSEN:
                return "OSEN";
            case WifiInfo.SECURITY_TYPE_OWE:
                return "OWE";
            case WifiInfo.SECURITY_TYPE_SAE:
                return "SAE";
            case WifiInfo.SECURITY_TYPE_WAPI_PSK, WifiInfo.SECURITY_TYPE_WAPI_CERT:
                return "WAPI";
            default:
                return "Unknown";
        }
    }
}
