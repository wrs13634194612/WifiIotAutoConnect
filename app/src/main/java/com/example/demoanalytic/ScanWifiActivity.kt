package com.example.demoanalytic

import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.SupplicantState.COMPLETED
import android.net.wifi.WifiInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap


class ScanWifiActivity : AppCompatActivity(), ScanResultListener {
    private lateinit var helper: YueWifiHelper
    private var wifiList = mutableListOf<ScanResult>()
    private var wifiOldList = mutableListOf<ScanResult>()
    private var WIFI_NAME = "mindor-AP-" //  mindor-AP-GWD001-8c20
    private lateinit var adapter: IrBrandAdapterAp
    private var isView  = false
    private val TAG="ScanWifiActivity"
    private lateinit var mScanResult : ScanResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helper = YueWifiHelper(this, this)
        btn_scan_start.setOnClickListener {
            helper.startScan()
        }
        adapter = IrBrandAdapterAp(wifiList)
        rv_ir_brand.layoutManager = LinearLayoutManager(this)
        rv_ir_brand.adapter = adapter
        adapter.setOnItemClickListener { item, position ->
           Log.e(TAG, "adapter:$position")
            /*helper.filterAndConnectTargetWifi2(wifiLisst[position], WIFI_NAME, true)
            isView = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                if (isView){
                    Log.e(TAG, "tr连接成功 界面 跳转 :")
                    isView = false  //执行完以后  必须改为false
                    //这里跳转到tcp连接的页面
                    val intent  = Intent(this@ScanWifiActivity,TcpActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },7000)*/

            val intent = Intent(this@ScanWifiActivity, ScanApActivity::class.java)
            //val data = Gson().toJson(wifsiList[position])
            intent.putExtra("test", wifiList[position])
            intent.putExtra("wifi", wifiOldList[0])
            startActivity(intent)
        }
    }

    private var phoneLoginHandler = Handler() {
        if (it.what == 825) {
            val info = it.obj as WifiInfo
            tv_wifi_status.text = "tr连接状态：${info.ssid}"
            isView = info.supplicantState == COMPLETED && info.ssid.contains(WIFI_NAME)
        }
        false
    }

    override fun resultMapSuc(map: HashMap<String, ScanResult>?, isLastTime: Boolean) {
        if (wifiList.isNotEmpty()){
            wifiList.clear()
        }
        if (wifiOldList.isNotEmpty()){
            wifiOldList.clear()
        }
        map?.let {
            val iter: Iterator<String> = it.keys.iterator()
            while (iter.hasNext()) {
                val key = iter.next()
                Log.e("TAG", "key:$key")
                if (key.contains(WIFI_NAME)){
                        it[key]?.let { it -> wifiList.add(it) }
                }else if(TextUtils.equals(key,"wanye_2021")){
                    it[key]?.let { it -> wifiOldList.add(it) }
                }
            }
            Log.e("TAG", "key:$wifiList,$wifiOldList")
        }
        adapter.notifyDataSetChanged() //刷新数据
    }

    override fun resultSuc(list: MutableList<ScanResult>?, isLastTime: Boolean) {
    }

    override fun filterFailure() {
        Log.e(TAG, "filterFailure error:")
    }

    override fun connectedWifiCallback(info: WifiInfo?, isLastTime: Boolean) {
        val msg = Message()
        msg.what = 825
        msg.obj = info
        phoneLoginHandler.sendMessageDelayed(msg,5000)// geDelayed(msg,5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.destroy()
    }
}