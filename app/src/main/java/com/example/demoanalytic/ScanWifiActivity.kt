package com.example.demoanalytic

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class ScanWifiActivity : AppCompatActivity(),ScanResultListener {
    private lateinit var helper : YueWifiHelper
    private var wifiList = mutableListOf<ScanResult>()
    private var wifiNewList = mutableListOf<ScanResult>()


    private var WIFI_NAME = "mindor-AP-"
    //  mindor-AP-GWD001-8c20
    private lateinit var s: RecyclerView

    private lateinit var adapter: IrBrandAdapterAp



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helper = YueWifiHelper(this,this)
        btn_scan_start.setOnClickListener {
            helper.startScan()
        }
    }

    override fun resultSuc(list: MutableList<ScanResult>?, isLastTime: Boolean) {
        if(isLastTime){
            val msg = Message()
            msg.what = 824
            msg.obj = list
            phoneLoginHandler.sendMessage(msg)
            //数据拿到了  停止扫描
            helper.stop()
        }
    }

    private var phoneLoginHandler = Handler() {
        if (it.what ==824) {
            val age=it.obj as MutableList<ScanResult>
            Log.e("TAG","handler:${age}")

            if (wifiList.isNotEmpty()){
                wifiList.clear()
            }
            if (wifiNewList.isNotEmpty()){
                wifiNewList.clear()
            }
            wifiList.addAll(age)
            Log.e("TAG","wifiList:${age}")  //拿到了  接下来去做筛选  然后连接
            for (i in wifiList.indices) {
            //    dates[i].isCheck =this.dateInt.contains(dates[i].day.toString())
                if(wifiList[i].SSID.indexOf(WIFI_NAME) !=-1 ){
                    wifiNewList.add(wifiList[i])
                }
            }
            //       .///     indexof !=-1  //就是包含  拿到了  接下来是显示连接
            Log.e("TAG","wifiList:${wifiNewList}")  //拿到了 接下来去做连接 然后连接
            adapter = IrBrandAdapterAp(wifiNewList)
            rv_ir_brand.layoutManager = LinearLayoutManager(this)
            rv_ir_brand.adapter = adapter
            adapter.setOnItemClickListener { item, position ->
                Log.e("TAG","adapter:$position")
                // 连接指定wifi
                //  helper.filterAndConnectTargetWifi(list, HOTPOINT_NBO, isLastTime);
                helper.filterAndConnectTargetWifi2(wifiNewList[position],WIFI_NAME,true)
            }
        }else if(it.what == 825){
            val info=it.obj as WifiInfo
            tv_wifi_status.text = "连接成功：${info.ssid}"
            // 连接成功 跳转新界面  跳转到原来的界面 把这个代码集成进去吧  这是最关键的一步  ap跟推送没影响的  那天晚上其实已经试过了
        }else if(it.what==826){
            val info=it.obj as WifiInfo
            tv_wifi_status.text = "连接失败：${info.ssid}"
        }
        false
    }

    override fun filterFailure() {
        Log.e("TAG","filterFailure error:")
    }

    override fun connectedWifiCallback(info: WifiInfo?) {
        val isConnect = helper.isConnected(info)
        var connectStatus = if (isConnect){
            825
        }else{
            826
        }
        val msg = Message()
        msg.what = connectStatus
        msg.obj = info
        phoneLoginHandler.sendMessage(msg)
    }
}