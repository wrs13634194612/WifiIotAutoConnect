//package com.example.demoanalytic;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.net.wifi.ScanResult;
//import android.view.View;
//import android.widget.Button;
//
//import com.google.gson.Gson;
//
//public class TcpApActivity extends AppCompatActivity {
//    private String WIFI_NAME = "mindor-AP-"; //  mindor-AP-GWD001-8c20
//
//    private YueWifiHelper   helper;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tcp);
//
//
//
//        helper = new YueWifiHelper();
//
//
//        ScanResult person = (ScanResult) getIntent().getParcelableExtra("test");
//
//        Log.e("TAG", "" + person  );
//
//        Button btn_start_connect_wifi = findViewById(R.id.btn_start_connect_wifi);
//
//        btn_start_connect_wifi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                             helper.filterAndConnectTargetWifi2(person, WIFI_NAME, true);
//
//            }
//        });
//
//
//    }
//
//}
