package com.lytran.seniorproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    /* onCreate make layout of the app */
    CurrNetwork activeNet;
    ConnectivityManager netManager;
    NetworkRequest netfilter;
    ArrayList<NetModel> local;

    WifiManager wifiManager;
    BroadcastReceiver wifiScanReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setCurrViews();
        localViewAdapter adapter = new localViewAdapter(this, local);

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details
//            return ;
//        }

    }

    private void buildConnectionclass() {
        activeNet = CurrNetwork.getInstance(this);
        netfilter = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();
        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            }
        };
        netManager = (ConnectivityManager) getSystemService(ConnectivityManager.class);
    }

    private void buildWifiClass() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false
                );

                if (success) {
                    scanSuccess();
                } else {
                    scanFailure();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

        Executor executor = ContextCompat.getMainExecutor(this);
        WifiManager.ScanResultsCallback callback = new WifiManager.ScanResultsCallback() {
            @Override
            public void onScanResultsAvailable() {
                //TODO: handle scan result
                scanSuccess();
            }
        };

        wifiManager.registerScanResultsCallback(executor,callback);
        wifiManager.startScan();
    }

    private void scanSuccess() {
        checkPermission();
        List<ScanResult> results = wifiManager.getScanResults();

    }

    private void scanFailure(){
        checkPermission();
        List<ScanResult> results = wifiManager.getScanResults();
        //TODO: continue using older scan results for views
    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details
        }
    }
    private void setLocalViews(){
        RecyclerView recyclerView = findViewById(R.id.connectionView);

        String[] name;
        int[] status;

        localViewAdapter adapter = new localViewAdapter(this, local);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    public void switchPage(View view){
//        //Intent = use to request an action from another screen
//        //startActivity = start/open a new screen
//        //startActivityForResult = getting result from opening a screen
//        startActivity(new Intent(MainActivity.this, MainActivity2.class));
//    }
}