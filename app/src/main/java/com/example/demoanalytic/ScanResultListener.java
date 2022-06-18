package com.example.demoanalytic;



        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiInfo;

        import java.util.HashMap;
        import java.util.List;

public interface ScanResultListener {
    void resultSuc(List<ScanResult> list, boolean isLastTime);

    void resultMapSuc(HashMap<String, ScanResult> map, boolean isLastTime);


    void filterFailure();

    void connectedWifiCallback(WifiInfo info, boolean isLastTime);
}
