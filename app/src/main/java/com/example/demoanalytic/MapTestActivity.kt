package com.example.demoanalytic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log

class MapTestActivity : AppCompatActivity() {
    //     private HashMap<String, StudentBean> map2 = new HashMap<>();
    private var map2: HashMap<String, StudentBean> = HashMap<String, StudentBean>()

    private var wifiList = mutableListOf<StudentBean>()

    private val wifi = "mindor-"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val good = "mindor-ap-10086"
        val hello = "mindor-ap-10086"

val data =             good.contains(hello)

        Log.e("TAG","hello_man:$data")




        /*
         map2.put("1", mStudentBean);
        map2.put("2", mStudentBean2);
        * */

        val mStudentBean = StudentBean()
        val mStudentBean2 = StudentBean()
        val mStudentBean3 = StudentBean()


        mStudentBean.age = 18
        mStudentBean.name = "郭靖"

        mStudentBean2.age = 22
        mStudentBean2.name = "杨过"

        mStudentBean3.age = 32
        mStudentBean3.name = "黄蓉"

        map2["mindor-001"] = mStudentBean
        map2["mindor-002"] =mStudentBean2
        map2["608"] =mStudentBean3


        Log.e("TAG", "$map2")

        /*
          if (!list.isEmpty()) {
            list.clear();
        }
        * */

        if (wifiList.isNotEmpty()){
            wifiList.clear()
        }

        val iter: Iterator<String> = map2.keys.iterator()
        while (iter.hasNext()) {
            val key = iter.next()
            Log.e("TAG", "key:$key")
        /*    if (TextUtils.equals(key, "1")) {
                map2?.let {
                    it[key]?.let { it1 -> wifiList.add(it1) }
                }
            }
                    boolean isData = connectedWifiInfo.getSSID().contains(WIFI_NAME);

            */

            if (key.contains(wifi)){
                map2?.let {
                    it[key]?.let { it1 -> wifiList.add(it1) }
                }
            }

        }

        Log.e("TAG", "key:$wifiList")



    }

}
