# Wifi-Data - Android application displaying all the attributes of an available wi-fi 
**Application Interface :** The interface of the application is quite minimalistic. It contains a listview displaying the list of available wifi devices with the following useful details :
1. SSID - The network name.
2. BSSID - The address of the access point.
3. Capabilities - Describes the authentication, key management, and encryption schemes supported by the access point.
4. Level - The detected signal level in dBm, also known as the RSSI.
5. Frequency - The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating with the access point.
6. Timestamp - timestamp in microseconds (since boot) when this result was last seen.

There is also a refresh button which enables us to refresh the list and get new scan results.

Source code snippets explained :
1. When the application starts it checks if the wifi is enabled, if it is not the application enables the wifi. The code underlying this process is given below.

        if ​(mainWifi​.isWifiEnabled() == false​)
        {
          // If wifi disabled then enable it
          Toast.makeText(getApplicationContext(), "WiFi is Disabled.. Enabling Wi-Fi!!"​,Toast.LENGTH_LONG​).show();
          mainWifi​.setWifiEnabled(true​);
        }
This process requires the permissions to access and change the wifi status which is added in the manifest file :
        
        <uses-permission ​android​:name=​"android.permission.ACCESS_WIFI_STATE" ​/>
        <uses-permission ​android​:name=​"android.permission.CHANGE_WIFI_STATE" ​/>
        

2. Android provides WifiManager​ API to manage all aspects of WIFI connectivity.We can instantiate this class by calling getSystemService​ method. Its syntax is given below −  
        
        WifiManager mainWifi​;
        mainWifi ​= (WifiManager) getSystemService(Context.WIFI_SERVICE​);
3. In order to scan a list of wireless networks, you also need to register your BroadcastReceiver. It can be registered using registerReceiver​ method with argument of your receiver class object. Its syntax is given below −

        class ​WifiReceiver extends ​BroadcastReceiver {
          public void ​onReceive(Context c, Intent intent) {
            sb ​= new ​StringBuilder();
            wifiList ​= mainWifi​.getScanResults();
            sb​.append("​\n​ Number Of Wifi connections:"​+wifiList​.size()+"​\n\n​"​);
            for​(int ​i = 0; i < wifiList​.size(); i++){
              ssidList​.add(wifiList​.get(i).SSID​.toString());
              bssidList​.add(wifiList​.get(i).BSSID​.toString());
              cap​.add(wifiList​.get(i).capabilities​.toString());
              level​.add(String.valueOf(wifiList​.get(i).level​));
              freq​.add(String.valueOf(wifiList​.get(i).frequency​));
              if ​(Build.VERSION.SDK_INT ​>= Build.VERSION_CODES.JELLY_BEAN_MR1​) {
                timestmp​.add(String.valueOf(wifiList​.get(i).timestamp​));
              }
              completeStuff​.add(new ​Integer(i+1).toString() +"."​+wifiList​.get(i).toString());
              sb​.append(new ​Integer(i+1).toString() + ". "​);
              sb​.append((wifiList​.get(i)).toString());
              sb​.append("​\n\n​"​);
            }
            mainText​.setText(" Number Of Wifi connections :"​+wifiList​.size());
          }
       }
Here, we are registering the broadcastreceiver and making it equipped with the instructions we want the application to follow. On receiving the results of wifi scan we store the different values like ssid, bssid etc in their respective arrays. We also update the mainText with the number of active wifi connections available.

Screenshots of the app are attached below:
The first shows the scanning window and the second shows the value retrieved by the application. (Ignore the font :stuck_out_tongue:)
![Image of Yaktocat](https://github.com/lovemehta/images/blob/master/Wifi-DataScreenshot.png)
