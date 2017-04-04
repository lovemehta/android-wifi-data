package com.lovemehta.www.myapplication;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    ArrayList<String> completeStuff = new ArrayList<String>();
    ArrayList<String> ssidList = new ArrayList<String>();
    ArrayList<String> bssidList= new ArrayList<String>();
    ArrayList<String> cap= new ArrayList<String>();
    ArrayList<String> level= new ArrayList<String>();
    ArrayList<String> freq= new ArrayList<String>();
    ArrayList<String> timestmp= new ArrayList<String>();
    ArrayList<String> dist= new ArrayList<String>();
    ArrayList<String> distSd= new ArrayList<String>();

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
    Button refresh;
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, completeStuff);
        mainText = (TextView) findViewById(R.id.mainText);
        refresh = (Button) findViewById(R.id.button);
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "WiFi is Disabled.. Enabling Wi-Fi !!",
                    Toast.LENGTH_LONG).show();

            mainWifi.setWifiEnabled(true);
        }
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainWifi.startScan();
                mainText.setText("Starting Scan...");
                completeStuff.clear(); //remember to cear others before refreshing
                listView.setAdapter(adapter);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mainWifi.startScan();
        mainText.setText("Starting Scan");
        return super.onMenuItemSelected(featureId, item);
    }

    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    // Broadcast receiver class called its receive method
    // when number of wifi connections changed

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            sb.append("\n        Number Of Wifi connections :"+wifiList.size()+"\n\n");

            for(int i = 0; i < wifiList.size(); i++){
                ssidList.add(wifiList.get(i).SSID.toString());
                bssidList.add(wifiList.get(i).BSSID.toString());
                cap.add(wifiList.get(i).capabilities.toString());
                level.add(String.valueOf(wifiList.get(i).level));
                freq.add(String.valueOf(wifiList.get(i).frequency));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    timestmp.add(String.valueOf(wifiList.get(i).timestamp));
                }

                completeStuff.add(new Integer(i+1).toString() +". "+wifiList.get(i).toString());

                sb.append(new Integer(i+1).toString() + ". ");
                sb.append((wifiList.get(i)).toString());
                sb.append("\n\n");
            }

            mainText.setText(" Number Of Wifi connections :"+wifiList.size());
        }

    }
}