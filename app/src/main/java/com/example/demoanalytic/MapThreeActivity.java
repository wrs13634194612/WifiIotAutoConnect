package com.example.demoanalytic;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MapThreeActivity extends AppCompatActivity {
    private CacheMap<String, StudentBean> cacheMap = new CacheMap<>();
    private CacheMap<String, String> map = new CacheMap<>();
    private HashMap<String, StudentBean> map2 = new HashMap<>();
    private List<StudentBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        StudentBean mStudentBean = new StudentBean();
        mStudentBean.setName("张飞");
        mStudentBean.setAge(18);
        StudentBean mStudentBean2 = new StudentBean();
        mStudentBean2.setName("关羽");
        mStudentBean2.setAge(21);
        map2.put("1", mStudentBean);
        map2.put("2", mStudentBean2);
        if (!list.isEmpty()) {
            list.clear();
        }
        Iterator<String> iter = map2.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Log.e("TAG", "key:" + key);
            if (TextUtils.equals(key, "1")) {
                list.add(map2.get(key));
            }
        }
        Log.e("TAG", "list:" + list);


    }
}
