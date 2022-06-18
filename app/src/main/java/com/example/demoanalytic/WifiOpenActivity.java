package com.example.demoanalytic;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.net.wifi.WifiManager;
        import android.os.Handler;
        import android.os.Looper;
        import android.os.Message;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.Switch;

        import com.google.gson.Gson;

        import java.util.Timer;
        import java.util.TimerTask;

        import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
        import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

public class WifiOpenActivity extends AppCompatActivity {
    private WifiManager wifiManager;
    private Switch wifiSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
/*        wifiSwitch = findViewById(R.id.wifi_switch);
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    wifiSwitch.setText("WiFi is ON");
                } else {
                    wifiManager.setWifiEnabled(false);
                    wifiSwitch.setText("WiFi is OFF");
                }
            }
        });*/

        Button btn_open = findViewById(R.id.btn_open);
        Button btn_close = findViewById(R.id.btn_close);
        Button btn_status = findViewById(R.id.btn_status);



        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(true);
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(false);
            }
        });
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int age =     wifiManager.getWifiState();

            Log.e("TAG","wifi status:"+age);   // 1  是关  3 是开

            }
        });


        //这就是一整个完整的wifi 开关逻辑

        /*

Must be one of:
WifiManager.WIFI_STATE_DISABLING,
 WifiManager.WIFI_STATE_DISABLED,
  WifiManager.WIFI_STATE_ENABLING,
   WifiManager.WIFI_STATE_ENABLED,
    WifiManager.WIFI_STATE_UNKNOWN
        * */
//        WifiManager.WIFI_STATE_DISABLING,


        if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLED){
            wifiManager.setWifiEnabled(true);
        }else if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            wifiManager.setWifiEnabled(false);
        }



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLED){
                    wifiManager.setWifiEnabled(true);
                }else if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
                    wifiManager.setWifiEnabled(false);
                }
            }
        },2000);




    }




    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 101) {

            }
            return false;
        }
    });





 /*   @Override
    protected void onStart() {
        super.onStart();
       // IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
      //  registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  unregisterReceiver(wifiStateReceiver);
    }*/

  /*  private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiSwitch.setChecked(true);
                    wifiSwitch.setText("WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    wifiSwitch.setChecked(false);
                    wifiSwitch.setText("WiFi is OFF");
                    break;
            }
        }
    };*/
}
