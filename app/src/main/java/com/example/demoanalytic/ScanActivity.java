package com.example.demoanalytic;



        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiInfo;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;



        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;

public class ScanActivity extends AppCompatActivity implements ScanResultListener {
    private YueWifiHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new YueWifiHelper(this, this);
        Button btn_scan_start = findViewById(R.id.btn_scan_start);
        btn_scan_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始扫描
                helper.startScan();

            }
        });
    }

    @Override
    public void resultSuc(List<ScanResult> list, boolean isLastTime) {
        Log.e("TAG","resultSuc:"+list.size()+"\t"+isLastTime);
        /**
         扫出来了  然后做什么呢   扫出来了所有的wifi列表  然后做配对 名称  筛选
         这是一个扫描循环
         * */
        if(isLastTime){
            Message message = new Message();
            message.what = 751;
            message.obj = list;
            handler.sendMessage(message);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 751) {

            }
            return false;
        }
    });

    @Override
    public void filterFailure() {
        Log.e("TAG","filterFailure error:");
    }

    @Override
    public void connectedWifiCallback(WifiInfo info) {
        Log.e("TAG","connectedWifiCallback list:"+info.getSSID());
    }
}
