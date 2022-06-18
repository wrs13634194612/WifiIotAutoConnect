package com.example.demoanalytic;



        import static android.net.wifi.SupplicantState.COMPLETED;

        import android.content.Context;
        import android.content.Intent;
        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiInfo;
        import android.net.wifi.WifiManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.text.format.Formatter;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;


        import com.google.gson.Gson;

        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;


//如何获取指定的WiFi并且自动连接: https://github.com/wrs13634194612/YueWifiConnect
public class TcpActivity extends AppCompatActivity {

    public static final String HOTPOINT_NBO = "mindor-AP-2019-07-03";
    private TextView tvCurrentWifi;
    private ImageView ivScan;

    //分割线
    private final static String TAG = "MyNet";
    private final int TYPE_QUERY = 0; //查询是否连接成功
    private final int TYPE_CONNECT = 1;   //连接指定WiFi
    private final int TYPE_CLOSE_TCP = 2; //连接指定WiFi
    private TcpClient tcpClient;
    private final static String IP_ADDRESS_OLD = "192.168.10.10";
    private final static String IP_ADDRESS_NEW = "192.168.5.1";
    private final static int PORT = 5051;
    private final static String WIFI_NAME = "mindor-AP-";
    private final static int NET_FAILED = 3;
    private final static int REC_MSG = 4;
    private final static int SEND_MSG_ERROR = 5;
    private final static int SEND_MSG_SUCCESS = 7;
    private final static int TIME_COUNT = 8;
    private final static int REC_WIFI_MSG = 9; //获取wifi名称

    private int timeCount = 0;
    private int retryCount = 0;

    private TextView tv_ap_wifi_status;

    private TextView tv_time_wifi_status,tv_time_wifi_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);

        tv_ap_wifi_status = findViewById(R.id.tv_ap_wifi_status);

        tv_time_wifi_status = findViewById(R.id.tv_time_wifi_status);

        tv_time_wifi_product = findViewById(R.id.tv_time_wifi_product);
    }


    @Override
    protected void onStart() {
        super.onStart();
        tcpClient = new TcpClient(IP_ADDRESS_OLD, PORT);
        tcpClient.setTcpSocketListener(tcpSocketListener);
        // setListen(); 这里放置下一步 去点击连接wifi  但这一步  需要用代码实现 略过手动操作
        goToNext();  //自动配网
    }


    private TcpSocketListener tcpSocketListener = new TcpSocketListener() {
        @Override
        public void onConnException(Exception e) {
            Log.e(TAG, " TCP连接异常" + e);
            handler.sendEmptyMessage(SEND_MSG_ERROR);
        }

        @Override
        public void onMessage(String s) {
            Log.d(TAG, "onMessage: " + s);
            Message message = new Message();
            message.what = REC_MSG;
            message.obj = s;
            handler.sendMessage(message);
        }

        @Override
        public void onListenerException(Exception e) {
            Log.d(TAG, "onListenerException: " + e);
            handler.sendEmptyMessage(NET_FAILED);
        }

        @Override
        public void onSendMsgSuccess(String s) {
            Log.d(TAG, "onSendMsgSuccess: " + s);
            handler.sendEmptyMessage(SEND_MSG_SUCCESS);
        }

        @Override
        public void onSendMsgException(Exception e) {
            Log.e(TAG, "发送消息时遇到异常， " + e);
            handler.sendEmptyMessage(SEND_MSG_ERROR);
        }

        @Override
        public void onCloseException(Exception e) {
            Log.e(TAG, " 当TCP连接断开时遇到异常-onCloseException :" + e);

        }

        @Override
        public void onConnected() {
            Log.e(TAG, "onConnected:端口连接成功 ");
            //连接成功就发送WiFi账号密码
            send2Device(TYPE_CONNECT);
        }
    };


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == TIME_COUNT) {
                timeCount++;
                if (timeCount > 95) {
                    timeCount = 95;
                    showErrorDialog();
                }
                tv_time_wifi_status.setText(String.valueOf(timeCount)+"%");
                handler.sendEmptyMessageDelayed(TIME_COUNT, 1000);
            } else if (message.what == NET_FAILED) {
            } else if (message.what == REC_MSG) {
                try {
                    String dataString = (String) message.obj;
                    NetEntity netEntity = new Gson().fromJson(dataString, NetEntity.class);
                    if (netEntity.getStatus() == 0) {
                        String productId = netEntity.getPRODUCTID();
                        String deviceId = netEntity.getDEVICEID();
                        tv_time_wifi_product.setText(productId);
                        Log.e(TAG, "从tcp服务端返回的数据：" + productId + "==" + deviceId);
                        send2Device(TYPE_CLOSE_TCP);
                        startBeforeWifi();
                    } else if (netEntity.getStatus() == 2 || netEntity.getStatus() == 1) {
                        //返回连接中，继续查询状态
                        if (retryCount >= 10) {
                            if (tcpClient != null) {
                                tcpClient.closeTcpSocket();
                            }
                            showErrorDialog();
                            retryCount = 0;
                        }
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                send2Device(TYPE_QUERY);
                                retryCount++;
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 1000);//3秒后执行TimeTask的run方法
                    } else if (netEntity.getStatus() == 4) {
                        showErrorDialog();
                    } else if (netEntity.getStatus() == 3) {
                        showErrorDialog();
                    } else {
                        showErrorDialog();
                    }
                    Log.e(TAG, "REC_MSG: " + netEntity.toString());
                } catch (Exception e) {
                    Log.e(TAG, "handleMessage: " + e.toString());
                }
            } else if (message.what == REC_WIFI_MSG) {


            }
            return false;
        }
    });


    //切换到可用网络
    private void startBeforeWifi() {
        handler.removeMessages(TIME_COUNT);
        //   goToNesxt2();
        //暂时注释掉
    }

    private void goToNext() {
        // String ssid = "mindor-AP-GWD001-8c20";//  "mindor-AP-GWD001-8c20" //info.getSSID();
        //获取当前wifi名称
        WifiInfo connectedWifiInfo = ((WifiManager)  getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        WifiManager wifiManager = (WifiManager)  getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isData = connectedWifiInfo.getSSID().contains(WIFI_NAME);
        Log.e(TAG, "进添加设备 页面"  + "\t" + isData);


        tv_ap_wifi_status.setText(connectedWifiInfo.getSSID());



        if (isData) {
            if (tcpClient != null) {
                tcpClient.closeTcpSocket();
            }
            String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            Log.e(TAG, "ip:" + ip);
            if (!TextUtils.isEmpty(ip) && ip.contains("192.168.5")) {
                tcpClient = new TcpClient(IP_ADDRESS_NEW, PORT);
            } else {
                tcpClient = new TcpClient(IP_ADDRESS_OLD, PORT);
            }
            tcpClient.setTcpSocketListener(tcpSocketListener);
            tcpClient.startConn();
            handler.removeMessages(TIME_COUNT);
            handler.sendEmptyMessageDelayed(TIME_COUNT, 1000);
            return;
        }
    }


    private void showErrorDialog() {
        Log.e(TAG, "错误弹窗");
    }

    /**
     * 发送消息到设备TCP服务
     * 上一个页面 传递过来的值 直接写固定 后期在改
     *
     * @param type 0 查询 1连接指定WiFi 2配网结束，请求关闭设备TCP服务器.
     */
    private void send2Device(int type) {
        NetEntity netEntity = new NetEntity();
        netEntity.setStatus(type);
        netEntity.setName("mindor");
        netEntity.setSsid("wanye_2021");
        netEntity.setPassword("123456789");
        tcpClient.sendMsg(new Gson().toJson(netEntity));
    }

}
