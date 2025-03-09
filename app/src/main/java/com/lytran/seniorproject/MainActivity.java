package com.lytran.seniorproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.lytran.seniorproject.Constant.Permissions.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    /* onCreate make layout of the app */
    CurrNetwork activeNet;
    ConnectivityManager netManager;
    NetworkRequest netfilter;
//    ArrayList<Wifi.wifiModel> local;
    ArrayList<ScanResult> local;

    WifiManager wifiManager;
    localViewAdapter adapter;
    FloatingActionButton tog;
    boolean isAbs = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        activeNet = new CurrNetwork(this, this);

         tog= findViewById(R.id.toggle);

        if(local == null)
            tog.setVisibility(View.INVISIBLE);



        grantPermm();
        getWifiScan();
        setCurrent();
    }



    private void grantPermm()
    {

        try
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                        MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOC);

            }if(ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOC);
            }
            else
            {
                wifiManager.startScan();
            }

        }
        catch (Exception xx){}

    }
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        wifiScanReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//                if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
//                    @SuppressLint("MissingPermission") List<ScanResult> wifiList = wifiManager.getScanResults();
//                    setLocalViews(wifiList);
//                    Toast.makeText(context, "Wifi Scan Get", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        registerReceiver(wifiScanReceiver, intentFilter);
//        getWifi();
//    }
//    private void getWifi() {
//        boolean state = false, fineLoc = false;
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOC);
//        } else {
//            wifiManager.startScan();
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE)
//                != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, WIFI_STATE);
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOC);
//
//        wifiManager.getScanResults();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(wifiScanReceiver);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
//            wifiManager.startScan();
//        } else {
//            Toast.makeText(MainActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
//            return;
//        }
//    }

    private void getWifiScan() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        // Ensure permissions before registering receiver
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        // Register the receiver BEFORE starting the scan
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(MainActivity.this, "Scan received", Toast.LENGTH_SHORT).show();
                Log.d("WiFiScan", "Scan results received");

                // Ensure permission is granted before accessing results
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestLocationPermission();
                    return;
                }

                // Get scan results
                List<ScanResult> results = wifiManager.getScanResults();
                Log.d("WiFiScan", "Scan results size: " + results.size());

                if (results.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Scan empty", Toast.LENGTH_SHORT).show();
                    Log.d("WiFiScan", "Scan results are empty");
                } else {
                    local = new ArrayList<>(results);
                    activeNet.setScan(results);
                    Toast.makeText(MainActivity.this, "Scan not empty", Toast.LENGTH_SHORT).show();
                    Log.d("WiFiScan", "Scan results are not empty: " + results.size());
                    tog.setVisibility(View.VISIBLE);
                    tog.setOnClickListener(view -> {
                        isAbs = !isAbs;
                        setCurrent();
                        adapter = new localViewAdapter(MainActivity.this, local, activeNet.getCurrName(), isAbs);
                        adapter.notifyDataSetChanged();
                        setLocalViews();
                    });
                    adapter = new localViewAdapter(MainActivity.this, local, activeNet.getCurrName(), isAbs);
                    adapter.notifyDataSetChanged();
                    setLocalViews();
                    setCurrent();
                    View need = findViewById(R.id.needle);
                    need.setRotation((float) MathUtil.interpolate(-90, 90, Rate.rate(activeNet.scan, local)));
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Start the scan AFTER registering the receiver
        boolean scanStarted = wifiManager.startScan();
        if (!scanStarted) {
            Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show();
            Log.d("WiFiScan", "WiFi scan failed");
        } else {
            Log.d("WiFiScan", "WiFi scan started successfully");
        }
    }

    // Function to request permission until granted
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, restart scan
                getWifiScan();
            } else {
                // Permission denied, ask again
                requestLocationPermission();
            }
        }
    }
    private void setCurrent(){
        TextView name = findViewById(R.id.currNetworkName);
        TextView infos = findViewById(R.id.currInfo);
//        Wifi.wifiModel curre = activeNet.curr;

        activeNet.scanWifi();
        name.setText(activeNet.getCurrName());
        if(activeNet.scan == null)
            infos.setText(activeNet.getCapabilities());
        else if(!isAbs)
            infos.setText(activeNet.curr.getAnalysis());
        else
            infos.setText(activeNet.curr.getAbstract());
    }

    private void setLocalViews(){

        RecyclerView recyclerView = findViewById(R.id.connectionView);
//        String[] name;
//        int[] status;

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