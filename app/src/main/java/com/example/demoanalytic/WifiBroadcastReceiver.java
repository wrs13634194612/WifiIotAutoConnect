package com.example.demoanalytic;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.List;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    public final int TOTAL_TIME = 20;
    private WifiDelegate delegate;
    private ScanResultListener listener;
    private HashMap<String, ScanResult> cacheMap = new HashMap<>();


    public WifiBroadcastReceiver(WifiDelegate delegate, ScanResultListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) { //扫描到WiFi列表
            if (delegate != null) {
                List<ScanResult> results = delegate.getWifiScanResult(context);
                Log.i("WIFI_LIST", "2 :  WifiBroadcastReceiver#onReceive" + " currentIndex = " + delegate.getCurrentIndex() + " results.size = " + results.size());

                for (int index = 0;index < results.size(); index++) {
                    cacheMap.put(results.get(index).SSID,results.get(index));
                }
                if (listener != null) {
                    /*if (delegate.getCurrentIndex() == TOTAL_TIME) {
                       // listener.resultSuc(results, true);
                    } else {
                      //  listener.resultSuc(results, false);
                    }*/
                    listener.resultMapSuc(cacheMap,false);
                }
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) { //连接的WiFi变化了
            WifiInfo connectedWifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
            if (listener != null) {
                listener.connectedWifiCallback(connectedWifiInfo, true);
            }
        }
    }
}
