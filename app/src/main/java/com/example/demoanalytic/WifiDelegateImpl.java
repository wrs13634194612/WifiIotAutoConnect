package com.example.demoanalytic;


        import android.Manifest;
        import android.content.Context;
        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiManager;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;



        import java.util.List;
        import java.util.concurrent.Executors;
        import java.util.concurrent.ScheduledExecutorService;
        import java.util.concurrent.TimeUnit;


public class WifiDelegateImpl implements WifiDelegate {

    private final int DELAYT_TIME = 2000; //每秒扫描一次
    private final int TOTAL_TIMES = 20;//最多扫描20次

    private ScheduledExecutorService pool;
    private volatile int index; //当前扫描的次数


    @Override
    public void wifiScan(final AppCompatActivity mActivity) {
        index = 0;


        if (pool != null) {
            pool.shutdown();
            pool = null;
        }
        pool = Executors.newScheduledThreadPool(1);

        //兼容小米手机
        if (WifiUtil.isMIUI()) {
            if (WifiUtil.checkMIwifiPermission(mActivity)) {
                WifiUtil.requestWifiPermision(mActivity);
                return;
            }

        }


        final WifiManager wifimanager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (index < TOTAL_TIMES) {

                    wifimanager.startScan();
                    Log.i("WIFI_LIST", "1 :  wifimanager.startScan()" + " index = " + index);
                    index++;
                }
            }
        }, 0, DELAYT_TIME, TimeUnit.MILLISECONDS);




    }

    @Override
    public List<ScanResult> getWifiScanResult(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();
    }

    @Override
    public int getCurrentIndex() {
        return index;
    }

    @Override
    public void stopScan() {
        if (pool != null) {
            pool.shutdown();
        }
    }

}
