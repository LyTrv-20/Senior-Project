package com.lytran.seniorproject;

public interface Wifi {
    default void scanWifi(){}
    default void setModel(){}
    record wifiModel(String name, int status, Data data){
        public String getAnalysis(){
            //TODO: change to rating output
            return "Security Type: " + data.secur
                    + "\n Encryption: " + data.encrypt
                    + "\n Integrity: " + data.integ
                    + "\n Authentication: " + data.authen;
        }
    }

    //all the cia stuff here
    //TODO: change to raw data of network
    record Data(String secur, String encrypt, String integ, String authen){}
}
